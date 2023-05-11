package com.itsa.moviereviewsite.fxml.controllers;

import com.itsa.moviereviewsite.MovieReviewSiteApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SplashController {

    @FXML
    public Button loginBtn;

    @FXML
    public Button signupBtn;

    @FXML
    public Button btnContinueAsGuest;

    /**
     * This method is called when the user clicks the login button.
     * @param actionEvent
     * @throws IOException
     */
    @FXML
    public void goToLogin(ActionEvent actionEvent) throws IOException {
        MovieReviewSiteApplication.setRoot("login");
    }

    /**
     * This method is called when the user clicks the signup button.
     * @param actionEvent
     * @throws IOException
     */
    @FXML
    public void goToSignup(ActionEvent actionEvent) throws IOException {
        MovieReviewSiteApplication.setRoot("signup");
    }

    /**
     * This method is called when the user clicks the continue as guest button.
     * @param actionEvent
     * @throws IOException
     */
    @FXML
    public void goToHomeScreen(ActionEvent actionEvent) throws IOException {
        MovieReviewSiteApplication.setRoot("home");
    }
}
