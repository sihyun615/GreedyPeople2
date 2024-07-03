package com.sparta.greeypeople.menu.repository;

import static com.sparta.greeypeople.like.entity.QMenuLikes.menuLikes;
import static com.sparta.greeypeople.menu.entity.QMenu.menu;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.greeypeople.menu.entity.Menu;
import com.sparta.greeypeople.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MenuRepositoryQueryImpl implements MenuRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Menu> getLikesMenusWithPageAndSortDesc(User user, long offset, int pageSize) {
        OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(Order.DESC, menuLikes.createdAt);

        return jpaQueryFactory.selectFrom(menu)
            .join(menuLikes).on(menuLikes.menu.eq(menu))
            .where(menuLikes.user.eq(user))
            .offset(offset)
            .limit(pageSize)
            .orderBy(orderSpecifier)
            .fetch();
    }
}