package com.itsa.moviereviewsite.fxml.controllers;

import com.itsa.moviereviewsite.MovieReviewSiteApplication;

import com.itsa.moviereviewsite.factory.UserFactory;
import com.itsa.moviereviewsite.spring.entities.*;
import com.itsa.moviereviewsite.spring.repos.*;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.List;

@Component
public class MovieController {

    public static int movieId;
    private Users currentUser;
    @Autowired
    private UserFactory userFactory;
    @FXML
    private TextField tfSearch;
    @FXML
    private MenuButton mButton;
    @FXML
    private MenuItem miAllMovies;
    @FXML
    private MenuItem miProfile;
    @FXML
    private MenuItem miWishlist;
    @FXML
    private TextArea taComment;
    @FXML
    private Button btnSubmit;
    @FXML
    private Label lblTitle;
    @FXML
    private ImageView movieImageView;
    @FXML
    private ImageView ratingImageView;
    @FXML
    private Label lblGenre;
    @FXML
    private Button btnLeadToDirectorPage;
    @FXML
    private TextArea taActors;
    @FXML
    private TextArea taSummary;
    @FXML
    private TextArea taPlot;
    @FXML
    private RadioButton radioButton1;
    @FXML
    private RadioButton radioButton2;
    @FXML
    private RadioButton radioButton3;
    @FXML
    private RadioButton radioButton4;
    @FXML
    private RadioButton radioButton5;
    @Autowired
    private MoviesRepository moviesRepository;
    @Autowired
    private ActorsRepository actorRepository;
    @Autowired
    private ActorMovieRepository actorMovieRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ReviewsRepository reviewsRepository;
    @Autowired
    private WishlistRepository wishlistRepository;
    @Autowired
    private DirectorsRepository directorsRepository;
    @FXML
    private ChoiceBox choiceBoxRating;
    @FXML
    private Hyperlink hyperlinkTrailer;
    @FXML
    private Button btnAddToWishlist;
    @FXML
    private Button btnDeleteMovie;
    @FXML
    private HBox hBoxRemoveBtnContainer;
    @FXML
    private HBox HBoxBtnUpdateMovieContainer;
    private String reviewComment;
    private int reviewRating;

    @FXML
    private Button btnUpdateMovieInfo;

    @FXML
    private void goToAllMovies(ActionEvent event) throws IOException {
        MovieReviewSiteApplication.setRoot("home");
    }

    @FXML
    private void goToWishlist(ActionEvent event) throws IOException {
        MovieReviewSiteApplication.setRoot("wishlist");
    }

    @FXML
    private void goToProfile(ActionEvent event) throws IOException {
        MovieReviewSiteApplication.setRoot("profile");
    }

    @FXML
    private void postReview() {
        reviewComment = taComment.getText().trim();

        if (choiceBoxRating.getValue() != null) {
            reviewRating = (int) choiceBoxRating.getValue();
        } else {
            reviewRating = 0;
        }

        Movies currMovie = moviesRepository.findByMovieId(movieId);
        if (reviewComment == null || reviewComment.isEmpty()) {
            reviewComment = "No comment submitted.";
        }

        reviewsRepository.save(new Reviews(currentUser, currMovie, reviewRating, reviewComment));

        taComment.setText("");
        choiceBoxRating.setValue("");
    }


