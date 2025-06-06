package org.example.controllers;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.config.ConfigProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/photos")
@RequiredArgsConstructor
public class PhotoController {

    private S3Client s3Client;
    private final ConfigProperties configProperties;


    @PostConstruct
    private void initS3Client() {
        AwsCredentials credentials = AwsBasicCredentials.create(
                configProperties.getKeyId(),
                configProperties.getSecretKey());

        s3Client = S3Client.builder()
                .httpClient(ApacheHttpClient.create())
                .region(Region.of(configProperties.getRegion()))
                .endpointOverride(URI.create(configProperties.getS3_endpoint()))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    @PutMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public String uploadFile(
            @RequestParam("photo") MultipartFile photo
    ) throws IOException {

        String originalFilename = photo.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String uniqueFilename = "photos/" + UUID.randomUUID() + extension;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(configProperties.getBucket())
                .key(uniqueFilename)
                .contentType(photo.getContentType())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(photo.getBytes()));

        return uniqueFilename;
    }


    @GetMapping
    public ResponseEntity<byte[]> downloadFile(@RequestParam String key) throws IOException {

        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(configProperties.getBucket())
                .key(key)
                .build();

        var inputStream = s3Client.getObject(objectRequest);
        byte[] data = inputStream.readAllBytes();

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline");
        headers.add(HttpHeaders.CONTENT_TYPE, inputStream.response().contentType());

        return ResponseEntity.ok()
                .headers(headers)
                .body(data);
    }
}
