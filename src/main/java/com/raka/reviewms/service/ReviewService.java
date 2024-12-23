package com.raka.reviewms.service;


import com.raka.reviewms.payload.ReviewDTO;

import java.util.List;

public interface ReviewService {
    List<ReviewDTO> getAllReviews(Long companyId);

    Boolean createReview(Long companyId, ReviewDTO reviewDTO);

    ReviewDTO getReviewById(Long reviewId);

    ReviewDTO updateReview(Long reviewId, ReviewDTO reviewDTO);

    String deleteReview(Long reviewId);
}
