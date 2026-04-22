package com.roomies.backend.services;

import com.roomies.backend.dto.UserCreateDto;
import com.roomies.backend.dto.UserDto;
import com.roomies.backend.exceptions.EmailAlreadyExistsException;
import com.roomies.backend.exceptions.ResourceNotFoundException;
import com.roomies.backend.exceptions.UserNotFoundException;
import com.roomies.backend.models.LifestyleProfile;
import com.roomies.backend.models.User;
import com.roomies.backend.repositories.UserRepository;
import com.roomies.backend.security.SecurityUtils;
import com.roomies.backend.models.Role;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public UserDto getMyProfile() {
        User me = securityUtils.getCurrentUser();
        return convertToDto(me);
    }

    public UserDto updateMyProfile(UserDto updateDto) {
        User me = securityUtils.getCurrentUser();
        return updateUserProfile(me.getId(), updateDto);
    }

    public UserDto updateMyPreferences(Map<String, Object> updates) {
        User me = securityUtils.getCurrentUser();
        return updateUserPreferences(me.getId(), updates);
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
        return convertToDto(savedUser);
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
            existingUser.setPetType(null);
        }

        User updatedUser = userRepository.save(existingUser);
        return convertToDto(updatedUser);
    }

    public UserDto updateUserPreferences(UUID id, Map<String, Object> updates) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));

        // Отримуємо поточний профіль звичок або створюємо новий
        LifestyleProfile profile = existingUser.getLifestyleProfile();
        if (profile == null) {
            profile = new LifestyleProfile();
        }

        // Оновлюємо поля
        if (updates.containsKey("sleep_schedule")) profile.setSleepSchedule((String) updates.get("sleep_schedule"));
        if (updates.containsKey("guests_frequency")) profile.setGuestsFrequency((String) updates.get("guests_frequency"));
        if (updates.containsKey("noise_tolerance")) profile.setNoiseTolerance((String) updates.get("noise_tolerance"));
        if (updates.containsKey("cleanliness_level")) profile.setCleanlinessLevel((String) updates.get("cleanliness_level"));
        if (updates.containsKey("dietary_preferences")) profile.setDietaryPreferences((String) updates.get("dietary_preferences"));
        if (updates.containsKey("isSmoker")) profile.setIsSmoker((Boolean) updates.get("isSmoker"));
        if (updates.containsKey("drinksAlcohol")) profile.setDrinksAlcohol((Boolean) updates.get("drinksAlcohol"));
        if (updates.containsKey("partyHabits")) profile.setPartyHabits((Boolean) updates.get("partyHabits"));

        // Зберігаємо профіль в користувача
        existingUser.setLifestyleProfile(profile);

        User updatedUser = userRepository.save(existingUser);
        return convertToDto(updatedUser);
    }

    public void deleteUser(UUID id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
        userRepository.delete(existingUser);
    }

    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setBirthDate(user.getBirthDate());
        dto.setGender(user.getGender());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setOccupation(user.getOccupation());
        dto.setBio(user.getBio());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setPhoneNumber(user.getPhoneNumber());

        if (user.getPetType() != null) {
            dto.setPetTypeId(user.getPetType().getId());
            dto.setPetTypeName(user.getPetType().getName());
        }

        // Просто передаємо об'єкт (Spring/Jackson сам перетворить його у правильний JSON для фронтенда)
        dto.setLifestyleProfile(user.getLifestyleProfile());

        return dto;
    }

    public void updatePassword(String email, String newRawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Користувача не знайдено"));

        user.setPassword(passwordEncoder.encode(newRawPassword));
        userRepository.save(user);
    }


    // Отримати всіх користувачів для адмінки
    public List<UserDto> getAllUsersForAdmin() {
        return userRepository.findAll().stream().map(user -> {
            UserDto dto = new UserDto();
            dto.setId(user.getId());
            dto.setEmail(user.getEmail());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setRole(user.getRole().name());
            return dto;
        }).collect(Collectors.toList());
    }

    // Видалити користувача (без зайвих питань)
    @Transactional
    public void deleteUserByAdmin(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Користувача не знайдено");
        }
        userRepository.deleteById(userId);
    }

    @Transactional
    public UserDto updateUserByAdmin(UUID userId, UserDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Користувача не знайдено"));

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        
        if (dto.getRole() != null) {
            user.setRole(Role.valueOf(dto.getRole())); // Оновлюємо роль
        }

        userRepository.save(user);

        // Повертаємо оновлений DTO
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());
        return dto;
    }

}