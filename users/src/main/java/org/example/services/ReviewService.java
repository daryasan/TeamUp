package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.CreateReviewDto;
import org.example.dto.UserDetailsDto;
import org.example.exceptions.AuthException;
import org.example.exceptions.DataException;
import org.example.exceptions.ReviewException;
import org.example.models.Review;
import org.example.repositories.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final TokenService tokenService;

    public Review addReview(String token, Long receiverId, CreateReviewDto createReviewDto) throws DataException, AuthException, ReviewException {

        UserDetailsDto user = tokenService.getDetails(token);

        if (createReviewDto.getRate() < 1 || createReviewDto.getRate() > 5)
            throw new DataException("Rate can be 1 to 5");

        if (receiverId == user.getId()) throw new ReviewException("Can't send review to yourself");
        Review review = new Review();
        review.setSenderId(user.getId());
        review.setReceiverId(receiverId);
        review.setReviewText(createReviewDto.getText());
        review.setRate(createReviewDto.getRate());

        reviewRepository.save(review);
        return review;

    }


    public Review findReviewById(Long id) throws ReviewException {
        Optional<Review> review = reviewRepository.findById(id);
        if (review.isEmpty()) throw new ReviewException("No review with such id");
        return review.get();
    }


    public Review editReview(Long reviewId, CreateReviewDto createReviewDto) throws DataException, ReviewException {

        if (createReviewDto.getRate() < 1 || createReviewDto.getRate() > 5)
            throw new DataException("Rate can be 1 to 5");

        Review review = findReviewById(reviewId);
        review.setReviewText(createReviewDto.getText());
        review.setRate(createReviewDto.getRate());
        reviewRepository.save(review);
        return review;
    }

    public boolean deleteReview(String token, Long reviewId) throws AuthException {

        UserDetailsDto user = tokenService.getDetails(token);
        Optional<Review> review = reviewRepository.findById(reviewId);

        if (review.isEmpty()) return false;
        if (review.get().getSenderId() != user.getId()) return false;

        reviewRepository.delete(review.get());
        return true;

    }


    public List<Review> getUserReviews(Long userId) {

        List<Review> reviews = new ArrayList<>();

        for (Review r : reviewRepository.findAll()) {
            if (Objects.equals(r.getReceiverId(), userId)) {
                reviews.add(r);
            }
        }

        return reviews;

    }

    public double getUserRating(Long userId) {

        double sum = 0;
        int reviewNum = 0;

        for (Review r : reviewRepository.findAll()) {
            if (Objects.equals(r.getReceiverId(), userId)) {
                sum += r.getRate();
                reviewNum++;
            }
        }

        if (reviewNum == 0) return 0;

        return sum / reviewNum;

    }


}
