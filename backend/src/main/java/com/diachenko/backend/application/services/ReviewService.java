package com.diachenko.backend.application.services;
/*  E-commerce-shop
    27.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.entities.Review;
import com.diachenko.backend.dtos.ReviewDto;
import com.diachenko.backend.dtos.ReviewPayload;

import java.util.List;

public interface ReviewService {

    ReviewDto getReviewById(Long id);

    List<ReviewDto> getReviewListByItemId(Long id);

    ReviewDto addReview(ReviewPayload payload);

    ReviewDto updateReview(Long id, ReviewPayload reviewPayload);

    ReviewDto deleteReview(Long id);

    public List<ReviewDto> getAllReviews();

    boolean checkReview(ReviewPayload payload);

    Review convertToReview(ReviewDto reviewDto);

}
