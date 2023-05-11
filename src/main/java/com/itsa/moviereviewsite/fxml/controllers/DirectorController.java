package com.itsa.moviereviewsite.fxml.controllers;

import com.itsa.moviereviewsite.MovieReviewSiteApplication;
import com.itsa.moviereviewsite.factory.UserFactory;
import com.itsa.moviereviewsite.spring.entities.*;
import com.itsa.moviereviewsite.spring.repos.*;
import jakarta.annotation.PreDestroy;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class DirectorController {

    public static int directorId;
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
    private Label lblName;
    @FXML
    private Label lblBirthday;
    @FXML
    private TextArea taBiography;
    @FXML
    private ImageView directorImageView;
    @Autowired
    private DirectorsRepository directorsRepository;
    @Autowired
    private MoviesRepository moviesRepository;
    @FXML
    private Button btnDeleteDirector;
    @FXML
    private Button btnUpdateDirectorInfo;
    @FXML
    private HBox hBoxButtonsContainer;
    private Users currentUser;

    @Autowired
    private UserFactory userFactory;

    @FXML
    private TextArea taMoviesDirected;

    private Directors rightDirector;

    private ExecutorService executorService;

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
    private void getDirectorMovies() {
    }

    private void populateData() {
        executorService.submit(() -> {
            rightDirector = directorsRepository.findByDirectorId(directorId);
            List<Movies> movies = moviesRepository.findByDirector(rightDirector);

            Platform.runLater(() -> {
                lblName.setText(rightDirector.getDirectorName());
                lblBirthday.setText(rightDirector.getBirthDate() + "");
                directorImageView.setImage(new Image(rightDirector.getImage()));
                taBiography.setText(rightDirector.getBiography());


                for (Movies movie : movies) {
                    taMoviesDirected.appendText(movie.getTitle() + ", " + movie.getReleaseDate() + "\n");
                }
            });

        });
    }

    private void disableForGuest() {
        miProfile.setDisable(true);
        miWishlist.setDisable(true);
    }

    private void displayForAdmin() {
        hBoxButtonsContainer.setVisible(true);
        taBiography.setEditable(true);
        taMoviesDirected.setEditable(true);

        btnDeleteDirector.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                //delete director
                directorsRepository.delete(rightDirector);
            }
        });

        btnUpdateDirectorInfo.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                //update director
                if (!taBiography.getText().equals(rightDirector.getBiography())) {
                    if (directorsRepository.updateDirectorBiography(taBiography.getText(), directorId) < 1) {
                        System.out.println("Director biography was not updated!");
                    } else {
                        System.out.println("Director biography was updated!");
                    }
                }
            }
        });
    }

    @PreDestroy
    public void shutdownExecutorService() {
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    @FXML
    private void initialize() {
        executorService = Executors.newFixedThreadPool(2);
        currentUser = userFactory.getCurrentUser();
        populateData();
        //if guest
        if (currentUser.getRole().getRoleId() == 3) {
            disableForGuest();
        }
        //if admin
        if (currentUser.getRole().getRoleId() == 1) {
            displayForAdmin();
        }
    }

}
