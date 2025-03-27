package com.seplag.employee_manager.domain.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class MinioService {

    private MinioClient minioClient;
    private String bucketName = "servidores";

    public MinioService() {
        this.minioClient = MinioClient.builder()
            .endpoint("http://minio:9000")
            .credentials("minioaccesskey", "miniosecretkey")
            .build();
    }

    public String uploadFile(MultipartFile file) throws Exception {
        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        minioClient.putObject(PutObjectArgs.builder()
            .bucket(bucketName)
            .object(fileName)
            .stream(file.getInputStream(), file.getSize(), -1)
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
}

