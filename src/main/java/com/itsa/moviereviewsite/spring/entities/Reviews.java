package com.itsa.moviereviewsite.spring.entities;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.time.LocalDate;

@Entity
@Table(name = "Reviews")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Reviews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private int reviewId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movies movie;

    @Column(name = "rating")
    private int rating;

    @Column(name = "review_text")
    private String reviewText;

    @Column(name = "review_date")
    private Date reviewDate;

    public Reviews(Users user, Movies movie, int rating, String reviewText) {
        this.user = user;
        this.movie = movie;
        this.rating = rating;
        this.reviewText = reviewText;
        this.reviewDate = Date.valueOf(LocalDate.now());
    }

}