    private void loadMovieImage(String imageUrl) {
        Task<Image> loadImageTask = new Task<>() {
            @Override
            protected Image call() throws Exception {
                try {
                    return new Image(imageUrl, 200, 232, true, true);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };

        loadImageTask.setOnSucceeded(event -> movieImageView.setImage(loadImageTask.getValue()));
        loadImageTask.setOnFailed(event -> {
            Throwable exception = loadImageTask.getException();
            System.err.println("Failed to load image: " + exception.getMessage());
            exception.printStackTrace();
        });

        Thread thread = new Thread(loadImageTask);
        thread.setDaemon(true);
        thread.start();
    }


    private void populateData() {
        taActors.setText("");

        // Fetch movie first
        Movies rightMovie = moviesRepository.findOneByMovieId(movieId);

        if (rightMovie == null) {
            System.out.println("Movie not found");
            return;
        }

        // Fetch actor-movie relationships by movie
        List<ActorMovie> actorMovies = actorMovieRepository.findAllByMovie(rightMovie);

        if (actorMovies.isEmpty()) {
            System.out.println("No actors associated with this movie");
            return;
        }

        // Populate actors
        System.out.println("Populating actors");
        for (ActorMovie actorMovie : actorMovies) {
            Actors actor = actorMovie.getActor();
            taActors.appendText(actor.getName() + "\n");
        }

        lblTitle.setText(rightMovie.getTitle());
        loadMovieImage(rightMovie.getImage());
        lblGenre.setText(rightMovie.getGenre().getGenreName());
        String directorName = rightMovie.getDirector().getDirectorName();
        btnLeadToDirectorPage.setText(directorName);
        btnLeadToDirectorPage.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                if (directorsRepository.findOneByDirectorNameIgnoreCase(directorName) != null) {
                    DirectorController.directorId = rightMovie.getDirector().getDirectorId();
                    try {
                        MovieReviewSiteApplication.setRoot("director");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        taSummary.setText(rightMovie.getSummary());
        taPlot.setText(rightMovie.getPlot());

        String path = "./src/main/resources/static/images/stars" + rightMovie.getRating() + ".png";
        Image ratingImage = null;
        try {
            ratingImage = new Image(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        ratingImageView.setImage(ratingImage);

        String url = rightMovie.getTrailer();
        hyperlinkTrailer.setText("Watch Trailer on YouTube");

        String osName = System.getProperty("os.name");
        if (osName.startsWith("Mac")) {
            hyperlinkTrailer.setOnAction(e -> {

                try {
                    ProcessBuilder pb = new ProcessBuilder("open", "-a", "Safari", url);
                    pb.start();
                } catch (IOException ex) {
                    System.err.println("Failed to open URL: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });
        } else {
            hyperlinkTrailer.setOnAction(e -> {

                try {
                    ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "start", url);
                    pb.start();
                } catch (IOException ex) {
                    System.err.println("Failed to open URL: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });
        }


        for (int i = 1; i <= 5; i++) {
            choiceBoxRating.getItems().add(i);
        }

        List<Wishlist> wishlists = wishlistRepository.findAllByUser(currentUser);

        for (Wishlist wishlist : wishlists) {
            if (wishlist.getMovie().getMovieId() == movieId && wishlist.getUser().getUserId() == currentUser.getUserId()) {
                btnAddToWishlist.setDisable(true);
                btnAddToWishlist.setText("Added to Wishlist");
            }
        }

        btnAddToWishlist.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                btnAddToWishlist.setDisable(true);
                btnAddToWishlist.setText("Added to Wishlist");
                addToWishlist();
            }
        });
    }

    @FXML
    private void addToWishlist() {
        Movies currMovie = moviesRepository.findByMovieId(movieId);
        Wishlist wishlist = new Wishlist(currentUser, currMovie);
        wishlistRepository.save(wishlist);
    }

    private void disableForGuest() {
        taComment.setPromptText("Log in to leave reviews!");
        taComment.setDisable(true);
        choiceBoxRating.setDisable(true);
        btnSubmit.setDisable(true);
        miProfile.setDisable(true);
        miWishlist.setDisable(true);

    }

    private void makeVisibleForAdmin() {
        hBoxRemoveBtnContainer.setVisible(true);
        HBoxBtnUpdateMovieContainer.setVisible(true);
        taSummary.setEditable(true);
        taPlot.setEditable(true);
        btnDeleteMovie.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                //delete actors
                moviesRepository.delete(moviesRepository.findByMovieId(movieId));
            }
        });

        btnUpdateMovieInfo.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                //update movie summary
                if (!taSummary.getText().equals(moviesRepository.findByMovieId(movieId).getSummary())) {
                    if (moviesRepository.updateMovieSummary(taSummary.getText(), movieId) < 1) {
                        System.out.println("Movie summary was not updated!");
                    } else {
                        System.out.println("Movie summary was updated!");
                    }
                }
                //update movie plot
                if (!taPlot.getText().equals(moviesRepository.findByMovieId(movieId).getPlot())) {
                    if (moviesRepository.updateMoviePlot(taPlot.getText(), movieId) < 1) {
                        System.out.println("Movie plot was not updated!");
                    } else {
                        System.out.println("Movie plot was updated!");
                    }
                }
            }
        });
    }

    @FXML
    private void initialize() {
        currentUser = userFactory.getCurrentUser();
        populateData();
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

