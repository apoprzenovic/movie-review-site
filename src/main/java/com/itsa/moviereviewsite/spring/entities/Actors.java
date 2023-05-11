package com.itsa.moviereviewsite.spring.entities;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Entity
@Table(name = "Actors")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Actors {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "actor_id")
    private int actorId;

    @Column(name = "name")
    private String name;

    @Column(name = "birthdate")
    private Date birthdate;

    @Column(name = "tag_line")
    private String tagLine;

    @Column(name = "biography")
    private String biography;

    @Column(name = "image")
    private String image;

    @Column(name = "rating")
    private int rating;

    // The rating column is omitted as it's a computed column in the database
    // it can be accessed through the ActorsRepository using the method

    public Actors(String name, Date birthdate, String tagLine, String biography, String image) {
        this.name = name;
        this.birthdate = birthdate;
        this.tagLine = tagLine;
        this.biography = biography;
        this.image = image;
    }

}
