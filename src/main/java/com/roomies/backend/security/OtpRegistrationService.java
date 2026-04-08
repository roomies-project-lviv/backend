package com.roomies.backend.security;

import com.roomies.backend.dto.UserCreateDto;
import com.roomies.backend.exceptions.InvalidOtpException;
import com.roomies.backend.exceptions.RateLimitException;

import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpRegistrationService {

    // Клас для зберігання тимчасових даних
    public static class OtpData {
        public UserCreateDto userDto;
        public String otpCode;
        public Instant expiryTime; // Коли код протухне (напр. через 5 хв)
        public Instant lastSentTime; // Для таймера 30 секунд
    }

    // Тимчасове сховище (Email -> Дані)
    private final Map<String, OtpData> cache = new ConcurrentHashMap<>();
    private final Random random = new Random();

    public String generateAndStoreOtp(UserCreateDto dto) {
        String email = dto.getEmail();
        
        // Перевірка кулдауну 30 секунд (якщо запит повторний)
        if (cache.containsKey(email)) {
            Instant lastSent = cache.get(email).lastSentTime;
            if (Instant.now().isBefore(lastSent.plusSeconds(30))) {
                throw new RateLimitException("Зачекайте 30 секунд перед наступною відправкою коду");
            }
        }

        // Генерація 6-значного коду
        String otp = String.format("%06d", random.nextInt(999999));

        OtpData data = new OtpData();
        data.userDto = dto; // Зберігаємо дані, які ввів користувач
        data.otpCode = otp;
        data.expiryTime = Instant.now().plusSeconds(300); // 5 хвилин
        data.lastSentTime = Instant.now();

        cache.put(email, data);
        return otp;
    }

    public UserCreateDto verifyOtp(String email, String otpCode) {
        OtpData data = cache.get(email);

        if (data == null) throw new InvalidOtpException("Сесія не знайдена або протухла");
        if (Instant.now().isAfter(data.expiryTime)) {
            cache.remove(email);
            throw new InvalidOtpException("Код прострочений. Запросіть новий.");
        }
        if (!data.otpCode.equals(otpCode)) {
            throw new InvalidOtpException("Неправильний код підтвердження");
        }

        // Код правильний! Віддаємо DTO і видаляємо з кешу
        UserCreateDto validDto = data.userDto;
        cache.remove(email); 
        return validDto;
    }

    
}