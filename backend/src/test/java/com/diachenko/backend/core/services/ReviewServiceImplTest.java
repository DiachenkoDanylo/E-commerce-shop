package com.diachenko.backend.core.services;
/*  E-commerce-shop
    28.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.entities.*;
import com.diachenko.backend.dtos.ReviewDto;
import com.diachenko.backend.dtos.ReviewPayload;
import com.diachenko.backend.exceptions.AppException;
import com.diachenko.backend.infrastructure.mappers.ReviewMapper;
import com.diachenko.backend.infrastructure.repositories.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReviewServiceImplTest {

    private static final int PAGE = 0;
    private static final int SIZE = 10;
    private static final Pageable PAGEABLE = PageRequest.of(PAGE, SIZE);

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private ReviewMapper reviewMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    public List<Item> mockItemList() {
        Category category = new Category(1L, "testing category name", "testing category description");
        Item item1 = new Item(1L, "testItem1", category, "testDesc1", 100, null, 10);
        Item item2 = new Item(2L, "testItem2", category, "testDesc2", 200, null, 20);
        return List.of(item1, item2);
    }

    public Order mockOrder() {
        User userTest = new User(1L, "testname", "testlastname", "testlogin", "testpass", "testemail", "TEST_AUTHORITY");
        return new Order(1L, Collections.emptyList(), userTest, OrderStatus.CART);
    }

    @Test
    void testGetReviewById() {
        LocalDateTime localDateTime = LocalDateTime.of(2024, 10, 28, 17, 15);
        long id = 1;
        Review review = new Review(id, mockItemList().get(0), 5, "TEXT FROM REVIEW", localDateTime, mockOrder());
        ReviewDto reviewDto = new ReviewDto(id, 1L, 5, "TEXT FROM REVIEW", localDateTime, 1L);
        when(reviewRepository.findById(id)).thenReturn(Optional.of(review));
        when(reviewMapper.toReviewDto(review)).thenReturn(reviewDto);

        assertEquals(reviewService.getReviewById(id), reviewDto);
    }

    @Test
    void testGetReviewById_AppException() {
        long id = 1;
        when(reviewRepository.findById(id)).thenThrow(new AppException("Review with id:" + id + " not found", HttpStatus.NOT_FOUND));
        AppException thrown = assertThrows(AppException.class, () -> {
            reviewService.getReviewById(id);
        });
        assertEquals("Review with id:" + id + " not found", thrown.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
    }

    @Test
    void testGetReviewListByItemId() {

        LocalDateTime localDateTime = LocalDateTime.of(2024, 10, 28, 17, 15);
        long id = 1;
        Review review = new Review(id, mockItemList().get(0), 5, "TEXT FROM REVIEW", localDateTime, mockOrder());
        ReviewDto reviewDto = new ReviewDto(id, 1L, 5, "TEXT FROM REVIEW", localDateTime, 1L);

        Page<Review> page = new PageImpl<>(List.of(review), PAGEABLE, 1);
        Page<ReviewDto> pageDto = new PageImpl<>(List.of(reviewDto), PAGEABLE, 1);

        when(reviewRepository.findAllByItem_Id(id, PAGEABLE)).thenReturn(page);
        when(reviewMapper.toReviewDtoList(List.of(review))).thenReturn(List.of(reviewDto));

        assertEquals(reviewService.getReviewListByItemId(id, PAGE, SIZE), pageDto);
    }

    @Test
    void testGetReviewListByItemId_AppException() {
        long id = 1;
        when(reviewRepository.findAllByItem_Id(id, PAGEABLE)).thenThrow(new AppException("Review for item with id:" + id + " not found", HttpStatus.NOT_FOUND));
        AppException thrown = assertThrows(AppException.class, () -> {
            reviewService.getReviewListByItemId(id, PAGE, SIZE);
        });
        assertEquals("Review for item with id:" + id + " not found", thrown.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
    }

    @Test
    void testAddReview() {
        LocalDateTime localDateTime = LocalDateTime.of(2024, 10, 28, 17, 15);
        long id = 1;
        Review review = new Review(id, mockItemList().get(0), 5, "TEXT FROM REVIEW", localDateTime, mockOrder());
        ReviewDto reviewDto = new ReviewDto(id, 1L, 5, "TEXT FROM REVIEW", localDateTime, 1L);

        ReviewPayload reviewPayload = new ReviewPayload(1L, 5, "TEXT FROM REVIEW", 1L);
        when(reviewMapper.toReviewDtoFromPayload(reviewPayload)).thenReturn(reviewDto);
        when(reviewService.convertToReview(reviewDto)).thenReturn(review);
        when(reviewRepository.save(review)).thenReturn(review);
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(review));
        when(reviewService.getReviewById(review.getId())).thenReturn(reviewDto);

        assertEquals(reviewService.addReview(reviewPayload), reviewDto);

        verify(reviewRepository, times(1)).save(any());
    }

    @Test
    void testIsReview_NotDuplicate_True() {
        LocalDateTime localDateTime = LocalDateTime.of(2024, 10, 28, 17, 15);
        long id = 1;

        ReviewPayload reviewPayload = new ReviewPayload(1L, 5, "TEXT FROM REVIEW", 1L);
        Review review = new Review(id, mockItemList().get(0), 5, "TEXT FROM REVIEW", localDateTime, mockOrder());
        ReviewDto reviewDto = new ReviewDto(id, 1L, 5, "TEXT FROM REVIEW", localDateTime, 1L);
        Page<Review> page = new PageImpl<>(List.of(review), PageRequest.of(0, 1000), 1);

        when(reviewRepository.findAllByItem_Id(id, PageRequest.of(0, 1000))).thenReturn(page);
        when(reviewMapper.toReviewDtoFromPayload(reviewPayload)).thenReturn(reviewDto);
        when(reviewMapper.toReviewDtoList(page.getContent())).thenReturn(List.of(reviewDto));

//        reviewMapper.toReviewDtoFromPayload(payload);
//        List<ReviewDto> reviewDtoList = reviewMapper.toReviewDtoList(list);

        assertTrue(reviewService.isReviewAvailible(reviewPayload));
    }

    @Test
    void testIsReview_NotDuplicate_False() {
        LocalDateTime localDateTime = LocalDateTime.of(2024, 10, 28, 17, 15);
        long id = 1;

        ReviewPayload reviewPayload = new ReviewPayload(1L, 5, "TEXT FROM REVIEW", 1L);
        Review review = new Review(id, mockItemList().get(0), 5, "TEXT FROM REVIEW", localDateTime, mockOrder());
        ReviewDto reviewDto2 = new ReviewDto(id, 2L, 5, "TEXT FROM REVIEW", localDateTime, 1L);
        Page<Review> page = new PageImpl<>(List.of(review), PageRequest.of(0, 1000), 1);

        when(reviewRepository.findAllByItem_Id(id, PageRequest.of(0, 1000))).thenReturn(page);
        when(reviewMapper.toReviewDtoFromPayload(reviewPayload)).thenReturn(reviewDto2);

        assertFalse(reviewService.isReviewAvailible(reviewPayload));
    }


    @Test
    void testUpdateReview() {
        LocalDateTime localDateTime = LocalDateTime.of(2024, 10, 28, 17, 15);
        long id = 1;
        ReviewPayload reviewPayload = new ReviewPayload(1L, 5, "TEXT FROM REVIEW", 1L);
        Review review = new Review(id, mockItemList().get(0), 5, "TEXT FROM REVIEW", localDateTime, mockOrder());
        Review review2 = new Review(id, mockItemList().get(1), 4, "TEXT FROM REVIEW 2 ", localDateTime, mockOrder());
        ReviewDto reviewDto = new ReviewDto(id, 1L, 5, "TEXT FROM REVIEW", localDateTime, 1L);
        ReviewDto reviewDto2 = new ReviewDto(id, mockItemList().get(1).getId(), 4, "TEXT FROM REVIEW 2 ", localDateTime, mockOrder().getId());

        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(review2));
        when(reviewMapper.toReview(any(ReviewDto.class), any(ItemServiceImpl.class), any(OrderServiceImpl.class)))
                .thenReturn(review);
        when(reviewMapper.toReviewDtoFromPayload(reviewPayload)).thenReturn(reviewDto);
        when(reviewRepository.save(review2)).thenReturn(review2);
        when(reviewMapper.toReviewDto(review2)).thenReturn(reviewDto2);

        assertEquals(reviewService.updateReview(id, reviewPayload), reviewDto2);
    }

    @Test
    void testDeleteReview() {
        LocalDateTime localDateTime = LocalDateTime.of(2024, 10, 28, 17, 15);
        long id = 1;
        Review review = new Review(id, mockItemList().get(0), 5, "TEXT FROM REVIEW", localDateTime, mockOrder());
        ReviewDto reviewDto = new ReviewDto(id, 1L, 5, "TEXT FROM REVIEW", localDateTime, 1L);

        when(reviewRepository.findById(id)).thenReturn(Optional.of(review));
        when(reviewMapper.toReviewDto(review)).thenReturn(reviewDto);

        assertEquals(reviewService.deleteReview(id), reviewDto);
        verify(reviewRepository, times(2)).findById(id);
        verify(reviewRepository).deleteById(id);
        verify(reviewMapper).toReviewDto(review);
    }

    @Test
    void testGetAllReviews() {
        LocalDateTime localDateTime = LocalDateTime.of(2024, 10, 28, 17, 15);
        long id = 1;

        Review review = new Review(id, mockItemList().get(0), 5, "TEXT FROM REVIEW", localDateTime, mockOrder());
        ReviewDto reviewDto = new ReviewDto(id, 1L, 5, "TEXT FROM REVIEW", localDateTime, 1L);
        when(reviewRepository.findAll(PAGEABLE)).thenReturn(new PageImpl<>(List.of(review), PAGEABLE, 1));
        when(reviewMapper.toReviewDtoList(List.of(review))).thenReturn(List.of(reviewDto));

        assertEquals(reviewService.getAllReviews(PAGE, SIZE).getContent(), List.of(reviewDto));
    }

    @Test
    void testGetAllReviews_AppException() {
        when(reviewRepository.findAll(PAGEABLE)).thenReturn(Page.empty());

        AppException thrown = assertThrows(AppException.class, () -> {
            reviewService.getAllReviews(PAGE, SIZE);
        });

        assertEquals("Reviews not found", thrown.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
    }
}
