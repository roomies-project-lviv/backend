package com.roomies.backend.dto;

import java.util.UUID;

public class SocialLinkDto {
    
    private UUID id;
    private String platformName;
    private String url;

    // Порожній конструктор
    public SocialLinkDto() {}

    // Конструктор для зручного створення
    public SocialLinkDto(UUID id, String platformName, String url) {
        this.id = id;
        this.platformName = platformName;
        this.url = url;
    }

    // --- Гетери та Сетери ---
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getPlatformName() { return platformName; }
    public void setPlatformName(String platformName) { this.platformName = platformName; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    
}