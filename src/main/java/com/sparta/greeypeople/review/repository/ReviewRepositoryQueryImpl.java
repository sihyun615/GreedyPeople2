package com.sparta.greeypeople.review.repository;

import static com.sparta.greeypeople.like.entity.QReviewLikes.reviewLikes;
import static com.sparta.greeypeople.review.entity.QReview.review;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.greeypeople.review.entity.Review;
import com.sparta.greeypeople.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryQueryImpl implements ReviewRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Review> getLikesReviewsWithPageAndSortDesc(User user, long offset, int pageSize) {
        OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(Order.DESC, reviewLikes.createdAt);

        return jpaQueryFactory.selectFrom(review)
            .join(reviewLikes).on(reviewLikes.review.eq(review))
            .where(reviewLikes.user.eq(user))
            .offset(offset)
            .limit(pageSize)
            .orderBy(orderSpecifier)
            .fetch();
    }
}