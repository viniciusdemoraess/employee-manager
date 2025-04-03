package com.seplag.employee_manager.domain.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.security.MessageDigest;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class MinioService {

    private MinioClient minioClient;
    private String bucketName = "servidores";

    public MinioService() {
        this.minioClient = MinioClient.builder()
            .endpoint("http://localhost:9000")
            .credentials("minio-access", "minio-secret")
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
}

