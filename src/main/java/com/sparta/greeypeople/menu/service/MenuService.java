package com.sparta.greeypeople.menu.service;

import com.sparta.greeypeople.exception.DataNotFoundException;
import com.sparta.greeypeople.menu.dto.response.AdminMenuResponseDto;
import com.sparta.greeypeople.menu.dto.response.MenuResponseDto;
import com.sparta.greeypeople.menu.entity.Menu;
import com.sparta.greeypeople.menu.repository.MenuRepository;
import com.sparta.greeypeople.store.entity.Store;
import com.sparta.greeypeople.store.repository.StoreRepository;
import com.sparta.greeypeople.user.entity.User;
import com.sparta.greeypeople.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;

    public List<AdminMenuResponseDto> getStoreMenu(Long storeId) {
        findStore(storeId);

        List<Menu> menus = menuRepository.findByStoreId(storeId);
        return menus.stream().map(AdminMenuResponseDto::new).collect(Collectors.toList());
    }

    public Store findStore(Long storeId) {
        return storeRepository.findById(storeId).orElseThrow(
            () -> new DataNotFoundException("조회된 가게의 정보가 없습니다.")
        );
    }

    @Transactional(readOnly = true)
    public List<MenuResponseDto> getLikesMenusWithPageAndSortDesc(User user, int page, int size) {
        User validatedUser = userRepository.findById(user.getId()).orElseThrow(() ->
            new DataNotFoundException("해당 사용자가 존재하지 않습니다."));

        PageRequest pageRequest = PageRequest.of(page, size);

        return menuRepository.getLikesMenusWithPageAndSortDesc(validatedUser, pageRequest.getOffset(), pageRequest.getPageSize())
            .stream()
            .map(m ->
                MenuResponseDto.builder()
                    .storeId(m.getStore().getId())
                    .menuName(m.getMenuName())
                    .price(m.getPrice())
                    .build())
            .collect(Collectors.toList());
    }

}
