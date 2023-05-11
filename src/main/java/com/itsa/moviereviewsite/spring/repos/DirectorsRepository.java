package com.itsa.moviereviewsite.spring.repos;

import com.itsa.moviereviewsite.spring.entities.Directors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DirectorsRepository extends JpaRepository<Directors, Long> {
    Directors findOneByDirectorNameIgnoreCase(String name);
    List<Directors> findAllByDirectorNameContainingIgnoreCase(String text);

    Directors findByDirectorId(int directorId);

    @Modifying
    @Transactional
    @Query("update Directors d set d.biography = ?1 where d.directorId = ?2")
    int updateDirectorBiography(String biography, int directorId);

}

//In order to save to a database, save() method is needed because of:
//   jpa:
//    hibernate:
//      ddl-auto: none
//in the application.yml.
//To do all the updates automatically, change "ddl-auto: none" to "ddl-auto: auto"
