package com.diachenko.backend.core.services;
/*  E-commerce-shop
    27.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.application.services.ReviewService;
import com.diachenko.backend.core.entities.Review;
import com.diachenko.backend.dtos.ReviewDto;
import com.diachenko.backend.dtos.ReviewPayload;
import com.diachenko.backend.exceptions.AppException;
import com.diachenko.backend.infrastructure.mappers.ReviewMapper;
import com.diachenko.backend.infrastructure.repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final ItemServiceImpl itemService;
    private final  OrderServiceImpl orderService;

    @Override
    public ReviewDto getReviewById(Long id) {
        Optional<Review> optionalReview = reviewRepository.findById(id);
        if (optionalReview.isPresent()) {
            return reviewMapper.toReviewDto(optionalReview.get());
        } else {
            throw new AppException("Review with id:" + id + " not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<ReviewDto> getReviewListByItemId(Long id) {
        List<Review> reviewList = reviewRepository.findAllByItem_Id(id);
        if (!reviewList.isEmpty()) {
            return reviewMapper.toReviewDtoList(reviewList);
        } else {
            throw new AppException("Review for item with id:" + id + " not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ReviewDto addReview(ReviewPayload payload) {
        ReviewDto review = reviewMapper.toReviewDtoFromPayload(payload);
        Review savedReview = reviewRepository.save(convertToReview(review));
        return getReviewById(savedReview.getId());
    }

    @Override
    public boolean checkReview(ReviewPayload payload) {
        List<ReviewDto> list;
        try {
            list = getReviewListByItemId(payload.itemId());
        } catch (AppException e){
            return false;
        }
        ReviewDto review = reviewMapper.toReviewDtoFromPayload(payload);
        Optional<ReviewDto> optional = list.stream().filter(s-> s.equals(review)).findFirst();
        return optional.isPresent();
    }

    @Override
    public Review convertToReview(ReviewDto reviewDto) {
        return reviewMapper.toReview(reviewDto, itemService, orderService);
    }

    @Override
    public ReviewDto updateReview(Long id, ReviewPayload reviewPayload) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new AppException("Review not found", HttpStatus.NOT_FOUND));

        reviewMapper.updateReview(review, convertToReview(reviewMapper.toReviewDtoFromPayload(reviewPayload)));

        Review updateReview = reviewRepository.save(review);

        return reviewMapper.toReviewDto(updateReview);
    }

    @Override
    public ReviewDto deleteReview(Long id) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new AppException("Review not found", HttpStatus.NOT_FOUND));
        reviewRepository.deleteById(id);
        if (reviewRepository.findById(id).isPresent()) {
            System.out.println("problem");
        }
        return reviewMapper.toReviewDto(review);
    }

    @Override
    public List<ReviewDto> getAllReviews() {
        List<Review> reviewList = reviewRepository.findAll();
        if (!reviewList.isEmpty()) {
            return reviewMapper.toReviewDtoList(reviewList);
        } else {
            throw new AppException("Reviews not found", HttpStatus.NOT_FOUND);
        }
    }
}
