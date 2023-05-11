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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ActorController {

    public static int actorId;
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
    private Button btnSubmitComment;
    @FXML
    private Button btnSubmitRating;
    @FXML
    private Label lblName;
    @FXML
    private Label lblTagline;
    @FXML
    private TextArea taStarredIn;
    @FXML
    private TextArea taBiography;
    @FXML
    private ImageView actorImageView;
    @FXML
    private ImageView ratingImageView;
    @FXML
    private ChoiceBox choiceBoxRating;
    @Autowired
    private ActorsRepository actorsRepository;
    @Autowired
    private ActorMovieRepository actorMovieRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ActorReviewsRepository actorReviewsRepository;
    @FXML
    private HBox hBoxBtnDeleteContainer;
    @FXML
    private HBox hBoxBtnUpdateContainer;
    private String reviewComment;
    private int reviewRating;

    @FXML
    private Button btnDeleteActor;

    @FXML
    private Button btnUpdateInfo;

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

        Actors currActor = actorsRepository.findByActorId(actorId);
        if (reviewComment == null || reviewComment.isEmpty()) {
            reviewComment = "No comment submitted.";
        }
        actorReviewsRepository.save(new ActorReviews(currentUser, currActor, reviewRating, reviewComment));

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

        loadImageTask.setOnSucceeded(event -> actorImageView.setImage(loadImageTask.getValue()));
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
        taStarredIn.setText("");
        Actors rightActor = actorsRepository.findByActorId(actorId);
        List<ActorMovie> actorMovies = actorMovieRepository.findAllByActorMovieId(actorId);

        for (ActorMovie actorMovie : actorMovies) {
            if (actorMovie.getActor().getActorId() == actorId) {
                taStarredIn.appendText(actorMovie.getMovie().getTitle() + "\n");
            }
        }

        lblName.setText(rightActor.getName());

        loadMovieImage(rightActor.getImage());
        lblTagline.setText(rightActor.getTagLine());
        taBiography.setText(rightActor.getBiography());

        String path = "./src/main/resources/static/images/stars" + rightActor.getRating() + ".png";
        Image ratingImage = null;
        try {
            ratingImage = new Image(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        ratingImageView.setImage(ratingImage);

        for (int i = 1; i <= 5; i++) {
            choiceBoxRating.getItems().add(i);
        }
    }

    private void disableForGuest() {
        taComment.setPromptText("Log in to leave reviews!");
        taComment.setDisable(true);
        choiceBoxRating.setDisable(true);
        btnSubmitComment.setDisable(true);
        miProfile.setDisable(true);
        miWishlist.setDisable(true);
    }

    private void makeVisibleForAdmin() {
        hBoxBtnDeleteContainer.setVisible(true);
        hBoxBtnUpdateContainer.setVisible(true);
        taBiography.setEditable(true);
        btnDeleteActor.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                //delete actors
                actorsRepository.delete(actorsRepository.findByActorId(actorId));
            }
        });

        btnUpdateInfo.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                System.out.println("update button clicked");
                //update actor biography
                if (!taBiography.getText().equals(actorsRepository.findByActorId(actorId).getBiography())) {
                    if (actorsRepository.updateActorBiography(taBiography.getText(), actorId) < 1) {
                        System.out.println("Actor biography was not updated!");
                    } else {
                        System.out.println("Actor biography was updated!");
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
