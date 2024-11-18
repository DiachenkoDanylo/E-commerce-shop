package com.diachenko.backend.application.services;
/*  E-commerce-shop
    27.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.entities.Review;
import com.diachenko.backend.dtos.ReviewDto;
import com.diachenko.backend.dtos.ReviewPayload;
import org.springframework.data.domain.Page;

public interface ReviewService {

    ReviewDto getReviewById(Long id);

    Page<ReviewDto> getReviewListByItemId(Long id, int page, int size);

    ReviewDto addReview(ReviewPayload payload);

    ReviewDto updateReview(Long id, ReviewPayload reviewPayload);

    ReviewDto deleteReview(Long id);

    Page<ReviewDto> getAllReviews(int page, int size);

    boolean isReviewAvailible(ReviewPayload payload);

    Review convertToReview(ReviewDto reviewDto);

}
