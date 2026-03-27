package com.roomies.backend.services;

import com.roomies.backend.dto.UserCreateDto;
import com.roomies.backend.dto.UserDto;
import com.roomies.backend.exceptions.UserNotFoundException;
import com.roomies.backend.models.User;
import com.roomies.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private com.roomies.backend.repositories.PetTypeRepository petTypeRepository;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
        return convertToDto(user);
    }

    public UserDto createUser(UserCreateDto createDto) {
        User user = new User();
        user.setEmail(createDto.getEmail());
        user.setPassword(createDto.getPassword()); // У майбутньому ми додамо сюди хешування BCrypt!
        user.setFirstName(createDto.getFirstName());
        user.setLastName(createDto.getLastName());
        user.setBirthDate(createDto.getBirthDate());
        user.setGender(createDto.getGender());

        if (createDto.getPetTypeId() != null) {
            com.roomies.backend.models.PetType petType = petTypeRepository.findById(createDto.getPetTypeId())
                    .orElseThrow(() -> new RuntimeException("Pet Type not found"));
            user.setPetType(petType);
        }

        User savedUser = userRepository.save(user);
        return convertToDto(savedUser); // Повертаємо безпечний DTO без пароля
    }

    public UserDto updateUserProfile(UUID id, UserDto updateDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));

        existingUser.setFirstName(updateDto.getFirstName());
        existingUser.setLastName(updateDto.getLastName());
        existingUser.setEmail(updateDto.getEmail());
        existingUser.setBirthDate(updateDto.getBirthDate());
        existingUser.setGender(updateDto.getGender());
        existingUser.setAvatarUrl(updateDto.getAvatarUrl());
        existingUser.setOccupation(updateDto.getOccupation());
        existingUser.setBio(updateDto.getBio());

        if (updateDto.getPetTypeId() != null) {
            com.roomies.backend.models.PetType petType = petTypeRepository.findById(updateDto.getPetTypeId())
                    .orElseThrow(() -> new RuntimeException("Pet Type not found"));
            existingUser.setPetType(petType);
        } else {
            existingUser.setPetType(null); // Якщо користувач видалив тварину з профілю
        }

        User updatedUser = userRepository.save(existingUser);
        return convertToDto(updatedUser);
    }

    public UserDto updateUserPreferences(UUID id, Map<String, Object> updates) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));

        if (updates.containsKey("sleep_schedule")) existingUser.setSleepSchedule((String) updates.get("sleep_schedule"));
        if (updates.containsKey("guests_frequency")) existingUser.setGuestsFrequency((String) updates.get("guests_frequency"));
        if (updates.containsKey("noise_tolerance")) existingUser.setNoiseTolerance((String) updates.get("noise_tolerance"));
        if (updates.containsKey("cleanliness_level")) existingUser.setCleanlinessLevel((String) updates.get("cleanliness_level"));
        if (updates.containsKey("dietary_preferences")) existingUser.setDietaryPreferences((String) updates.get("dietary_preferences"));
        //if (updates.containsKey("lifestyle_flags")) existingUser.setLifestyleFlags((String) updates.get("lifestyle_flags"));


        // --- ЗАПАКОВУЄМО lifestyle_flags НАЗАД У БАЗУ ---
        // Беремо поточні прапорці або створюємо масив з 8 нулів, якщо їх ще не було
        String currentFlags = existingUser.getLifestyleFlags();
        if (currentFlags == null || currentFlags.length() < 8) {
            currentFlags = "00000000";
        }
        
        // Перетворюємо рядок на масив символів, щоб зручно міняти окремі біти
        char[] flagsArray = currentFlags.toCharArray();

        if (updates.containsKey("isSmoker")) {
            boolean isSmoker = (Boolean) updates.get("isSmoker");
            flagsArray[0] = isSmoker ? '1' : '0';
        }
        
        if (updates.containsKey("drinksAlcohol")) {
            boolean drinksAlcohol = (Boolean) updates.get("drinksAlcohol");
            flagsArray[1] = drinksAlcohol ? '1' : '0';
        }

        if (updates.containsKey("partyHabits")) {
            boolean partyHabits = (Boolean) updates.get("partyHabits");
            flagsArray[2] = partyHabits ? '1' : '0';
        }

        // Зберігаємо змінений масив назад як рядок
        existingUser.setLifestyleFlags(new String(flagsArray));

        User updatedUser = userRepository.save(existingUser);
        return convertToDto(updatedUser);
    }

    public void deleteUser(UUID id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
        userRepository.delete(existingUser);
    }

    // --- ПЕРЕКЛАДАЧ (Mapper) ---
    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setBirthDate(user.getBirthDate());
        dto.setGender(user.getGender());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setSleepSchedule(user.getSleepSchedule());
        dto.setOccupation(user.getOccupation());
        dto.setGuestsFrequency(user.getGuestsFrequency());
        dto.setNoiseTolerance(user.getNoiseTolerance());
        dto.setCleanlinessLevel(user.getCleanlinessLevel());
        dto.setDietaryPreferences(user.getDietaryPreferences());
        dto.setBio(user.getBio());
        dto.setCreatedAt(user.getCreatedAt());

        // --- ДОДАЄМО ОБРОБКУ ТВАРИНИ ---
        if (user.getPetType() != null) {
            dto.setPetTypeId(user.getPetType().getId());
            dto.setPetTypeName(user.getPetType().getName());
        }
        
        // --- РОЗШИФРОВУЄМО lifestyle_flags ---
        String flags = user.getLifestyleFlags();
        
        // Перевіряємо, чи рядок не порожній і має хоча б 2 символи
        if (flags != null && flags.length() >= 2) {
            // '1' на нульовій позиції = курить
            dto.setIsSmoker(flags.charAt(0) == '1'); 
            // '1' на першій позиції = вживає алкоголь
            dto.setDrinksAlcohol(flags.charAt(1) == '1');
            // '1' на другій позиції = влаштує вечірки
            dto.setPartyHabits(flags.charAt(2) == '1');
        } else {
            // Безпечні значення за замовчуванням
            dto.setIsSmoker(false);
            dto.setDrinksAlcohol(false);
            dto.setPartyHabits(false);
        }
        
        return dto;
    }


}