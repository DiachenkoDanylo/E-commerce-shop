package com.diachenko.backend.ui.controllers;
/*  E-commerce-shop
    27.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.services.ReviewServiceImpl;
import com.diachenko.backend.dtos.ReviewDto;
import com.diachenko.backend.dtos.ReviewPayload;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review/")
public class ReviewController {

    private final ReviewServiceImpl reviewService;

    @GetMapping("")
    public ResponseEntity<Page<ReviewDto>> getReviewById(@RequestParam(name = "page", defaultValue = "0") int page,
                                                         @RequestParam(name = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(reviewService.getAllReviews(page, size));
    }

    @PostMapping()
    public ResponseEntity<ReviewDto> createReview(@RequestBody @Valid ReviewPayload payload) {
        if (reviewService.isReviewAvailible(payload)) {
            ReviewDto createdReviewDto = reviewService.addReview(payload);
            return ResponseEntity.created(URI.create("/review/" + createdReviewDto.getId())).body(createdReviewDto);
        } else return null;
    }

    @GetMapping("{id}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ReviewDto> deleteItem(@PathVariable("id") Long id) {
        return ResponseEntity.ok(reviewService.deleteReview(id));
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ReviewDto> updateItem(@PathVariable("id") Long id, @Valid @RequestBody ReviewPayload reviewPayload) {
        return ResponseEntity.ok(reviewService.updateReview(id, reviewPayload));
    }

    @GetMapping("item/{id}")
    public ResponseEntity<Page<ReviewDto>> getReviewByItemId(@PathVariable("id") Long id,
                                                             @RequestParam(name = "page", defaultValue = "0") int page,
                                                             @RequestParam(name = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(reviewService.getReviewListByItemId(id, page, size));
    }
}
