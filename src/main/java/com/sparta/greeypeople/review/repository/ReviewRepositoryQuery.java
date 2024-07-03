package com.sparta.greeypeople.review.repository;

import com.sparta.greeypeople.review.entity.Review;
import com.sparta.greeypeople.user.entity.User;
import java.util.List;

public interface ReviewRepositoryQuery {
    List<Review> getLikesReviewsWithPageAndSortDesc(User user, long offset, int pageSize);
}