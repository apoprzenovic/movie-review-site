package com.itsa.moviereviewsite.fxml.controllers;

import com.itsa.moviereviewsite.MovieReviewSiteApplication;
import com.itsa.moviereviewsite.factory.UserFactory;
import com.itsa.moviereviewsite.spring.entities.*;
import com.itsa.moviereviewsite.spring.repos.*;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.List;

@Component
public class AdminController {

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
    private Button btnAddMovie;

    @FXML
    private Button btnAddActor;

    @FXML
    private Button btnAddDirector;

    @FXML
    private VBox vBoxAddMovieContainer;

    @FXML
    private VBox vBoxAddActorContainer;
    @FXML
    private VBox vBoxAddDirectorContainer;

    @FXML
    private TextField tfInsertMovieTitle;

    @FXML
    private TextField tfInsertMovieReleaseDate;

    @FXML
    private TextField tfInsertMovieDirectorId;

    @FXML
    private TextField tfInsertMovieGenreId;

    @FXML
    private TextField tfInsertMovieTrailer;

    @FXML
    private TextField tfInsertMovieImageUrl;
    @FXML
    private TextArea taMovieSummary;

    @FXML
    private TextArea taMoviePlot;

    @FXML
    private Label lblWarningMovie;

    @FXML
    private Label lblWarningActor;

    @FXML
    private TextField tfInsertActorName;

    @FXML
    private TextField tfInsertActorBirthDate;

    @FXML
    private TextField tfInsertActorTagline;

    @FXML
    private TextField tfInsertActorImageLink;

    @FXML
    private TextArea taInsertActorBiography;

    @FXML
    private VBox vBoxActorInputs;

    @FXML
    private VBox vBoxMovieInputs;

    @FXML
    private Label lblWarningDirector;

    @FXML
    private TextField tfInsertDirectorName;

    @FXML
    private TextField tfInsertDirectorBirthdate;

    @FXML
    private TextArea taInsertDirectorBiography;

    @FXML
    private VBox vBoxDirectorInputs;

    @Autowired
    private MoviesRepository moviesRepository;

    @Autowired
    private ActorsRepository actorsRepository;

    @Autowired
    private DirectorsRepository directorsRepository;

    @Autowired
    private GenresRepository genresRepository;

    @FXML
    private TextField tfInsertDirectorImage;


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
    private void addNewMovie(ActionEvent event) throws IOException {
        if (allInputsNotNull(vBoxMovieInputs, lblWarningMovie)) return;

        String title = tfInsertMovieTitle.getText();
        if (title.equals("/^[a-zA-Z0-9]+$/")) {
            lblWarningMovie.setVisible(true);
            return;
        }
        String dateString = tfInsertMovieReleaseDate.getText();
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            format.setLenient(false);
            format.parse(dateString);
        } catch (ParseException e) {
            lblWarningMovie.setVisible(true);
            return;
        }
        java.sql.Date sqlDate = java.sql.Date.valueOf(dateString);


        int directorId = Integer.parseInt(tfInsertMovieDirectorId.getText());
        Directors director = directorsRepository.findByDirectorId(directorId);
        int genreId = Integer.parseInt(tfInsertMovieGenreId.getText());
        Genres genre = genresRepository.findByGenreId(genreId);
        if(director == null || genre == null) {
            lblWarningMovie.setVisible(true);
            return;
        }
        String trailer = tfInsertMovieTrailer.getText();

        try {
            FileInputStream input = new FileInputStream(tfInsertMovieImageUrl.getText());
            new Image(input);
        } catch (FileNotFoundException e) {
            lblWarningMovie.setVisible(true);
            return;
        }
        String image = tfInsertMovieImageUrl.getText();

        String summary = taMovieSummary.getText();
        String plot = taMoviePlot.getText();

        Movies movie = new Movies(title, sqlDate, director, genre, trailer, image, summary, plot);
        moviesRepository.save(movie);

        deleteInput(vBoxMovieInputs);
    }

    @FXML
    private void addNewActor(ActionEvent event) throws IOException {

        if (allInputsNotNull(vBoxActorInputs, lblWarningActor)) return;

        String actorName = tfInsertActorName.getText();
        if (actorName.equals("/^[a-zA-Z0-9]+$/")) {
            lblWarningActor.setVisible(true);
            return;
        }
        String dateString = tfInsertActorBirthDate.getText();
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            format.setLenient(false);
            format.parse(dateString);
        } catch (ParseException e) {
            lblWarningActor.setVisible(true);
            return;
        }
        java.sql.Date sqlDate = java.sql.Date.valueOf(dateString);
        String tagline = tfInsertActorTagline.getText();
        try {
            FileInputStream input = new FileInputStream(tfInsertActorImageLink.getText());
            new Image(input);
        } catch (FileNotFoundException e) {
            lblWarningActor.setVisible(true);
            return;
        }
        String image = tfInsertActorImageLink.getText();
        String biographyText = taInsertActorBiography.getText();

        Actors actor = new Actors(actorName, sqlDate, tagline, biographyText, image);
        actorsRepository.save(actor);

        deleteInput(vBoxActorInputs);
    }

    private boolean allInputsNotNull(VBox vBoxActorInputs, Label lblWarningActor) {
        for (Node node : vBoxActorInputs.getChildren()) {
            if (node instanceof TextField || node instanceof TextArea) {
                if (((TextInputControl) node).getText().isEmpty()) {
                    lblWarningActor.setVisible(true);
                    return true;
                }
            }
        }
        return false;
    }

    private void deleteInput(VBox vBox) {
        for (Node node : vBox.getChildren()) {
            if (node instanceof TextField || node instanceof TextArea) {
                ((TextInputControl) node).setText("");
            }
        }
    }

    @FXML
    private void addNewDirector(ActionEvent event) throws IOException {
        if (allInputsNotNull(vBoxDirectorInputs, lblWarningDirector)) return;

        String directorName = tfInsertDirectorName.getText();
        if (directorName.equals("/^[a-zA-Z0-9]+$/")) {
            lblWarningDirector.setVisible(true);
            return;
        }
        String dateString = tfInsertDirectorBirthdate.getText();
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            format.setLenient(false);
            format.parse(dateString);
        } catch (ParseException e) {
            lblWarningDirector.setVisible(true);
            return;
        }
        java.sql.Date sqlDate = java.sql.Date.valueOf(dateString);
        String biographyText = taInsertDirectorBiography.getText();
        try {
            FileInputStream input = new FileInputStream(tfInsertDirectorImage.getText());
            new Image(input);
        } catch (FileNotFoundException e) {
            lblWarningDirector.setVisible(true);
            return;
        }
        String image = tfInsertDirectorImage.getText();

        Directors director = new Directors(directorName, sqlDate, biographyText, image);
        directorsRepository.save(director);

        deleteInput(vBoxDirectorInputs);
    }

    @FXML
    private void initialize() {

    }

}
