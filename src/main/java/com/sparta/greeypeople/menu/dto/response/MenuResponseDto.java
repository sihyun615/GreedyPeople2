package com.sparta.greeypeople.menu.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuResponseDto {

    private Long storeId;
    private String menuName;
    private int price;

    @Builder
    public MenuResponseDto(Long storeId, String menuName, Integer price) {
        this.storeId = storeId;
        this.menuName = menuName;
        this.price = price;
    }

}