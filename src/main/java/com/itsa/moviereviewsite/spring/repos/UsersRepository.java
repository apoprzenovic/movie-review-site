package com.itsa.moviereviewsite.spring.repos;

import com.itsa.moviereviewsite.spring.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findOneByUsername(String username);
    Users findOneByUsernameIgnoreCase(String username);
    Users findOneByUserId(Integer userId);

}

//In order to save to a database, save() method is needed because of:
//   jpa:
//    hibernate:
//      ddl-auto: none
//in the application.yml.
//To do all the updates automatically, change "ddl-auto: none" to "ddl-auto: auto"
