package com.raka.reviewms.service;

import com.raka.reviewms.exception.ResourceNotFoundException;
import com.raka.reviewms.model.Review;
import com.raka.reviewms.payload.ReviewDTO;
import com.raka.reviewms.repository.ReviewRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<ReviewDTO> getAllReviews(Long companyId) {
        List<Review> reviews = reviewRepository.findReviewsByCompanyId(companyId);

        return reviews.stream()
                .map(review -> modelMapper.map(review, ReviewDTO.class)).toList();
    }

    @Override
    public Boolean createReview(Long companyId, ReviewDTO reviewDTO) {
        if (companyId != null && reviewDTO != null) {
            Review review = modelMapper.map(reviewDTO, Review.class);
            review.setCompanyId(companyId);
            reviewRepository.save(review);
            return true;
        } else {
            return false;
        }

    }

    @Override
    public ReviewDTO getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Review", "ReviewId", reviewId));

        return modelMapper.map(review, ReviewDTO.class);
    }

    @Override
    public ReviewDTO updateReview(Long reviewId, ReviewDTO reviewDTO) {
        Review review = reviewRepository
                .findById(reviewId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Review", "ReviewId", reviewId));
        review.setDescription(reviewDTO.getDescription());
        review.setTitle(reviewDTO.getTitle());
        review.setRating(reviewDTO.getRating());

        Review updatedReview = reviewRepository.save(review);
        return modelMapper.map(updatedReview, ReviewDTO.class);
    }

    @Override
    public String deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() ->
                new ResourceNotFoundException("Review", "Review ID", reviewId));
        reviewRepository.deleteById(reviewId);
        return "Delete successfull";
    }
}
