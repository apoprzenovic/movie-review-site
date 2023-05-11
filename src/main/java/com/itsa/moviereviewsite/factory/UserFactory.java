package com.itsa.moviereviewsite.factory;

import com.itsa.moviereviewsite.fxml.controllers.LogInController;
import com.itsa.moviereviewsite.fxml.controllers.SignupController;
import com.itsa.moviereviewsite.spring.entities.Users;
import com.itsa.moviereviewsite.spring.repos.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserFactory {

    @Autowired
    private UsersRepository usersRepository;

    public UserFactory(){}

    public Users getCurrentUser() {
        Users currentUser;
        if (LogInController.id != -1) {
            currentUser = usersRepository.findOneByUserId(LogInController.id);
            return currentUser;
        } else if (SignupController.id != -1) {
            currentUser = usersRepository.findOneByUserId(SignupController.id);
            return currentUser;
        } else {
            currentUser = usersRepository.findOneByUserId(17);
            return currentUser;
        }

    }// end getCurrentUser()
} // end class UserFactory
