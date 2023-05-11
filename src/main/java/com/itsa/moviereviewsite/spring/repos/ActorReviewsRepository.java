package com.itsa.moviereviewsite.spring.repos;

import com.itsa.moviereviewsite.spring.entities.ActorReviews;
import com.itsa.moviereviewsite.spring.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActorReviewsRepository extends JpaRepository<ActorReviews, Long> {
    List<ActorReviews> findAllByUser(Users user);
}

//In order to save to a database, save() method is needed because of:
//   jpa:
//    hibernate:
//      ddl-auto: none
//in the application.yml.
//To do all the updates automatically, change "ddl-auto: none" to "ddl-auto: auto"
