package com.roomies.backend.security;

import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpPasswordResetService {

    public static class ResetData {
        public String otpCode;
        public Instant expiryTime;
        public Instant lastSentTime;
    }

    private final Map<String, ResetData> cache = new ConcurrentHashMap<>();
    private final Random random = new Random();

    public String generateAndStoreOtp(String email) {
        if (cache.containsKey(email)) {
            Instant lastSent = cache.get(email).lastSentTime;
            if (Instant.now().isBefore(lastSent.plusSeconds(30))) {
                throw new RuntimeException("Зачекайте 30 секунд перед наступною відправкою коду");
            }
        }

        String otp = String.format("%06d", random.nextInt(999999));
        ResetData data = new ResetData();
        data.otpCode = otp;
        data.expiryTime = Instant.now().plusSeconds(300); // 5 хвилин
        data.lastSentTime = Instant.now();

        cache.put(email, data);
        return otp;
    }

    public void verifyOtp(String email, String otpCode) {
        ResetData data = cache.get(email);

        if (data == null) throw new RuntimeException("Запит на відновлення не знайдено або він протух");
        if (Instant.now().isAfter(data.expiryTime)) {
            cache.remove(email);
            throw new RuntimeException("Код прострочений. Запросіть новий.");
        }
        if (!data.otpCode.equals(otpCode)) {
            throw new RuntimeException("Неправильний код");
        }
        
        // Якщо код правильний, видаляємо з кешу
        cache.remove(email);
    }

    
}
