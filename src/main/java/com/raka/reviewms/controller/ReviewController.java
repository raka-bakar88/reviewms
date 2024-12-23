package com.raka.reviewms.controller;


import com.raka.reviewms.model.Review;
import com.raka.reviewms.payload.ReviewDTO;
import com.raka.reviewms.payload.ReviewMessageProducer;
import com.raka.reviewms.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    @Autowired
    ReviewService reviewService;

    @Autowired
    private ReviewMessageProducer reviewMessageProducer;

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getAllReview(@RequestParam Long companyId) {
        List<ReviewDTO> reviewDTOS = reviewService.getAllReviews(companyId);
        return new ResponseEntity<>(reviewDTOS, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> addReview(@RequestParam Long companyId, @RequestBody ReviewDTO reviewDTO) {
        Boolean isSaved = reviewService.createReview(companyId, reviewDTO);
        if (isSaved) {
            reviewMessageProducer.sendMessage(reviewDTO);
            return new ResponseEntity<>("Review is added", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Review is failed to be added", HttpStatus.NOT_FOUND);

        }
    }

    @GetMapping(value = "/{reviewId}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable Long reviewId) {
        ReviewDTO savedReview = reviewService.getReviewById(reviewId);
        return new ResponseEntity<>(savedReview, HttpStatus.OK);
    }

    @PutMapping(value = "/{reviewId}")
    public ResponseEntity<ReviewDTO> updateReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewDTO reviewDTO) {
        ReviewDTO updatedReview = reviewService.updateReview(reviewId, reviewDTO);
        return new ResponseEntity<>(updatedReview, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId) {
        String status = reviewService.deleteReview(reviewId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @GetMapping("/averageRating")
    public Double getAverageReview(@RequestParam Long companyId){
        List<ReviewDTO> reviewDTOS = reviewService.getAllReviews(companyId);
        return reviewDTOS.stream().mapToDouble(ReviewDTO::getRating).average().orElse(0.0);
    }
}
