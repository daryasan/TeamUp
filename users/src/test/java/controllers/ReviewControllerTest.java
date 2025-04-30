package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.AuthApp;
import org.example.config.Config;
import org.example.security.SecurityConfig;
import org.example.dto.CreateReviewDto;
import org.example.models.Review;
import org.example.services.ReviewService;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {AuthApp.class, Config.class, SecurityConfig.class})
@AutoConfigureMockMvc
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@WithMockUser
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @Autowired
    private ObjectMapper objectMapper;

//    @Test
//    public void add_review_valid_add_review() throws Exception {
//
//        String authHeader = "Bearer dummy-token";
//        Long userId = 1L;
//        CreateReviewDto createReviewDto = new CreateReviewDto();
//
//        Review dummyReview = new Review();
//        dummyReview.setId(100L);
//
//        given(reviewService.addReview("dummy-token", userId, createReviewDto)).willReturn(dummyReview);
//
//        mockMvc.perform(post("/reviews/add/user={userId}", userId)
//                        .header("Authorization", authHeader)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(createReviewDto)))
//
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value(100));
//    }

    @Test
    public void find_review_by_id_valid_find_review() throws Exception {

        Long reviewId = 101L;
        Review dummyReview = new Review();
        dummyReview.setId(reviewId);

        given(reviewService.findReviewById(reviewId)).willReturn(dummyReview);

        mockMvc.perform(get("/reviews/id={id}", reviewId)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(101));
    }

    @Test
    public void edit_review_valid_edit_review() throws Exception {

        Long reviewId = 102L;
        CreateReviewDto createReviewDto = new CreateReviewDto();

        Review updatedReview = new Review();
        updatedReview.setId(reviewId);

        given(reviewService.editReview(eq(reviewId), any(CreateReviewDto.class))).willReturn(updatedReview);

        mockMvc.perform(patch("/reviews/id={id}/edit", reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReviewDto)))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(102));
    }

//    @Test
//    public void delete_review_valid_delete_review() throws Exception {
//
//        String authHeader = "Bearer dummy-token";
//        Long reviewId = 103L;
//
//        given(reviewService.deleteReview("dummy-token", reviewId)).willReturn(true);
//
//        mockMvc.perform(delete("/reviews/id={id}/delete", reviewId)
//                        .header("Authorization", authHeader)
//                        .contentType(MediaType.APPLICATION_JSON))
//
//                .andExpect(status().isOk());
//    }

    @Test
    public void get_user_reviews_valid_get_reviews() throws Exception {

        Long userId = 1L;
        Review review1 = new Review();
        review1.setId(201L);
        Review review2 = new Review();
        review2.setId(202L);
        List<Review> reviews = Arrays.asList(review1, review2);

        given(reviewService.getUserReviews(userId)).willReturn(reviews);

        mockMvc.perform(get("/reviews/by-user/id={userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(201))
                .andExpect(jsonPath("$[1].id").value(202));
    }

    @Test
    public void get_user_rate_valid_get_rating() throws Exception {

        Long userId = 1L;
        double rating = 4.5;

        given(reviewService.getUserRating(userId)).willReturn(rating);

        mockMvc.perform(get("/reviews/by-user/id={userId}/rating", userId)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(4.5));
    }
}
