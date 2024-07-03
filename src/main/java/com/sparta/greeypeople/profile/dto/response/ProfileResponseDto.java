package com.sparta.greeypeople.profile.dto.response;

import com.sparta.greeypeople.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * ProfileResponseDto는 사용자 프로필 정보를 담고 있는 데이터 전송 객체
 */
@Getter
@AllArgsConstructor
public class ProfileResponseDto {

    private String userId;

    private String userName;

    private String intro;

    private long likedMenusCount;

    private long likedReviewsCount;

    public ProfileResponseDto(User user) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.intro = user.getIntro();
    }

    @Builder
    public ProfileResponseDto(User user, long likedMenusCount, long likedReviewsCount) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.intro = user.getIntro();
        this.likedMenusCount = likedMenusCount;
        this.likedReviewsCount = likedReviewsCount;
    }
}