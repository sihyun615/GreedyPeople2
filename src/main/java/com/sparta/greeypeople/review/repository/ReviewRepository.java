package com.sparta.greeypeople.review.repository;

import com.sparta.greeypeople.review.entity.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = Review.class, idClass = Long.class)
public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryQuery {

    List<Review> findAllByStoreId(Long storeId);
}
