package com.roomies.backend.controllers;

import com.roomies.backend.dto.SocialLinkDto;
import com.roomies.backend.services.UserSocialLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UserSocialLinkController {

    @Autowired
    private UserSocialLinkService linkService;

    @GetMapping("/users/{userId}/social-links")
    public List<SocialLinkDto> getUserLinks(@PathVariable UUID userId) {
        return linkService.getUserLinks(userId);
    }

    @PostMapping("/users/{userId}/social-links")
    public SocialLinkDto addLink(@PathVariable UUID userId, @RequestBody SocialLinkDto newLinkDto) {
        return linkService.addLink(userId, newLinkDto);
    }

    @PutMapping("/social-links/{linkId}")
    public SocialLinkDto updateLink(@PathVariable UUID linkId, @RequestBody SocialLinkDto updatedDataDto) {
        return linkService.updateLink(linkId, updatedDataDto);
    }

    @DeleteMapping("/social-links/{linkId}")
    public void deleteLink(@PathVariable UUID linkId) {
        linkService.deleteLink(linkId);
    }
}