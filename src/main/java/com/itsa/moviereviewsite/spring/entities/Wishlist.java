package com.itsa.moviereviewsite.spring.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Wishlist")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wishlist_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;
    
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movies movie;

    public Wishlist(Users user, Movies movie) {
        this.user = user;
        this.movie = movie;
    }

}

