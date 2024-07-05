package com.sparta.greeypeople.repository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.sparta.greeypeople.config.TestConfig;
import com.sparta.greeypeople.like.entity.MenuLikes;
import com.sparta.greeypeople.like.repository.MenuLikesRepository;
import com.sparta.greeypeople.menu.entity.Menu;
import com.sparta.greeypeople.menu.repository.MenuRepository;
import com.sparta.greeypeople.store.entity.Store;
import com.sparta.greeypeople.store.repository.StoreRepository;
import com.sparta.greeypeople.user.entity.User;
import com.sparta.greeypeople.user.enumeration.UserAuth;
import com.sparta.greeypeople.user.enumeration.UserStatus;
import com.sparta.greeypeople.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;


@ActiveProfiles("test")
@Import(TestConfig.class)
@DataJpaTest
class MenuRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MenuLikesRepository menuLikesRepository;

    @Autowired
    private StoreRepository storeRepository;

    private User user;
    private Store store;

    @BeforeEach
    void setUp() {

        user = User.builder()
            .userId("judys26")
            .password("test1234!!")
            .email("judy1234@naver.com")
            .userName("션")
            .userAuth(UserAuth.USER)
            .userStatus(UserStatus.ACTIVE)
            .build();
        userRepository.save(user);

        store = new Store("Store 1", "Store 1 Intro");
        storeRepository.save(store);

        for (long i = 0; i < 10; i++) {
            Menu menu = Menu.builder()
                .menuName("Menu Name " + i)
                .price((int)i * 1000)
                .store(store)
                .user(user)
                .build();
            menuRepository.save(menu);

            if (i < 5) {
                MenuLikes menuLike = MenuLikes.builder()
                    .user(user)
                    .menu(menu)
                    .build();
                menuLikesRepository.save(menuLike);
            }
        }

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void testGetMenuLikesWithPageAndSortDesc() {
        List<Menu> menus = menuRepository.getLikesMenusWithPageAndSortDesc(user, 0, 5);
        assertThat(menus).hasSize(5);
        assertThat(menus.get(0).getMenuName()).isEqualTo("Menu Name 4");
        assertThat(menus.get(1).getMenuName()).isEqualTo("Menu Name 3");
        assertThat(menus.get(2).getMenuName()).isEqualTo("Menu Name 2");
        assertThat(menus.get(3).getMenuName()).isEqualTo("Menu Name 1");
        assertThat(menus.get(4).getMenuName()).isEqualTo("Menu Name 0");
    }
}
