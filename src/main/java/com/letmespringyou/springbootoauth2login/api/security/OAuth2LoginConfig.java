package com.letmespringyou.springbootoauth2login.api.security;

import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties({OAuth2ClientProperties.class})
public class OAuth2LoginConfig {
    private final OAuth2ClientProperties oauth2ClientProperties;

    public OAuth2LoginConfig(OAuth2ClientProperties oauth2ClientProperties) {
        this.oauth2ClientProperties = oauth2ClientProperties;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry.requestMatchers("/css/**", "/login/oauth2")
                                .permitAll()
                )
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry.anyRequest()
                                .authenticated()
                )
                .oauth2Login(httpSecurityOAuth2LoginConfigurer ->
                        httpSecurityOAuth2LoginConfigurer
                                .loginPage("/login/oauth2")
                                .defaultSuccessUrl("/")
                                .authorizedClientService(new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository()))
                );
        return http.build();
    }

    private ClientRegistrationRepository clientRegistrationRepository() {
        List<ClientRegistration> registrations = oauth2ClientProperties.getRegistration().entrySet().stream()
                .map(OAuth2LoginConfig::toClientRegistration)
                .toList();
        return new InMemoryClientRegistrationRepository(registrations);
    }

    private static ClientRegistration toClientRegistration(Map.Entry<String, OAuth2ClientProperties.Registration> entry) {
        return CommonOAuth2Provider.valueOf(entry.getKey()).getBuilder(entry.getKey())
                .clientId(entry.getValue().getClientId())
                .clientSecret(entry.getValue().getClientSecret())
                .build();
    }
}
