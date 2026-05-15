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

    @Value("${supabase.bucket.apartments:apartments}")
    private String apartmentsBucket;

    public String uploadListingImage(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename().replaceAll("[^a-zA-Z0-9.\\-]", "_");
        // Змінюємо тут на apartmentsBucket
        String url = supabaseUrl + "/storage/v1/object/" + apartmentsBucket + "/" + fileName;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + supabaseKey);
        headers.setContentType(MediaType.valueOf(file.getContentType() != null ? file.getContentType() : "application/octet-stream"));

        HttpEntity<byte[]> requestEntity = new HttpEntity<>(file.getBytes(), headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            // І тут теж на apartmentsBucket
            return supabaseUrl + "/storage/v1/object/public/" + apartmentsBucket + "/" + fileName;
        } else {
            throw new RuntimeException("Помилка завантаження фото квартири");
        }
    }

    public java.util.List<String> uploadListingImages(java.util.List<MultipartFile> files) {
        java.util.List<String> uploadedUrls = new java.util.ArrayList<>();
        if (files == null || files.isEmpty()) {
            return uploadedUrls; // Якщо фото не передали, повертаємо порожній список
        }

        for (MultipartFile file : files) {
            try {
                // Викликаємо метод з Частини 2 для кожного файлу
                String url = uploadListingImage(file);
                uploadedUrls.add(url);
            } catch (IOException e) {
                // Логуємо помилку, але НЕ викидаємо Exception
                System.err.println("Не вдалося завантажити файл: " + file.getOriginalFilename());
            }
        }
        return uploadedUrls;
    }

    // Універсальний метод видалення
    private void deleteFile(String fileUrl, String bucketName) {
        if (fileUrl == null || fileUrl.isEmpty()) return;
        try {
            // Витягуємо ім'я файлу з URL (все, що після останнього слєшу)
            String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            String url = supabaseUrl + "/storage/v1/object/" + bucketName + "/" + fileName;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseKey);

            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();

            // Відправляємо DELETE запит до Supabase
            restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, String.class);
            System.out.println("Файл успішно видалено: " + fileName);
        } catch (Exception e) {
            System.err.println("Помилка видалення файлу з Supabase: " + e.getMessage());
        }
    }

    // Зручні обгортки для конкретних бакетів
    public void deleteAvatar(String fileUrl) {
        deleteFile(fileUrl, avatarBucket);
    }

    public void deleteListingImage(String fileUrl) {
        deleteFile(fileUrl, apartmentsBucket); // apartmentsBucket у тебе вже визначено через @Value
    }
}