package com.itsa.moviereviewsite.spring.entities;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.time.LocalDate;

@Entity
@Table(name = "actor_reviews")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class ActorReviews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private int reviewId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "actor_id")
    private Actors actor;

    @Column(name = "rating")
    private int rating;

    @Column(name = "review_text")
    private String reviewText;

    @Column(name = "review_date")
    private Date reviewDate;

    public ActorReviews(Users user, Actors actor, int rating, String reviewText) {
        this.user = user;
        this.actor = actor;
        this.rating = rating;
        this.reviewText = reviewText;
        this.reviewDate = Date.valueOf(LocalDate.now());
    }

}

