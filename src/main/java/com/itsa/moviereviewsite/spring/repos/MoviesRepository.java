package com.itsa.moviereviewsite.spring.repos;

import com.itsa.moviereviewsite.spring.entities.Directors;
import com.itsa.moviereviewsite.spring.entities.Genres;
import com.itsa.moviereviewsite.spring.entities.Movies;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MoviesRepository extends JpaRepository<Movies, Long> {

    Movies findOneByMovieId(Integer movieId);
    //filtered by genre
    @Query("SELECT m FROM Movies m WHERE m.genre = ?1")
    List<Movies> findByGenre(Genres genre);

    //filtered by genre
    @Query("SELECT m FROM Movies m WHERE m.director = ?1")
    List<Movies> findByDirector(Directors director);

    //sorted
    List<Movies> findAll(Sort sort);

    //sorted and filtered by genre
    @Query("SELECT m FROM Movies m WHERE m.genre = ?1")
    List<Movies> findByGenreSorted(Genres genre, Sort sort);

    Movies findOneByTitleIgnoreCase(String title);
    List<Movies> findAllByTitleContainingIgnoreCase(String text);

    Movies findByMovieId(int movieId);

    @Modifying
    @Transactional
    @Query("update Movies m set m.summary = ?1 where m.movieId = ?2")
    int updateMovieSummary(String summary, int actorId);

    @Modifying
    @Transactional
    @Query("update Movies m set m.plot = ?1 where m.movieId = ?2")
    int updateMoviePlot(String plot, int actorId);
}

//In order to save to a database, save() method is needed because of:
//   jpa:
//    hibernate:
//      ddl-auto: none
//in the application.yml.
//To do all the updates automatically, change "ddl-auto: none" to "ddl-auto: auto"
