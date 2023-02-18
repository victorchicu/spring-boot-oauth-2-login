package com.letmespringyou.springbootoauth2login.api.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/me")
public class OAuthUserController {
    @GetMapping
    public Map<String, Object> me(@AuthenticationPrincipal OAuth2User principal) throws Exception {
        return principal.getAttributes();
    }
}
