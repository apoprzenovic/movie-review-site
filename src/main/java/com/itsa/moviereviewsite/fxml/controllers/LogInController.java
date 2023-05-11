package com.itsa.moviereviewsite.fxml.controllers;

import com.itsa.moviereviewsite.MovieReviewSiteApplication;
import com.itsa.moviereviewsite.spring.entities.Users;
import com.itsa.moviereviewsite.spring.repos.UsersRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LogInController {

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfPw;

    @FXML
    private Button btnLogin;

    @FXML
    private Label lblError;

    public static int id = -1;

    @Autowired
    private UsersRepository usersRepository;

    @FXML
    private void goToSignUp(ActionEvent event) throws IOException {
        MovieReviewSiteApplication.setRoot("signup");
    }

    @FXML
    private void submit(ActionEvent event) throws IOException {
        String name = tfName.getText();
        String password = tfPw.getText();

        Users user = usersRepository.findOneByUsername(name);

        if (user == null) {
            lblError.setText("Username not found! Try again.");
        } else if (!user.authenticatePassword(password)) {
            lblError.setText("Incorrect password! Try again.");
        } else {
            lblError.setText("");
            System.out.println("Logged in as " + user.getUsername());
            id = user.getUserId();
            MovieReviewSiteApplication.setRoot("home");
        }
    }

}
