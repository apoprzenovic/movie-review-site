package com.itsa.moviereviewsite.spring.repos;

import com.itsa.moviereviewsite.spring.entities.Actors;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ActorsRepository extends JpaRepository<Actors, Long> {
    List<Actors> findAll(Sort sort);

    Actors findOneByNameIgnoreCase(String name);

    List<Actors> findAllByNameContainingIgnoreCase(String text);

    Actors findByActorId(int actorId);

    @Modifying
    @Transactional
    @Query("update Actors a set a.biography = ?1 where a.actorId = ?2")
    int updateActorBiography(String biography, int actorId);

    Actors findOneByActorId(Integer actorId);

//    @Query(value = "SELECT dbo.GetAverageActorRating(:actor_id)", nativeQuery = true)
//    Integer getAverageActorRating(@Param("actor_id") Integer actorId);

}

//In order to save to a database, save() method is needed because of:
//   jpa:
//    hibernate:
//      ddl-auto: none
//in the application.yml.
//To do all the updates automatically, change "ddl-auto: none" to "ddl-auto: auto"
