package com.sparta.greeypeople.menu.entity;

import com.sparta.greeypeople.common.TimeStamp;
import com.sparta.greeypeople.menu.dto.request.AdminMenuSaveRequestDto;
import com.sparta.greeypeople.menu.dto.request.AdminMenuUpdateRequestDto;
import com.sparta.greeypeople.order.entity.OrderMenu;
import com.sparta.greeypeople.store.entity.Store;
import com.sparta.greeypeople.user.entity.User;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "menu")
@NoArgsConstructor
public class Menu extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "menu_name", length = 50, nullable = false)
    private String menuName;

    @Column(name = "price", nullable = false)
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToMany(mappedBy = "menu")
    private List<OrderMenu> orderMenus = new ArrayList<>();

    @Column
    private Long menuLikes = 0L;

    public Menu(AdminMenuSaveRequestDto requestDto, User user, Store store) {
        this.menuName = requestDto.getMenuName();
        this.price = requestDto.getPrice();
        this.user = user;
        this.store = store;
    }

    @Builder
    public Menu(Long id, String menuName, Integer price, Store store, User user) {
        this.id = id;
        this.menuName = menuName;
        this.price = price;
        this.store = store;
        this.user = user;
    }

    public void update(AdminMenuUpdateRequestDto requestDto) {
        this.menuName = requestDto.getMenuName();
        this.price = requestDto.getPrice();
    }

    public void addLike() {
        this.menuLikes = menuLikes + 1L;
    }

    public void minusLike() {
        this.menuLikes = menuLikes - 1L;
    }

}
