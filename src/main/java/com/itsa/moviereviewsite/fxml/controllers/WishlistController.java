package com.itsa.moviereviewsite.fxml.controllers;

import com.itsa.moviereviewsite.MovieReviewSiteApplication;
import com.itsa.moviereviewsite.factory.UserFactory;
import com.itsa.moviereviewsite.spring.entities.Users;
import com.itsa.moviereviewsite.spring.entities.Wishlist;
import com.itsa.moviereviewsite.spring.repos.UsersRepository;
import com.itsa.moviereviewsite.spring.repos.WishlistRepository;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Component
public class WishlistController {

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
    private MenuItem miProfile;

    @FXML
    private Button btnRemove;

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private UsersRepository usersRepository;

    @FXML
    private VBox scrollPaneWishlistItems;


    @FXML
    private void goToAllMovies(ActionEvent event) throws IOException {
        MovieReviewSiteApplication.setRoot("home");
    }

    @FXML
    private void goToProfile(ActionEvent event) throws IOException {
        MovieReviewSiteApplication.setRoot("profile");
    }

    public void populateData() {
        scrollPaneWishlistItems.getChildren().clear();
        List<Wishlist> wishlistMovies = wishlistRepository.findAllByUser(loggedInUser);

        for (Wishlist wishlistItem : wishlistMovies) {
            HBox hBoxWishlistItem = new HBox();
            hBoxWishlistItem.setPrefWidth(1166);
            hBoxWishlistItem.setPrefHeight(239);

            //1
            VBox vBoxImage = createImageContainer(wishlistItem.getMovie().getImage());

            //2
            VBox vBoxMiddle = createMiddleContainer(wishlistItem.getMovie().getTitle(), wishlistItem.getMovie().getRating(), wishlistItem.getMovie().getSummary());

            //3
            VBox vBoxBtnContainer = createButtonContainer(wishlistItem);

            hBoxWishlistItem.getChildren().addAll(vBoxImage, vBoxMiddle, vBoxBtnContainer);

            scrollPaneWishlistItems.getChildren().add(hBoxWishlistItem);
        }
    }

    private VBox createImageContainer(String imageUrl) {
        VBox vBoxImage = new VBox();
        ImageView imageView = new ImageView();
        imageView.setFitHeight(200);
        imageView.setFitWidth(158);
        vBoxImage.setPadding(new Insets(20, 20, 20, 20));
        vBoxImage.getChildren().add(imageView);

        Task<Image> loadImage = new Task<Image>() {
            @Override
            protected Image call() {
                return new Image(imageUrl);
            }
        };

        loadImage.setOnSucceeded(event -> imageView.setImage(loadImage.getValue()));
        new Thread(loadImage).start();

        return vBoxImage;
    }

    private VBox createMiddleContainer(String title, int rating, String summary) {
        VBox vBoxMiddle = new VBox();
        HBox hBoxTitleAndRating = createTitleAndRating(title, rating);
        TextArea taSummary = createSummary(summary);
        vBoxMiddle.setPadding(new Insets(20, 20, 20, 20));
        vBoxMiddle.setPrefWidth(867);
        vBoxMiddle.getChildren().addAll(hBoxTitleAndRating, taSummary);

        return vBoxMiddle;
    }

    private HBox createTitleAndRating(String title, int rating) {
        HBox hBoxTitleAndRating = new HBox();
        hBoxTitleAndRating.setPrefSize(757, 29);

        Label lblTitle = new Label(title);
        lblTitle.setPrefWidth(615);
        lblTitle.setFont(new Font("System", 24));
        lblTitle.setStyle("-fx-text-fill: #44576d;");

        ImageView ratingImageView = new ImageView();
        ratingImageView.setPreserveRatio(true);
        hBoxTitleAndRating.setPadding(new Insets(0, 0, 5, 0));
        hBoxTitleAndRating.getChildren().addAll(lblTitle, ratingImageView);

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

        return hBoxTitleAndRating;
    }

    private TextArea createSummary(String summary) {
        TextArea taSummary = new TextArea(summary);
        taSummary.setPadding(new Insets(10, 0, 0, 0));
        taSummary.setWrapText(true);
        taSummary.setDisable(true);

        return taSummary;
    }

    private VBox createButtonContainer(Wishlist wishlistItem) {
        VBox vBoxBtnContainer = new VBox();
        Button btnRemove = new Button("Remove");
        btnRemove.setStyle("-fx-text-fill: white; -fx-background-color: #29353C");
        vBoxBtnContainer.setPadding(new Insets(100, 0, 100, 0));
        btnRemove.setFont(new Font(20));
        vBoxBtnContainer.getChildren().add(btnRemove);

        btnRemove.setOnAction(event -> {
            wishlistRepository.delete(wishlistItem);
            populateData();
        });

        return vBoxBtnContainer;
    }

    @FXML
    private void initialize() {
        loggedInUser = userFactory.getCurrentUser();
        populateData();
    }


}
