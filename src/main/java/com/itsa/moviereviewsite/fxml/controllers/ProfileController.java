package com.itsa.moviereviewsite.fxml.controllers;

import com.itsa.moviereviewsite.MovieReviewSiteApplication;
import com.itsa.moviereviewsite.factory.UserFactory;
import com.itsa.moviereviewsite.spring.entities.ActorReviews;
import com.itsa.moviereviewsite.spring.entities.Movies;
import com.itsa.moviereviewsite.spring.entities.Reviews;
import com.itsa.moviereviewsite.spring.entities.Users;
import com.itsa.moviereviewsite.spring.repos.ActorReviewsRepository;
import com.itsa.moviereviewsite.spring.repos.ReviewsRepository;
import com.itsa.moviereviewsite.spring.repos.UsersRepository;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Component
public class ProfileController {

    @FXML
    public Button btnLogOut;
    private Users loggedInUser;
    @Autowired
    private UserFactory userFactory;
    @FXML
    private TextField tfSearch;

    @FXML
    private MenuButton mButton;

    @FXML
    private MenuItem miAllMovies;

    @FXML
    private MenuItem miWishlist;

    @FXML
    private Button btnChangePassword;

    @FXML
    private Label lblUsername;

    @FXML
    private Label lblEmail;

    @FXML
    private Label lblRole;

    @FXML
    private TextField tfNewEmail;

    @FXML
    private TextField tfNewProfilePhoto;

    @FXML
    private VBox vBoxEditInfo;

    @Autowired
    private UsersRepository usersRepository;

    @FXML
    private ImageView imageViewProfilePicture;

    @FXML
    private VBox vBoxRatings;

    @Autowired
    private ReviewsRepository reviewsRepository;

    @Autowired
    private ActorReviewsRepository actorReviewsRepository;

    @FXML
    private void goToAllMovies(ActionEvent event) throws IOException {
        MovieReviewSiteApplication.setRoot("home");
    }

    @FXML
    private void goToWishlist(ActionEvent event) throws IOException {
        MovieReviewSiteApplication.setRoot("wishlist");
    }

    @FXML
    private void showEditOptions(ActionEvent event) throws IOException {
        vBoxEditInfo.setVisible(!vBoxEditInfo.isVisible());
    }

    @FXML
    private void saveNewInfo(ActionEvent event) throws IOException {
        if (!tfNewEmail.getText().equals("")) {
            loggedInUser.setEmail(tfNewEmail.getText());
            tfNewEmail.setText("");
        }
        if (!tfNewProfilePhoto.getText().equals("")) {
            loggedInUser.setProfilePicture(tfNewProfilePhoto.getText());
            tfNewProfilePhoto.setText("");
        }
        usersRepository.save(loggedInUser);
        populateData();
    }

    private void displayRatings() {
        vBoxRatings.getChildren().removeAll(vBoxRatings.getChildren());
        List<Reviews> reviews = reviewsRepository.findAllByUser(loggedInUser);
        List<ActorReviews> actorReviews = actorReviewsRepository.findAllByUser(loggedInUser);

        for (Reviews review : reviews) {
            VBox vBoxRatingContainer = createRatingContainer(review.getMovie().getTitle(), review.getRating(), review.getReviewText());
            vBoxRatings.getChildren().addAll(vBoxRatingContainer);
        }

        for (ActorReviews actorReview : actorReviews) {
            VBox vBoxRatingContainer = createRatingContainer(actorReview.getActor().getName(), actorReview.getRating(), actorReview.getReviewText());
            vBoxRatings.getChildren().addAll(vBoxRatingContainer);
        }
    }

    private void populateData() {
        lblUsername.setText(loggedInUser.getUsername());
        lblEmail.setText(loggedInUser.getEmail() == null ? "No email provided" : loggedInUser.getEmail());
        if (loggedInUser.getProfilePicture() != null) {
            Task<Image> loadImage = new Task<Image>() {
                @Override
                protected Image call() {
                    return new Image(loggedInUser.getProfilePicture());
                }
            };
            loadImage.setOnSucceeded(event -> imageViewProfilePicture.setImage(loadImage.getValue()));
            new Thread(loadImage).start();
        }
        displayRatings();
    }

    // ... (displayRatings method)

    private VBox createRatingContainer(String title, int rating, String reviewText) {
        VBox vBoxRatingContainer = new VBox();
        HBox hBoxRating = new HBox();
        hBoxRating.setPrefWidth(200);
        hBoxRating.setPrefHeight(100);

        Label lblTitle = new Label(title);
        lblTitle.setPrefWidth(557);
        lblTitle.setPrefHeight(29);
        lblTitle.setStyle("-fx-text-fill: #44576d; -fx-font-size: 24px;");

        ImageView ratingImageView = new ImageView();
        ratingImageView.setFitWidth(194.57);
        ratingImageView.setFitHeight(35.8);
        hBoxRating.setPadding(new Insets(0, 0, 5, 0));
        hBoxRating.getChildren().addAll(lblTitle, ratingImageView);

        Task<Image> loadRatingImage = new Task<Image>() {
            @Override
            protected Image call() {
                String path = "./src/main/resources/static/images/stars" + rating + ".png";
                try {
                    return new Image(path);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };

        loadRatingImage.setOnSucceeded(event -> ratingImageView.setImage(loadRatingImage.getValue()));
        new Thread(loadRatingImage).start();

        VBox vBox = new VBox();
        TextArea taComment = new TextArea(reviewText);
        taComment.setFont(Font.font("System", 16));
        taComment.setDisable(true);
        taComment.setWrapText(true);

        vBox.setPrefHeight(160);
        vBox.setPrefWidth(661);
        vBox.getChildren().add(taComment);

        vBoxRatingContainer.getChildren().addAll(hBoxRating, vBox);
        vBoxRatingContainer.setPadding(new Insets(0, 0, 20, 0));

        return vBoxRatingContainer;
    }

    @FXML
    public void logOutOfProfile(ActionEvent event) throws IOException {
        LogInController.id = -1;
        SignupController.id = -1;
        MovieReviewSiteApplication.setRoot("splash");
    }


    @FXML
    private void initialize() {
        loggedInUser = userFactory.getCurrentUser();
        populateData();
    }
}
