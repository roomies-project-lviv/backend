package com.roomies.backend.services;

import com.roomies.backend.dto.UserCreateDto;
import com.roomies.backend.dto.UserDto;
import com.roomies.backend.exceptions.EmailAlreadyExistsException;
import com.roomies.backend.exceptions.ResourceNotFoundException;
import com.roomies.backend.exceptions.UserNotFoundException;
import com.roomies.backend.models.User;
import com.roomies.backend.repositories.UserRepository;
import com.roomies.backend.security.SecurityUtils;

import org.springframework.security.crypto.password.PasswordEncoder;
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
    private PasswordEncoder passwordEncoder;
    @Autowired
    private com.roomies.backend.repositories.PetTypeRepository petTypeRepository;

    @Autowired
    private SecurityUtils securityUtils;

    // Отримати СВІЙ профіль
    public UserDto getMyProfile() {
        User me = securityUtils.getCurrentUser();
        return convertToDto(me);
    }

    // Оновити СВІЙ профіль
    public UserDto updateMyProfile(UserDto updateDto) {
        User me = securityUtils.getCurrentUser();
        return updateUserProfile(me.getId(), updateDto); // Викликаємо існуючий метод
    }

    // Оновити СВОЇ налаштування
    public UserDto updateMyPreferences(Map<String, Object> updates) {
        User me = securityUtils.getCurrentUser();
        return updateUserPreferences(me.getId(), updates); // Викликаємо існуючий метод
    }
    
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
        if (userRepository.existsByEmail(createDto.getEmail())) {
            throw new EmailAlreadyExistsException("Користувач з таким email вже існує");
        }

        User user = new User();
        user.setPassword(passwordEncoder.encode(createDto.getPassword()));
        user.setPhoneNumber(createDto.getPhoneNumber());
        user.setEmail(createDto.getEmail());
        user.setFirstName(createDto.getFirstName());
        user.setLastName(createDto.getLastName());
        user.setBirthDate(createDto.getBirthDate());
        user.setGender(createDto.getGender());

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
        existingUser.setPhoneNumber(updateDto.getPhoneNumber());

        if (updateDto.getPetTypeId() != null) {
            com.roomies.backend.models.PetType petType = petTypeRepository.findById(updateDto.getPetTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Pet Type not found"));
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

        // --- КОРЕКТНЕ ПАКУВАННЯ ЗВИЧОК (lifestyle_flags) У JSON/MAP ---
        // 1. Беремо поточний словник користувача, щоб не затерти існуючі дані
        Map<String, Object> flags = existingUser.getLifestyleFlags();
        if (flags == null) {
            flags = new java.util.HashMap<>();
        }

        // 2. Якщо з фронтенду прийшли нові звички — точково додаємо їх у словник
        if (updates.containsKey("isSmoker")) {
            flags.put("isSmoker", updates.get("isSmoker"));
        }
        if (updates.containsKey("drinksAlcohol")) {
            flags.put("drinksAlcohol", updates.get("drinksAlcohol"));
        }
        if (updates.containsKey("partyHabits")) {
            flags.put("partyHabits", updates.get("partyHabits"));
        }

        // 3. Кладемо словник назад у правильну змінну (existingUser)
        existingUser.setLifestyleFlags(flags);

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
        dto.setPhoneNumber(user.getPhoneNumber());

        // --- ДОДАЄМО ОБРОБКУ ТВАРИНИ ---
        if (user.getPetType() != null) {
            dto.setPetTypeId(user.getPetType().getId());
            dto.setPetTypeName(user.getPetType().getName());
        }

        // --- РОЗШИФРОВУЄМО lifestyle_flags (З JSON у змінні DTO) ---
        Map<String, Object> flags = user.getLifestyleFlags();

        if (flags != null) {
            dto.setIsSmoker(Boolean.TRUE.equals(flags.get("isSmoker")));
            dto.setDrinksAlcohol(Boolean.TRUE.equals(flags.get("drinksAlcohol")));
            dto.setPartyHabits(Boolean.TRUE.equals(flags.get("partyHabits")));
        } else {
            // Безпечні значення за замовчуванням
            dto.setIsSmoker(false);
            dto.setDrinksAlcohol(false);
            dto.setPartyHabits(false);
        }
        
        return dto;
    }

    public void updatePassword(String email, String newRawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Користувача не знайдено"));
        
        // Хешуємо новий пароль і зберігаємо його в базі
        user.setPassword(passwordEncoder.encode(newRawPassword));
        userRepository.save(user);
    }

    
}