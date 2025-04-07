package com.seplag.employee_manager.domain.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;
import io.minio.GetPresignedObjectUrlArgs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.security.MessageDigest;
import java.util.concurrent.TimeUnit;

@Service
public class MinioService {

    private MinioClient minioClient;

    @Value("${minio.bucket-name:servidores}")
    private String bucketName;

    @Value("${minio.endpoint}")
    private String url;

    @Value("${minio.access-key:minioaccesskey}")
    private String login;

    @Value("${minio.secret-key:miniosecretkey}")
    private String password;

    @PostConstruct
    public void init() {
        this.minioClient = MinioClient.builder()
            .endpoint(url)
            .credentials(login, password)
            .build();
    }

    public String uploadFile(MultipartFile file, String fileName) throws Exception {
        
        minioClient.putObject(PutObjectArgs.builder()
            .bucket(bucketName)
            .object(fileName)
            .stream(file.getInputStream(), file.getSize(), -1)
            .contentType(file.getContentType())
            .build());
        return fileName;
    }

    public String getFileUrl(String fileName) {
        try {
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucketName)
                .object(fileName)
                .expiry(5, TimeUnit.MINUTES)
                .build());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar URL temporária", e);
        }
    }

    public String calculateSha256(InputStream inputStream) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            digest.update(buffer, 0, bytesRead);
        }
        byte[] hashBytes = digest.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString().substring(0, 46); // Limita para 50 caracteres
    }

    public void deleteFile(String hash) {
        try {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(hash)
                    .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar arquivo do MinIO", e);
        }
    }
}

