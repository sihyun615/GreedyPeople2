package com.sparta.greeypeople.naver;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.greeypeople.common.StatusCommonResponse;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class NaverLoginController {
    private final NaverLoginService naverLoginService;

    @GetMapping("/login/naver")
    public ResponseEntity<StatusCommonResponse> naverLogin(@RequestParam String code, @RequestParam String state, HttpServletResponse response) throws Exception {
        naverLoginService.loginWithNaver(code, state, response);

        StatusCommonResponse commonResponse = new StatusCommonResponse(200, "네이버 로그인 성공");
        return ResponseEntity.ok().body(commonResponse);
    }
}