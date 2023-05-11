package com.itsa.moviereviewsite.spring.entities;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Entity
@Table(name = "Movies")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Movies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private int movieId;

    @Column(name = "title")
    private String title;

    @Column(name = "release_date")
    private Date releaseDate;

    @ManyToOne
    @JoinColumn(name = "director_id")
    private Directors director;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genres genre;

    @Column(name = "rating")
    private int rating;

    @Column(name = "summary")
    private String summary;

    @Column(name = "plot")
    private String plot;

    @Column(name = "trailer")
    private String trailer;

    @Column(name = "image")
    private String image;

    public Movies(String title, Date releaseDate, Directors director, Genres genre, String summary, String plot, String trailer, String image) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.director = director;
        this.genre = genre;
        this.summary = summary;
        this.plot = plot;
        this.trailer = trailer;
        this.image = image;
    }

}

