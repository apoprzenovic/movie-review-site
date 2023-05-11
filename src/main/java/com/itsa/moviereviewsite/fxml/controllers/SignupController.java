package com.itsa.moviereviewsite.fxml.controllers;

import com.itsa.moviereviewsite.MovieReviewSiteApplication;
import com.itsa.moviereviewsite.spring.entities.Roles;
import com.itsa.moviereviewsite.spring.entities.Users;
import com.itsa.moviereviewsite.spring.repos.RolesRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.itsa.moviereviewsite.spring.repos.UsersRepository;

import java.io.IOException;

@Component
public class SignupController {

    public static int id = -1;
    @FXML
    public Button loginBtn2;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private RolesRepository rolesRepository;
    @FXML
    private TextField nameTf;
    @FXML
    private TextField pwTf;
    @FXML
    private TextField repPwTf;
    @FXML
    private Button signupButton;
    @FXML
    private Label errorLabel;

    @FXML
    private void goToLogin(ActionEvent event) throws IOException {
        MovieReviewSiteApplication.setRoot("login");
    }

    @FXML
    private void submit(ActionEvent event) throws IOException {
        String name = nameTf.getText();
        String password = pwTf.getText();
        String repeatPassword = repPwTf.getText();

        if (usersRepository.findOneByUsernameIgnoreCase(name) != null) {
            errorLabel.setText("Username already exists!");
            return;
        }

        if(name.equals("") || password.equals("") || repeatPassword.equals("")){
            errorLabel.setText("Please fill in all fields!");
            return;
        }

        if(password.length() < 8){
            errorLabel.setText("Password must be at least 8 characters long!");
            return;
        }

        if (!password.matches("^(?=.*[a-zA-Z])(?=.*\\d).+$")) {
            errorLabel.setText("Password must contain both letters and numbers!");
            return;
        }

        if (!password.equals(repeatPassword)) {
            errorLabel.setText("Passwords don't match!");
        } else {
            errorLabel.setText("");
            Users user = new Users();
            user.setUsername(name);
            user.setHashedPassword(password);
            user.setRole(rolesRepository.findByRoleName("General"));

            usersRepository.save(user);
            System.out.println("User saved to database: " + user);
            id = usersRepository.findOneByUsername(name).getUserId();
            MovieReviewSiteApplication.setRoot("home");
        }

    }

}
