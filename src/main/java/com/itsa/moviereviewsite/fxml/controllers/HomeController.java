package com.itsa.moviereviewsite.fxml.controllers;

import com.itsa.moviereviewsite.MovieReviewSiteApplication;
import com.itsa.moviereviewsite.factory.UserFactory;
import com.itsa.moviereviewsite.func.Search;
import com.itsa.moviereviewsite.spring.entities.Actors;
import com.itsa.moviereviewsite.spring.entities.Directors;
import com.itsa.moviereviewsite.spring.entities.Movies;
import com.itsa.moviereviewsite.spring.entities.Users;
import com.itsa.moviereviewsite.spring.repos.*;
import jakarta.annotation.PreDestroy;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class HomeController {

    private Users currentUser;

    @Autowired
    private UserFactory userFactory;

    @Autowired
    private Search searchResults;

    @Autowired
    private MoviesRepository moviesRepository;

    @Autowired
    private ActorsRepository actorsRepository;

    @Autowired
    private DirectorsRepository directorsRepository;

    @Autowired
    private GenresRepository genresRepository;

    @Autowired
    private UsersRepository usersRepository;

    @FXML
    private HBox movieItemsContainer;

    @FXML
    private HBox actorItemsContainer;

    @FXML
    private VBox vBoxSearch;

    @FXML
    private Button btnSearch;

    @FXML
    private TextField tfSearch;

    @FXML
    private MenuButton mButton;

    @FXML
    private MenuButton menuButtonFilterMovies;

    @FXML
    private MenuItem miProfile;

    @FXML
    private MenuItem miWishlist;

    @FXML
    private MenuItem miAdmin;

    @FXML
    private MenuItem menuItemFilterMoviesByAlphabet;

    @FXML
    private MenuItem menuItemFilterMoviesByGenreAction;

    @FXML
    private Button btnFilterMovie;

    @FXML
    private Button btnFilterActor;

    @FXML
    private VBox posterOne;

    //MenuItems for filtering movies:
    @FXML
    private MenuItem menuItemFilterMovieAlphabeticalA_Z;
    @FXML
    private MenuItem menuItemFilterMovieAlphabeticalZ_A;
    @FXML
    private MenuItem menuItemFilterMovieRatingH_L;
    @FXML
    private MenuItem menuItemFilterMovieRatingL_H;

    @FXML
    private MenuItem menuItemFilterMovieActionAlphabeticalA_Z;
    @FXML
    private MenuItem menuItemFilterMovieActionAlphabeticalZ_A;
    @FXML
    private MenuItem menuItemFilterMovieActionRatingH_L;
    @FXML
    private MenuItem menuItemFilterMovieActionRatingL_H;

    @FXML
    private MenuItem menuItemFilterMovieAdventureAlphabeticalA_Z;
    @FXML
    private MenuItem menuItemFilterMovieAdventureAlphabeticalZ_A;
    @FXML
    private MenuItem menuItemFilterMovieAdventureRatingH_L;
    @FXML
    private MenuItem menuItemFilterMovieAdventureRatingL_H;

    @FXML
    private MenuItem menuItemFilterMovieAnimationAlphabeticalA_Z;
    @FXML
    private MenuItem menuItemFilterMovieAnimationAlphabeticalZ_A;
    @FXML
    private MenuItem menuItemFilterMovieAnimationRatingH_L;
    @FXML
    private MenuItem menuItemFilterMovieAnimationRatingL_H;

    @FXML
    private MenuItem menuItemFilterMovieComedyAlphabeticalA_Z;
    @FXML
    private MenuItem menuItemFilterMovieComedyAlphabeticalZ_A;
    @FXML
    private MenuItem menuItemFilterMovieComedyRatingH_L;
    @FXML
    private MenuItem menuItemFilterMovieComedyRatingL_H;

    @FXML
    private MenuItem menuItemFilterMovieDramaAlphabeticalA_Z;
    @FXML
    private MenuItem menuItemFilterMovieDramaAlphabeticalZ_A;
    @FXML
    private MenuItem menuItemFilterMovieDramaRatingH_L;
    @FXML
    private MenuItem menuItemFilterMovieDramaRatingL_H;

    @FXML
    private MenuItem menuItemFilterMovieHorrorAlphabeticalA_Z;
    @FXML
    private MenuItem menuItemFilterMovieHorrorAlphabeticalZ_A;
    @FXML
    private MenuItem menuItemFilterMovieHorrorRatingH_L;
    @FXML
    private MenuItem menuItemFilterMovieHorrorRatingL_H;

    @FXML
    private MenuItem menuItemFilterMovieRomanceAlphabeticalA_Z;
    @FXML
    private MenuItem menuItemFilterMovieRomanceAlphabeticalZ_A;
    @FXML
    private MenuItem menuItemFilterMovieRomanceRatingH_L;
    @FXML
    private MenuItem menuItemFilterMovieRomanceRatingL_H;

    @FXML
    private MenuItem menuItemFilterMovieSciFiAlphabeticalA_Z;
    @FXML
    private MenuItem menuItemFilterMovieSciFiAlphabeticalZ_A;
    @FXML
    private MenuItem menuItemFilterMovieSciFiRatingH_L;
    @FXML
    private MenuItem menuItemFilterMovieSciFiRatingL_H;

    @FXML
    private MenuItem menuItemFilterMovieThrillerAlphabeticalA_Z;
    @FXML
    private MenuItem menuItemFilterMovieThrillerAlphabeticalZ_A;
    @FXML
    private MenuItem menuItemFilterMovieThrillerRatingH_L;
    @FXML
    private MenuItem menuItemFilterMovieThrillerRatingL_H;

    //4 MenuItems for filtering actors:
    @FXML
    private MenuItem menuItemFilterActorAlphabeticalA_Z;
    @FXML
    private MenuItem menuItemFilterActorAlphabeticalZ_A;
    @FXML
    private MenuItem menuItemFilterActorRatingH_L;
    @FXML
    private MenuItem menuItemFilterActorRatingL_H;

    @FXML
    private AnchorPane anchorPaneContent;

    @FXML
    private VBox vBoxContent;

    // Executors for loading elements asynchronously
    private ExecutorService executorService = Executors.newCachedThreadPool();

    private static void styleBtn(Button btn) {
        btn.setStyle("-fx-text-fill: white; -fx-underline: true; -fx-background-color: #44576D");
        btn.setPadding(new Insets(10, 0, 0, 500));
        btn.setFont(new Font(24));
    }

    /**
     * This method is called when the user clicks on the "All Movies" in the dropdown
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void goToProfile(ActionEvent event) throws IOException {
        MovieReviewSiteApplication.setRoot("profile");
    }

    /**
     * This method is called when the user clicks on the "Wishlist" in the dropdown
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void goToWishlist(ActionEvent event) throws IOException {
        MovieReviewSiteApplication.setRoot("wishlist");
    }

    @FXML
    private void goToAdmin(ActionEvent event) throws IOException {
        MovieReviewSiteApplication.setRoot("admin");
    }

    /**
     * This method is called when the user clicks on the "Movie"
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void goToMovie(ActionEvent event) throws IOException {
        MovieReviewSiteApplication.setRoot("movie");
    }

    /**
     * This method is called when the user clicks on the "Actor"
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void goToActor(ActionEvent event) throws IOException {
        MovieReviewSiteApplication.setRoot("actor");
    }

    @FXML
    private void showSearchResults(ActionEvent event) throws IOException {
        if (tfSearch.getText().equals("")) return;
        vBoxSearch.getChildren().removeAll(vBoxSearch.getChildren());
        vBoxSearch.setVisible(true);
        AnchorPane.setTopAnchor(vBoxContent, vBoxSearch.getHeight());

        // Do not make the Search class locally, @Autowire it,
        // so it can have access to the database
        searchResults.setSearchQuery(this.tfSearch.getText());
        List<Actors> actors = searchResults.searchActors();
        List<Movies> movies = searchResults.searchMovies();
        List<Directors> directors = searchResults.searchDirectors();

        if (actors.isEmpty() && movies.isEmpty() && directors.isEmpty()) {
            Label noResults = new Label("Nothing found for search: " + searchResults.getSearchQuery());
            noResults.setStyle("-fx-text-fill: white;");
            noResults.setPadding(new Insets(10, 0, 0, 500));
            noResults.setFont(new Font(24));
            vBoxSearch.getChildren().add(noResults);
            return;
        }

        for (Actors actor : actors)
            makeSearchedActorBtn(actor);
        for (Movies movie : movies)
            makeSearchedMovieBtn(movie);
        for (Directors director : directors)
            makeSearchedDirectorBtn(director);
    }

    private void makeSearchedDirectorBtn(Directors director) {
        Button btn = new Button("→ " + director.getDirectorName());
        styleBtn(btn);
        btn.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                try {
                    System.out.println("im going to director: " + director.getDirectorName());
                    DirectorController.directorId = director.getDirectorId();
                    MovieReviewSiteApplication.setRoot("director");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        vBoxSearch.getChildren().addAll(btn);
    }

    private void makeSearchedMovieBtn(Movies movie) {
        Button btn = new Button("→ " + movie.getTitle());
        styleBtn(btn);
        btn.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                try {
                    MovieController.movieId = movie.getMovieId();
                    MovieReviewSiteApplication.setRoot("movie");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        vBoxSearch.getChildren().addAll(btn);
    }

    private void makeSearchedActorBtn(Actors actor) {
        Button btn = new Button("→ " + actor.getName());
        styleBtn(btn);
        btn.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                try {
                    ActorController.actorId = actor.getActorId();
                    MovieReviewSiteApplication.setRoot("actor");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        vBoxSearch.getChildren().addAll(btn);
    }

    /**
     * This method is called when the user clicks on the "All Movies" button.
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void goToAllMovies(ActionEvent event) throws IOException {
        MovieReviewSiteApplication.setRoot("home");
    }

    /**
     * This method is called in initialize as it needs to work after the
     * initial FXML is set up.
     * Every other method should work as such, which is it should be called in initialize.
     * Because if it is not, it will break the program, because it will try to load into
     * a null object.
     */
    public void populateMovies(List<Movies> movies) {
        movieItemsContainer.getChildren().removeAll(movieItemsContainer.getChildren());

        for (Movies movie : movies) {
            Task<Node> loadImageTask = new Task<Node>() {
                @Override
                protected Node call() {
                    return createMovieButton(movie);
                }
            };

            loadImageTask.setOnSucceeded(event -> movieItemsContainer.getChildren().add(loadImageTask.getValue()));
            executorService.submit(loadImageTask);
        }
    }

    public void populateMoviesSlow(List<Movies> movies) {
        movieItemsContainer.getChildren().removeAll(movieItemsContainer.getChildren());
        for (Movies movie: movies) {
            createMovieButton(movie);
            movieItemsContainer.getChildren().add(createMovieButton(movie));
        }
    }


    public void populateActors(List<Actors> actors) {
        actorItemsContainer.getChildren().removeAll(actorItemsContainer.getChildren());

        for (Actors actor : actors) {
            Task<Node> loadImageTask = new Task<Node>() {
                @Override
                protected Node call() {
                    return createActorButton(actor);
                }
            };

            loadImageTask.setOnSucceeded(event -> actorItemsContainer.getChildren().add(loadImageTask.getValue()));
            executorService.submit(loadImageTask);
        }
    }

    public void populateActorsSlow(List<Actors> actors) {
        actorItemsContainer.getChildren().removeAll(actorItemsContainer.getChildren());
        for (Actors actor: actors) {
            createActorButton(actor);
            actorItemsContainer.getChildren().add(createActorButton(actor));
        }
    }

    private Button createMovieButton(Movies movie) {
        Button movieBtn = new Button();
        movieBtn.setStyle("-fx-background-color: none;");
        movieBtn.setOnAction(event -> {
            try {
                MovieController.movieId = movie.getMovieId();
                MovieReviewSiteApplication.setRoot("movie");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        movieBtn.setGraphic(createMovieVBox(movie));
        return movieBtn;
    }

    private Button createActorButton(Actors actor) {
        Button actorBtn = new Button();
        actorBtn.setStyle("-fx-background-color: none;");
        actorBtn.setOnAction(event -> {
            try {
                ActorController.actorId = actor.getActorId();
                MovieReviewSiteApplication.setRoot("actor");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        actorBtn.setGraphic(createActorVBox(actor));
        return actorBtn;
    }

    private VBox createMovieVBox(Movies movie) {
        VBox movieVBox = new VBox();
        movieVBox.setAlignment(Pos.CENTER);
        movieVBox.setPrefHeight(230);
        movieVBox.setPrefWidth(167);
        movieVBox.setSpacing(5);

        Image image = new Image(movie.getImage());
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(200);

        String path = "./src/main/resources/static/images/stars" + movie.getRating() + ".png";
        Image ratingImage = null;
        try {
            ratingImage = new Image(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ImageView ratingImageView = new ImageView(ratingImage);
        ratingImageView.setFitWidth(157);
        ratingImageView.setPreserveRatio(true);
        movieVBox.getChildren().add(imageView);
        movieVBox.getChildren().add(ratingImageView);

        return movieVBox;
    }

    private VBox createActorVBox(Actors actor) {
        VBox actorVBox = new VBox();
        actorVBox.setAlignment(Pos.CENTER);
        actorVBox.setPrefHeight(230);
        actorVBox.setPrefWidth(167);
        actorVBox.setSpacing(5);

        Image image = new Image(actor.getImage());
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(200);

        String path = "./src/main/resources/static/images/stars" + actor.getRating() + ".png";
        Image ratingImage = null;
        try {
            ratingImage = new Image(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ImageView ratingImageView = new ImageView(ratingImage);
        ratingImageView.setFitWidth(157);
        ratingImageView.setPreserveRatio(true);
        actorVBox.getChildren().add(imageView);
        actorVBox.getChildren().add(ratingImageView);
        return actorVBox;
    }
// Add other methods...

    // Don't forget to shut down the executor service when the application exits
    @PreDestroy
    public void shutdownExecutorService() {
        executorService.shutdown();
    }


    /**
     * FILTRATION AND SORTING
     */
    @FXML
    public void sortActorAlphabetA_Z() {
        List<Actors> actors = actorsRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
        populateActorsSlow(actors);
    }

    @FXML
    public void sortMovieAlphabetA_Z() {
        List<Movies> movies = moviesRepository.findAll(Sort.by(Sort.Direction.ASC, "title"));
        populateMoviesSlow(movies);
    }

    @FXML
    public void sortActorAlphabetZ_A() {
        List<Actors> actors = actorsRepository.findAll(Sort.by(Sort.Direction.DESC, "name"));
        populateActorsSlow(actors);
    }

    @FXML
    public void sortMovieAlphabetZ_A() {
        List<Movies> movies = moviesRepository.findAll(Sort.by(Sort.Direction.DESC, "title"));
        populateMoviesSlow(movies);
    }

    @FXML
    public void sortActorRatingH_L() {
        List<Actors> actors = actorsRepository.findAll(Sort.by(Sort.Direction.DESC, "rating"));
        populateActorsSlow(actors);
    }

    @FXML
    public void sortMovieRatingH_L() {
        List<Movies> movies = moviesRepository.findAll(Sort.by(Sort.Direction.DESC, "rating"));
        populateMoviesSlow(movies);
    }

    @FXML
    public void sortActorRatingL_H() {
        List<Actors> actors = actorsRepository.findAll(Sort.by(Sort.Direction.ASC, "rating"));
        populateActorsSlow(actors);
    }

    @FXML
    public void sortMovieRatingL_H() {
        List<Movies> movies = moviesRepository.findAll(Sort.by(Sort.Direction.ASC, "rating"));
        populateMoviesSlow(movies);
    }

    @FXML
    public void sortMovieActionAlphabetA_Z() {
        List<Movies> movies = moviesRepository.findByGenreSorted(genresRepository.findByGenreName("Action"), Sort.by(Sort.Direction.ASC, "title"));
        populateMoviesSlow(movies);
    }

    @FXML
    public void sortMovieActionAlphabetZ_A() {
        List<Movies> movies = moviesRepository.findByGenreSorted(genresRepository.findByGenreName("Action"), Sort.by(Sort.Direction.DESC, "title"));
        populateMoviesSlow(movies);
    }

    @FXML
    public void sortMovieDramaAlphabetA_Z() {
        List<Movies> movies = moviesRepository.findByGenreSorted(genresRepository.findByGenreName("Drama"), Sort.by(Sort.Direction.ASC, "title"));
        populateMoviesSlow(movies);
    }

    @FXML
    public void sortMovieDramaAlphabetZ_A() {
        List<Movies> movies = moviesRepository.findByGenreSorted(genresRepository.findByGenreName("Drama"), Sort.by(Sort.Direction.DESC, "title"));
        populateMoviesSlow(movies);
    }

    @FXML
    public void sortMovieComedyAlphabetA_Z() {
        List<Movies> movies = moviesRepository.findByGenreSorted(genresRepository.findByGenreName("Comedy"), Sort.by(Sort.Direction.ASC, "title"));
        populateMoviesSlow(movies);
    }

    @FXML
    public void sortMovieComedyAlphabetZ_A() {
        List<Movies> movies = moviesRepository.findByGenreSorted(genresRepository.findByGenreName("Comedy"), Sort.by(Sort.Direction.DESC, "title"));
        populateMoviesSlow(movies);
    }

    @FXML
    public void sortMovieScienceFictionAlphabetA_Z() {
        List<Movies> movies = moviesRepository.findByGenreSorted(genresRepository.findByGenreName("Science Fiction"), Sort.by(Sort.Direction.ASC, "title"));
        populateMoviesSlow(movies);
    }

    @FXML
    public void sortMovieScienceFictionAlphabetZ_A() {
        List<Movies> movies = moviesRepository.findByGenreSorted(genresRepository.findByGenreName("Science Fiction"), Sort.by(Sort.Direction.DESC, "title"));
        populateMoviesSlow(movies);
    }

    @FXML
    public void sortMovieHorrorAlphabetA_Z() {
        List<Movies> movies = moviesRepository.findByGenreSorted(genresRepository.findByGenreName("Horror"), Sort.by(Sort.Direction.ASC, "title"));
        populateMoviesSlow(movies);
    }

    @FXML
    public void sortMovieHorrorAlphabetZ_A() {
        List<Movies> movies = moviesRepository.findByGenreSorted(genresRepository.findByGenreName("Horror"), Sort.by(Sort.Direction.DESC, "title"));
        populateMoviesSlow(movies);
    }

    @FXML
    public void sortMovieThrillerAlphabetA_Z() {
        List<Movies> movies = moviesRepository.findByGenreSorted(genresRepository.findByGenreName("Thriller"), Sort.by(Sort.Direction.ASC, "title"));
        populateMoviesSlow(movies);
    }

    @FXML
    public void sortMovieThrillerAlphabetZ_A() {
        List<Movies> movies = moviesRepository.findByGenreSorted(genresRepository.findByGenreName("Thriller"), Sort.by(Sort.Direction.DESC, "title"));
        populateMoviesSlow(movies);
    }

    @FXML
    public void sortMovieRomanceAlphabetA_Z() {
        List<Movies> movies = moviesRepository.findByGenreSorted(genresRepository.findByGenreName("Romance"), Sort.by(Sort.Direction.ASC, "title"));
        populateMoviesSlow(movies);
    }

    @FXML
    public void sortMovieRomanceAlphabetZ_A() {
        List<Movies> movies = moviesRepository.findByGenreSorted(genresRepository.findByGenreName("Romance"), Sort.by(Sort.Direction.DESC, "title"));
        populateMoviesSlow(movies);
    }

    @FXML
    public void sortMovieAnimationAlphabetA_Z() {
        List<Movies> movies = moviesRepository.findByGenreSorted(genresRepository.findByGenreName("Animation"), Sort.by(Sort.Direction.ASC, "title"));
        populateMoviesSlow(movies);
    }

    @FXML
    public void sortMovieAnimationAlphabetZ_A() {
        List<Movies> movies = moviesRepository.findByGenreSorted(genresRepository.findByGenreName("Animation"), Sort.by(Sort.Direction.DESC, "title"));
        populateMoviesSlow(movies);
    }

    @FXML
    public void sortMovieAdventureAlphabetA_Z() {
        List<Movies> movies = moviesRepository.findByGenreSorted(genresRepository.findByGenreName("Adventure"), Sort.by(Sort.Direction.ASC, "title"));
        populateMoviesSlow(movies);
    }

    @FXML
    public void sortMovieAdventureAlphabetZ_A() {
        List<Movies> movies = moviesRepository.findByGenreSorted(genresRepository.findByGenreName("Adventure"), Sort.by(Sort.Direction.DESC, "title"));
        populateMoviesSlow(movies);
    }

    @FXML
    public void sortMovieActionByRatingH_L() {
        List<Movies> movies = moviesRepository.findByGenreSorted(genresRepository.findByGenreName("Action"), Sort.by(Sort.Direction.DESC, "rating"));
        populateMoviesSlow(movies);
    }

    @FXML
    public void sortMovieActionByRatingL_H() {
        List<Movies> movies = moviesRepository.findByGenreSorted(genresRepository.findByGenreName("Action"), Sort.by(Sort.Direction.ASC, "rating"));
        populateMoviesSlow(movies);
    }

    @FXML
    public void sortMovieDramaByRatingH_L() {
        List<Movies> movies = moviesRepository.findByGenreSorted(genresRepository.findByGenreName("Drama"), Sort.by(Sort.Direction.DESC, "rating"));
        populateMoviesSlow(movies);
    }

    @FXML
    public void sortMovieDramaByRatingL_H() {
        List<Movies> movies = moviesRepository.findByGenreSorted(genresRepository.findByGenreName("Drama"), Sort.by(Sort.Direction.ASC, "rating"));
        populateMoviesSlow(movies);
    }

    @FXML
    public void sortMovieComedyByRatingH_L() {
        List<Movies> movies = moviesRepository.findByGenreSorted(genresRepository.findByGenreName("Comedy"), Sort.by(Sort.Direction.DESC, "rating"));
        populateMoviesSlow(movies);
    }

    @FXML
    public void sortMovieComedyByRatingL_H() {
        List<Movies> movies = moviesRepository.findByGenreSorted(genresRepository.findByGenreName("Comedy"), Sort.by(Sort.Direction.ASC, "rating"));
        populateMoviesSlow(movies);
    }

    @FXML
    public void sortMovieScienceFictionByRatingH_L() {
        List<Movies> movies = moviesRepository.findByGenreSorted(genresRepository.findByGenreName("Science Fiction"), Sort.by(Sort.Direction.DESC, "rating"));
        populateMoviesSlow(movies);
    }

    @FXML
    public void sortMovieScienceFictionByRatingL_H() {
        List<Movies> movies = moviesRepository.findByGenreSorted(genresRepository.findByGenreName("Science Fiction"), Sort.by(Sort.Direction.ASC, "rating"));
        populateMoviesSlow(movies);
    }

    @FXML
    public void sortMovieHorrorByRatingH_L() {
        List<Movies> movies = moviesRepository.findByGenreSorted(genresRepository.findByGenreName("Horror"), Sort.by(Sort.Direction.DESC, "rating"));
        populateMoviesSlow(movies);
    }

    @FXML
    public void sortMovieHorrorByRatingL_H() {
        List<Movies> movies = moviesRepository.findByGenreSorted(genresRepository.findByGenreName("Horror"), Sort.by(Sort.Direction.ASC, "rating"));
        populateMoviesSlow(movies);
    }

    @FXML
    public void sortMovieThrillerByRatingH_L() {
        List<Movies> movies = moviesRepository.findByGenreSorted(genresRepository.findByGenreName("Thriller"), Sort.by(Sort.Direction.DESC, "rating"));
        populateMoviesSlow(movies);
    }

    @FXML
    public void sortMovieThrillerByRatingL_H() {
        List<Movies> movies = moviesRepository.findByGenreSorted(genresRepository.findByGenreName("Thriller"), Sort.by(Sort.Direction.ASC, "rating"));
        populateMoviesSlow(movies);
    }

    @FXML
    public void sortMovieRomanceByRatingH_L() {
        List<Movies> movies = moviesRepository.findByGenreSorted(genresRepository.findByGenreName("Romance"), Sort.by(Sort.Direction.DESC, "rating"));
        populateMoviesSlow(movies);
    }

    @FXML
    public void sortMovieRomanceByRatingL_H() {
        List<Movies> movies = moviesRepository.findByGenreSorted(genresRepository.findByGenreName("Romance"), Sort.by(Sort.Direction.ASC, "rating"));
        populateMoviesSlow(movies);
    }

    @FXML
    public void sortMovieAnimationByRatingH_L() {
        List<Movies> movies = moviesRepository.findByGenreSorted(genresRepository.findByGenreName("Animation"), Sort.by(Sort.Direction.DESC, "rating"));
        populateMoviesSlow(movies);
    }

    @FXML
    public void sortMovieAnimationByRatingL_H() {
        List<Movies> movies = moviesRepository.findByGenreSorted(genresRepository.findByGenreName("Animation"), Sort.by(Sort.Direction.ASC, "rating"));
        populateMoviesSlow(movies);
    }

    @FXML
    public void sortMovieAdventureByRatingH_L() {
        List<Movies> movies = moviesRepository.findByGenreSorted(genresRepository.findByGenreName("Adventure"), Sort.by(Sort.Direction.DESC, "rating"));
        populateMoviesSlow(movies);
    }

    @FXML
    public void sortMovieAdventureByRatingL_H() {
        List<Movies> movies = moviesRepository.findByGenreSorted(genresRepository.findByGenreName("Adventure"), Sort.by(Sort.Direction.ASC, "rating"));
        populateMoviesSlow(movies);
    }

    //END OF FILTRATION AND SORTING


    // Recommendation system

    /**
     * Sorts movies by recommendation
     */
    @FXML
    public void sortMovieByRecommendation() {
        Map<String, Double> averageRatingsByGenre = getAverageRatingsByGenre();
        List<Map.Entry<String, Double>> sortedGenreRatings = new ArrayList<>(averageRatingsByGenre.entrySet());
        Collections.sort(sortedGenreRatings, (a, b) -> b.getValue().compareTo(a.getValue()));

        int topMGenres = 3; // Change this to the number of top genres to consider
        int topNMoviesPerGenre = 5; // Change this to the number of top movies per genre to recommend
        List<Movies> recommendedMovies = new ArrayList<>();

        for (int i = 0; i < topMGenres && i < sortedGenreRatings.size(); i++) {
            String genreName = sortedGenreRatings.get(i).getKey();
            List<Movies> topMoviesInGenre = moviesRepository.findByGenreSorted(genresRepository.findByGenreName(genreName), Sort.by(Sort.Direction.DESC, "rating"));

            for (int j = 0; j < topNMoviesPerGenre && j < topMoviesInGenre.size(); j++) {
                recommendedMovies.add(topMoviesInGenre.get(j));
            }
        }

        populateMoviesSlow(recommendedMovies);
    }


    /**
     * Gets the Average Ratings by Genre of Movies
     *
     * @return Map of genre names to average ratings for that genre
     */
    private Map<String, Double> getAverageRatingsByGenre() {
        List<Movies> allMovies = moviesRepository.findAll();
        Map<String, Double> averageRatingsByGenre = new HashMap<>();
        Map<String, Integer> movieCountByGenre = new HashMap<>();

        for (Movies movie : allMovies) {
            String genreName = movie.getGenre().getGenreName();
            double rating = movie.getRating();

            if (averageRatingsByGenre.containsKey(genreName)) {
                averageRatingsByGenre.put(genreName, averageRatingsByGenre.get(genreName) + rating);
                movieCountByGenre.put(genreName, movieCountByGenre.get(genreName) + 1);
            } else {
                averageRatingsByGenre.put(genreName, rating);
                movieCountByGenre.put(genreName, 1);
            }
        }

        for (String genreName : averageRatingsByGenre.keySet()) {
            averageRatingsByGenre.put(genreName, averageRatingsByGenre.get(genreName) / movieCountByGenre.get(genreName));
        }

        return averageRatingsByGenre;
    }

    private void disableForGuest() {
        miProfile.setDisable(true);
        miWishlist.setDisable(true);
    }

    private void makeVisibleForAdmin() {
        miAdmin.setVisible(true);
    }


    /**
     * Initializes the controller class.
     * This method is automatically called AFTER the fxml file has been loaded.
     * This is where you must initialize any DYNAMIC UI elements that you want to have
     */
    @FXML
    public void initialize() {
        currentUser = userFactory.getCurrentUser();
        List<Movies> allMovies = moviesRepository.findAll();
        List<Actors> allActors = actorsRepository.findAll();
        populateMovies(allMovies);
        populateActors(allActors);

        //if guest
        if (currentUser.getRole().getRoleId() == 3) {
            disableForGuest();
        }

        //if admin
        if (currentUser.getRole().getRoleId() == 1) {
            makeVisibleForAdmin();
        }

    }

}

