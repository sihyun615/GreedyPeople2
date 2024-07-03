package com.sparta.greeypeople.like.repository;

import static com.sparta.greeypeople.like.entity.QMenuLikes.menuLikes;

import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MenuLikesRepositoryQueryImpl implements MenuLikesRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public long countMenuLikesByUserId(Long userId) {
        Long count =  jpaQueryFactory
            .select(Wildcard.count)
            .from(menuLikes)
            .where(menuLikes.user.id.eq(userId))
            .fetchOne();  //레코드의 개수를 세는 쿼리의 경우 단일 숫자 값을 반환하므로 fetchOne 사용

        // fetchOne()은 쿼리 결과가 없을 경우 null을 반환하므로 Optional을 사용하여 안전하게 처리
        return Optional.ofNullable(count).orElse(0L);
    }
}
