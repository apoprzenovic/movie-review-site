package com.itsa.moviereviewsite.spring.entities;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Entity
@Table(name = "Directors")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Directors {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "director_id")
    private Integer directorId;

    @Column(name = "director_name")
    private String directorName;

    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "biography")
    private String biography;

    @Column(name = "image")
    private String image;

    public Directors(String directorName, Date birthDate, String biography, String image) {
        this.directorName = directorName;
        this.birthDate = birthDate;
        this.biography = biography;
        this.image = image;
    }

}

