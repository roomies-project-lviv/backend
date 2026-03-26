package com.roomies.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "user_social_links")
public class UserSocialLink {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Зв'язок "Багато-до-Одного": Багато посилань можуть належати одному користувачу
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // Це зовнішній ключ (Foreign Key)
    @JsonIgnore // Дуже важлива анотація! Вона запобігає "зацикленню" при видачі даних у браузер
    private User user;

    @Column(name = "platform_name", nullable = false)
    private String platformName;

    @Column(nullable = false)
    private String url;

    // --- Гетери та Сетери ---

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getPlatformName() { return platformName; }
    public void setPlatformName(String platformName) { this.platformName = platformName; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    
}