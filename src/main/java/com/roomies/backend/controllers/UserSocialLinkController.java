package com.roomies.backend.controllers;

import com.roomies.backend.dto.SocialLinkDto;
import com.roomies.backend.services.UserSocialLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/social-links") // Змінили базовий шлях
public class UserSocialLinkController {

    @Autowired private UserSocialLinkService linkService;

    @GetMapping
    public List<SocialLinkDto> getMyLinks() {
        return linkService.getMyLinks(); // Більше не передаємо ID з URL
    }

    @PostMapping
    public SocialLinkDto addMyLink(@RequestBody SocialLinkDto newLinkDto) {
        return linkService.addMyLink(newLinkDto);
    }

    @PutMapping("/{linkId}")
    public SocialLinkDto updateMyLink(@PathVariable UUID linkId, @RequestBody SocialLinkDto updatedDataDto) {
        return linkService.updateMyLink(linkId, updatedDataDto);
    }

    @DeleteMapping("/{linkId}")
    public void deleteMyLink(@PathVariable UUID linkId) {
        linkService.deleteMyLink(linkId);
    }

    
}