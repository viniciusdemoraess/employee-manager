package com.seplag.employee_manager.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;

@Component
public class MinioInitializer {

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
    public void createBucketIfNotExists() {
        this.minioClient = MinioClient.builder()
            .endpoint(url)
            .credentials(login, password)
            .build();
       
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());

            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            } else {
                System.out.println("✔️ Bucket já existe: " + bucketName);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar/verificar bucket: " + e.getMessage(), e);
        }
    }
}
