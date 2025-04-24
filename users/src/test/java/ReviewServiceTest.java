
import org.example.dto.CreateReviewDto;
import org.example.dto.UserDetailsDto;
import org.example.exceptions.AuthException;
import org.example.exceptions.DataException;
import org.example.exceptions.ReviewException;
import org.example.models.Review;
import org.example.repositories.ReviewRepository;
import org.example.services.ReviewService;
import org.example.services.TokenService;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private TokenService tokenService;

    @Test
    public void add_review_successful() throws DataException, AuthException {
        CreateReviewDto dto = new CreateReviewDto();
        dto.setRate(5);
        dto.setText("Great work!");


        UserDetailsDto userDetails = new UserDetailsDto();
        userDetails.setId(10L);


        when(tokenService.getDetails("validToken")).thenReturn(userDetails);


        Review savedReview = new Review();
        savedReview.setSenderId(10L);
        savedReview.setReceiverId(20L);
        savedReview.setReviewText("Great work!");
        savedReview.setRate(5);


        when(reviewRepository.save(any(Review.class))).thenReturn(savedReview);


        Review review = reviewService.addReview("validToken", 20L, dto);


        assertEquals(10L, review.getSenderId());
        assertEquals(20L, review.getReceiverId());
        assertEquals("Great work!", review.getReviewText());
        assertEquals(5, review.getRate());


        verify(reviewRepository).save(any(Review.class));
        verifyNoMoreInteractions(reviewRepository);
    }

    @Test
    public void add_review_invalid_rate() throws AuthException {
        CreateReviewDto dto = new CreateReviewDto();
        dto.setRate(0);
        dto.setText("Bad rating");


        UserDetailsDto userDetails = new UserDetailsDto();
        userDetails.setId(10L);


        when(tokenService.getDetails("token")).thenReturn(userDetails);


        assertThrows(DataException.class, () -> reviewService.addReview("token", 20L, dto));
    }

    @Test
    public void find_review_by_id_successful() throws ReviewException {
        Review review = new Review();
        review.setSenderId(10L);
        review.setReceiverId(20L);
        review.setReviewText("Nice");
        review.setRate(4);


        when(reviewRepository.findById(100L)).thenReturn(Optional.of(review));


        Review result = reviewService.findReviewById(100L);


        assertThat(result).isEqualTo(review);


        verify(reviewRepository).findById(100L);
        verifyNoMoreInteractions(reviewRepository);
    }

    @Test
    public void find_review_by_id_not_found() {
        when(reviewRepository.findById(100L)).thenReturn(Optional.empty());


        assertThrows(ReviewException.class, () -> reviewService.findReviewById(100L));


        verify(reviewRepository).findById(100L);
        verifyNoMoreInteractions(reviewRepository);
    }

    @Test
    public void edit_review_successful() throws DataException, ReviewException {
        Review existing = new Review();
        existing.setReviewText("Old");
        existing.setRate(3);


        when(reviewRepository.findById(200L)).thenReturn(Optional.of(existing));


        CreateReviewDto dto = new CreateReviewDto();
        dto.setText("Updated review");
        dto.setRate(4);


        when(reviewRepository.save(existing)).thenReturn(existing);


        Review result = reviewService.editReview(200L, dto);


        assertEquals("Updated review", result.getReviewText());
        assertEquals(4, result.getRate());


        verify(reviewRepository).findById(200L);
        verify(reviewRepository).save(existing);
        verifyNoMoreInteractions(reviewRepository);
    }

    @Test
    public void edit_review_invalid_rate() {
        CreateReviewDto dto = new CreateReviewDto();
        dto.setText("Text");
        dto.setRate(6);


        assertThrows(DataException.class, () -> reviewService.editReview(300L, dto));
    }

    @Test
    public void delete_review_review_not_found() throws AuthException {
        UserDetailsDto userDetails = new UserDetailsDto();
        userDetails.setId(10L);


        when(tokenService.getDetails("token")).thenReturn(userDetails);


        when(reviewRepository.findById(400L)).thenReturn(Optional.empty());


        boolean result = reviewService.deleteReview("token", 400L);


        assertFalse(result);


        verify(reviewRepository).findById(400L);
        verifyNoMoreInteractions(reviewRepository);
    }

    @Test
    public void delete_review_wrong_sender() throws AuthException {
        UserDetailsDto userDetails = new UserDetailsDto();
        userDetails.setId(10L);


        when(tokenService.getDetails("token")).thenReturn(userDetails);


        Review review = new Review();
        review.setSenderId(20L);

        when(reviewRepository.findById(500L)).thenReturn(Optional.of(review));


        boolean result = reviewService.deleteReview("token", 500L);


        assertFalse(result);


        verify(reviewRepository).findById(500L);
        verifyNoMoreInteractions(reviewRepository);
    }

    @Test
    public void delete_review_successful() throws AuthException {
        UserDetailsDto userDetails = new UserDetailsDto();
        userDetails.setId(10L);


        when(tokenService.getDetails("token")).thenReturn(userDetails);


        Review review = new Review();
        review.setSenderId(10L);

        when(reviewRepository.findById(600L)).thenReturn(Optional.of(review));


        boolean result = reviewService.deleteReview("token", 600L);


        assertTrue(result);


        verify(reviewRepository).findById(600L);
        verify(reviewRepository).delete(review);
        verifyNoMoreInteractions(reviewRepository);
    }

    @Test
    public void get_user_reviews_returns_matching_reviews() {
        Review r1 = new Review();
        r1.setReceiverId(100L);
        Review r2 = new Review();
        r2.setReceiverId(100L);
        Review r3 = new Review();
        r3.setReceiverId(200L);


        when(reviewRepository.findAll()).thenReturn(List.of(r1, r2, r3));


        List<Review> results = reviewService.getUserReviews(100L);


        assertThat(results).containsExactlyInAnyOrder(r1, r2);


        verify(reviewRepository).findAll();
        verifyNoMoreInteractions(reviewRepository);
    }

    @Test
    public void get_user_reviews_returns_empty_list() {
        Review r1 = new Review();
        r1.setReceiverId(300L);


        when(reviewRepository.findAll()).thenReturn(List.of(r1));


        List<Review> results = reviewService.getUserReviews(400L);


        assertThat(results).isEmpty();


        verify(reviewRepository).findAll();
        verifyNoMoreInteractions(reviewRepository);
    }

    @Test
    public void get_user_rating_returns_average() {
        Review r1 = new Review();
        r1.setReceiverId(500L);
        r1.setRate(4);
        Review r2 = new Review();
        r2.setReceiverId(500L);
        r2.setRate(5);
        Review r3 = new Review();
        r3.setReceiverId(500L);
        r3.setRate(3);


        when(reviewRepository.findAll()).thenReturn(List.of(r1, r2, r3));


        double average = reviewService.getUserRating(500L);


        assertEquals((4 + 5 + 3) / 3.0, average);


        verify(reviewRepository).findAll();
        verifyNoMoreInteractions(reviewRepository);
    }

    @Test
    public void get_user_rating_no_reviews_returns_zero() {
        when(reviewRepository.findAll()).thenReturn(List.of());


        double average = reviewService.getUserRating(600L);


        assertEquals(0, average);


        verify(reviewRepository).findAll();
        verifyNoMoreInteractions(reviewRepository);
    }
}
