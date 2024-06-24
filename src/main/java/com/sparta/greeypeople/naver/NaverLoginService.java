package com.sparta.greeypeople.naver;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.greeypeople.auth.util.JwtUtil;
import com.sparta.greeypeople.user.entity.User;
import com.sparta.greeypeople.user.enumeration.UserAuth;
import com.sparta.greeypeople.user.enumeration.UserStatus;
import com.sparta.greeypeople.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverLoginService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;

    public void loginWithNaver(String code, String state, HttpServletResponse response) throws Exception {

        String accessToken = getAccessToken(code, state);

        NaverUserInfoDto naverUserInfo = getNaverUserInfo(accessToken);

        User naverUser = registerNaverUserIfNeeded(naverUserInfo);

        String jwtAccessToken = jwtUtil.generateAccessToken(naverUser.getUserId(), naverUser.getUserName(), naverUser.getUserAuth());
        String jwtRefreshToken = jwtUtil.generateRefreshToken(naverUser.getUserId(), naverUser.getUserName(), naverUser.getUserAuth());

        ResponseCookie refreshTokenCookie = jwtUtil.generateRefreshTokenCookie(jwtRefreshToken);

        naverUser.updateRefreshToken(jwtRefreshToken);
        userRepository.save(naverUser);

        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwtAccessToken);
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
    }

    private String getAccessToken(String code, String state) throws Exception {
        URI uri = UriComponentsBuilder
            .fromUriString("https://nid.naver.com/oauth2.0/token")
            .queryParam("grant_type", "authorization_code")
            .queryParam("client_id", "8mWUC54LGL2OeCnJ7vtZ")
            .queryParam("client_secret", "c_kAL5shZd")
            .queryParam("code", code)
            .queryParam("state", state)
            .build()
            .toUri();

        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        JsonNode accessTokenNode = jsonNode.get("access_token");

        if (accessTokenNode == null) {
            log.error("Failed to retrieve access token: {}", response.getBody());
            throw new IllegalStateException("Failed to retrieve access token");
        }

        return accessTokenNode.asText();
    }

    private NaverUserInfoDto getNaverUserInfo(String accessToken) throws Exception {
        URI uri = UriComponentsBuilder
            .fromUriString("https://openapi.naver.com/v1/nid/me")
            .build()
            .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

        JsonNode jsonNode = objectMapper.readTree(response.getBody()).get("response");

        if (jsonNode == null || jsonNode.get("name") == null || jsonNode.get("email") == null) {
            throw new IllegalStateException("네이버 사용자 정보 응답에서 필수 필드가 누락");
        }

        String name = jsonNode.get("name").asText();
        String email = jsonNode.get("email").asText();

        return new NaverUserInfoDto(name, email);
    }

    private User registerNaverUserIfNeeded(NaverUserInfoDto naverUserInfo) {
        String email = naverUserInfo.getEmail();
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User naverUser;

        if (optionalUser.isPresent()) {
            naverUser = optionalUser.get();
        } else {
            String password = passwordEncoder.encode(UUID.randomUUID().toString());
            naverUser = new User(
                email,
                password,
                naverUserInfo.getName(),
                email,
                UserStatus.ACTIVE,
                UserAuth.USER,
                null,
                null
            );
            userRepository.save(naverUser);
        }

        return naverUser;
    }
}
