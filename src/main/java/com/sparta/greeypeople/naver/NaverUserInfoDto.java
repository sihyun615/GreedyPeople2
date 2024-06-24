package com.sparta.greeypeople.naver;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NaverUserInfoDto {
    private String name;
    private String email;

    public NaverUserInfoDto(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
