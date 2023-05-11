package com.itsa.moviereviewsite.func;

import com.itsa.moviereviewsite.spring.entities.Actors;
import com.itsa.moviereviewsite.spring.entities.Directors;
import com.itsa.moviereviewsite.spring.entities.Movies;
import com.itsa.moviereviewsite.spring.repos.ActorsRepository;
import com.itsa.moviereviewsite.spring.repos.DirectorsRepository;
import com.itsa.moviereviewsite.spring.repos.MoviesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Search {
    // Repositories
    @Autowired
    private MoviesRepository moviesRepository;

    @Autowired
    private ActorsRepository actorsRepository;

    @Autowired
    private DirectorsRepository directorsRepository;

    // Attributes
    private String searchQuery;

    /**
     * Constructors
     */
    public Search() {
    }

    /**
     * Constructors args
     *
     * @param searchQuery
     */
    public Search(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    /**
     * Return Search Query
     *
     * @return searchQuery
     */
    public String getSearchQuery() {
        return searchQuery;
    }

    /**
     * Set Search Query
     *
     * @param searchQuery
     */
    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    /**
     * Search Movies
     * if there is a movie with the exact title, return it
     * else if there are movies with the search query in the title, return them
     * else return empty list
     *
     * @return moviesRepository.findByTitleContaining(searchQuery)
     */
    public List<Movies> searchMovies() {
        List<Movies> movies = new ArrayList<>();
        if (moviesRepository.findOneByTitleIgnoreCase(searchQuery) != null)
            movies.add(moviesRepository.findOneByTitleIgnoreCase(searchQuery));
        else if (moviesRepository.findAllByTitleContainingIgnoreCase(searchQuery) != null)
            movies.addAll(moviesRepository.findAllByTitleContainingIgnoreCase(searchQuery));
        else return List.of();
        return movies;
    }

    /**
     * Search Actors
     * if there is a movie with the exact title, return it
     * else if there are movies with the search query in the title, return them
     * else return empty list
     *
     * @return actorsRepository.findByNameContainingIgnoreCase(searchQuery)
     */
    public List<Actors> searchActors() {
        List<Actors> actors = new ArrayList<>();
        if (actorsRepository.findOneByNameIgnoreCase(searchQuery) != null)
            actors.add(actorsRepository.findOneByNameIgnoreCase(searchQuery));
        else if (actorsRepository.findAllByNameContainingIgnoreCase(searchQuery) != null)
            actors.addAll(actorsRepository.findAllByNameContainingIgnoreCase(searchQuery));
        else return List.of();
        return actors;
    }

    /**
     * Search Directors
     * if there is a movie with the exact title, return it
     * else if there are movies with the search query in the title, return them
     * else return empty list
     *
     * @return directorsRepository.findByNameContainingIgnoreCase(searchQuery)
     */
    public List<Directors> searchDirectors() {
        List<Directors> directors = new ArrayList<>();
        if (directorsRepository.findOneByDirectorNameIgnoreCase(searchQuery) != null)
            directors.add(directorsRepository.findOneByDirectorNameIgnoreCase(searchQuery));
        else if (directorsRepository.findAllByDirectorNameContainingIgnoreCase(searchQuery) != null)
            directors.addAll(directorsRepository.findAllByDirectorNameContainingIgnoreCase(searchQuery));
        else return List.of();
        return directors;
    }


}
