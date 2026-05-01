package com.roomies.backend.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class SupabaseStorageService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.bucket.avatars}")
    private String avatarBucket;

    public String uploadAvatar(MultipartFile file) throws IOException {
        // Генеруємо унікальне ім'я файлу (щоб не перезаписати існуючі)
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename().replaceAll("[^a-zA-Z0-9.\\-]", "_");
        String url = supabaseUrl + "/storage/v1/object/" + avatarBucket + "/" + fileName;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + supabaseKey);
        // Для Supabase API важливо передавати Content-Type файлу (наприклад image/jpeg)
        headers.setContentType(MediaType.valueOf(file.getContentType() != null ? file.getContentType() : "application/octet-stream"));

        HttpEntity<byte[]> requestEntity = new HttpEntity<>(file.getBytes(), headers);
        RestTemplate restTemplate = new RestTemplate();
        
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            // Повертаємо публічне посилання на завантажене фото
            return supabaseUrl + "/storage/v1/object/public/" + avatarBucket + "/" + fileName;
        } else {
            throw new RuntimeException("Помилка завантаження файлу в Supabase Storage");
        }
    }
    
}