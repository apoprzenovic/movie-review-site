package com.itsa.moviereviewsite.spring.repos;

import com.itsa.moviereviewsite.spring.entities.ActorMovie;
import com.itsa.moviereviewsite.spring.entities.Movies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActorMovieRepository extends JpaRepository<ActorMovie, Long> {
    List<ActorMovie> findAllByActorMovieId(Integer actorMovieId);

    List<ActorMovie> findAllByMovie(Movies movie);
}

//In order to save to a database, save() method is needed because of:
//   jpa:
//    hibernate:
//      ddl-auto: none
//in the application.yml.
//To do all the updates automatically, change "ddl-auto: none" to "ddl-auto: auto"
