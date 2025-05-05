package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.dto.CreateReviewDto;
import org.example.exceptions.AuthException;
import org.example.exceptions.DataException;
import org.example.exceptions.ReviewException;
import org.example.models.Review;
import org.example.services.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/add")
    public ResponseEntity<Review> addReview(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam Long userId,
            @RequestBody CreateReviewDto createReviewDto
    ) throws DataException, AuthException, ReviewException {
        String token = authHeader.substring(7);
        return new ResponseEntity<>(reviewService.addReview(token, userId, createReviewDto), HttpStatus.CREATED);
    }


    @GetMapping("/id")
    public ResponseEntity<Review> findReviewById(
            @RequestParam Long id
    ) throws ReviewException {
        return ResponseEntity.ok(reviewService.findReviewById(id));
    }


    @PatchMapping("/edit")
    public ResponseEntity<Review> editReview(
            @RequestParam Long id,
            @RequestBody CreateReviewDto createReviewDto
    ) throws DataException, ReviewException {
        return ResponseEntity.ok(reviewService.editReview(id, createReviewDto));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<HttpStatus> deleteReview(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam Long id
    ) throws AuthException {
        String token = authHeader.substring(7);
        boolean ans = reviewService.deleteReview(token, id);
        if (ans) return new ResponseEntity<>(HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @GetMapping("/by-user")
    public ResponseEntity<List<Review>> getUserReviews(
            @RequestParam Long userId
    ){
        return ResponseEntity.ok(reviewService.getUserReviews(userId));
    }


    @GetMapping("/by-user/rating")
    public ResponseEntity<Double> getUserRate(
            @RequestParam Long userId
    ) {
        return ResponseEntity.ok(reviewService.getUserRating(userId));
    }

}
