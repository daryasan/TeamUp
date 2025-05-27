package org.example.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "users-service-files", url = "http://users:8080")
public interface FileClient {

    @PostMapping("/photos")
    String uploadFile(MultipartFile file);

    @GetMapping("/photos")
    byte[] downloadFile(String path);
}
