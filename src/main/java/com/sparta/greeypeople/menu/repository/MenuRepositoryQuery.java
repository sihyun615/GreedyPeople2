package com.sparta.greeypeople.menu.repository;

import com.sparta.greeypeople.menu.entity.Menu;
import com.sparta.greeypeople.user.entity.User;
import java.util.List;

public interface MenuRepositoryQuery {
    List<Menu> getLikesMenusWithPageAndSortDesc(User user, long offset, int pageSize);
}