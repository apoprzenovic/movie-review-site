package com.itsa.moviereviewsite.spring.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Genres")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Genres {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_id")
    private long genreId;

    @Column(name = "genre_name")
    private String genreName;

    public Genres(String genreName) {
        this.genreName = genreName;
    }

}
