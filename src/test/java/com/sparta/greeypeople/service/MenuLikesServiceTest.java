package com.sparta.greeypeople.service;

import com.sparta.greeypeople.exception.DataNotFoundException;
import com.sparta.greeypeople.exception.ForbiddenException;
import com.sparta.greeypeople.exception.ViolatedException;
import com.sparta.greeypeople.like.entity.MenuLikes;
import com.sparta.greeypeople.like.repository.MenuLikesRepository;
import com.sparta.greeypeople.like.service.MenuLikesService;
import com.sparta.greeypeople.menu.entity.Menu;
import com.sparta.greeypeople.menu.repository.MenuRepository;
import com.sparta.greeypeople.store.entity.Store;
import com.sparta.greeypeople.store.repository.StoreRepository;
import com.sparta.greeypeople.user.entity.User;
import com.sparta.greeypeople.user.enumeration.UserAuth;
import com.sparta.greeypeople.user.enumeration.UserStatus;
import com.sparta.greeypeople.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuLikesServiceTest {

    @Mock
    private MenuLikesRepository menuLikesRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private MenuLikesService menuLikesService;

    private User user;
    private Store store;
    private Menu menu;
    private MenuLikes menuLikes;

    @BeforeEach
    void setUp() {
        user = new User("judys26", "test1234!!", "션", "judy1234@naver.com", UserStatus.ACTIVE, UserAuth.USER, null);
        user.setId(1L);

        User adminUser = new User("admin1234", "password123!", "관리자", "admin1234@naver.com", UserStatus.ACTIVE, UserAuth.ADMIN, null);
        adminUser.setId(2L);

        store = new Store(1L, "중국집", "짜장면 맛있어요");

        menu = new Menu(1L, "짜장면", 8000, store, adminUser);

        menuLikes = new MenuLikes(1L, user, menu);
    }

    @Test
    void addMenuLike_Success() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(storeRepository.findById(store.getId())).thenReturn(Optional.of(store));
        when(menuRepository.findById(menu.getId())).thenReturn(Optional.of(menu));
        when(menuLikesRepository.findByUserAndMenu(user, menu)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> menuLikesService.addMenuLike(store.getId(), menu.getId(), user));

        assertEquals(1, menu.getMenuLikes());
    }

    @Test
    void addMenuLike_AlreadyLiked() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(storeRepository.findById(store.getId())).thenReturn(Optional.of(store));
        when(menuRepository.findById(menu.getId())).thenReturn(Optional.of(menu));
        when(menuLikesRepository.findByUserAndMenu(user, menu)).thenReturn(Optional.of(menuLikes));

        assertThrows(ViolatedException.class, () -> menuLikesService.addMenuLike(store.getId(), menu.getId(), user));
    }

    @Test
    void addMenuLike_ValidateUserException() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> menuLikesService.addMenuLike(store.getId(), menu.getId(), user));
    }

    @Test
    void removeMenuLike_Success() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(menuLikesRepository.findById(menuLikes.getId())).thenReturn(Optional.of(menuLikes));
        when(storeRepository.findById(store.getId())).thenReturn(Optional.of(store));
        when(menuRepository.findById(menu.getId())).thenReturn(Optional.of(menu));

        assertDoesNotThrow(() -> menuLikesService.removeMenuLike(store.getId(), menuLikes.getId(), user));

        assertEquals(-1, menu.getMenuLikes());
    }

    @Test
    void removeMenuLike_OtherUserLike() {
        User otherUser = new User("hello123", "password123!", "userName", "email@example.com", UserStatus.ACTIVE, UserAuth.USER, null);
        otherUser.setId(3L);

        when(menuLikesRepository.findById(menuLikes.getId())).thenReturn(Optional.of(menuLikes));
        when(storeRepository.findById(store.getId())).thenReturn(Optional.of(store));
        when(menuRepository.findById(menu.getId())).thenReturn(Optional.of(menu));

        when(userRepository.findById(otherUser.getId())).thenReturn(Optional.of(otherUser));

        assertThrows(ForbiddenException.class, () -> menuLikesService.removeMenuLike(store.getId(), menuLikes.getId(), otherUser));
    }

    @Test
    void removeMenuLike_ValidateUserException() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> menuLikesService.removeMenuLike(store.getId(), menuLikes.getId(), user));
    }
}
