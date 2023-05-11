package com.itsa.moviereviewsite.spring.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "actor_movie")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class ActorMovie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "actor_movie_id")
    private Integer actorMovieId;

    @ManyToOne
    @JoinColumn(name = "actor_id", referencedColumnName = "actor_id")
    private Actors actor;

    @ManyToOne
    @JoinColumn(name = "movie_id", referencedColumnName = "movie_id")
    private Movies movie;

    public ActorMovie(Actors actor, Movies movie) {
        this.actor = actor;
        this.movie = movie;
    }

}

