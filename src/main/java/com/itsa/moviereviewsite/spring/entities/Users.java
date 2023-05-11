package com.itsa.moviereviewsite.spring.entities;

import jakarta.persistence.*;
import lombok.*;
import org.mindrot.jbcrypt.BCrypt;

@Entity
@Table(name = "Users")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(unique = true, name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "profile_picture")
    private String profilePicture;

    @ManyToOne//(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Roles role;

    public void setHashedPassword(String password){
        this.password = hashPassword(password);
    }

    public Users(String username, String email, String password, String profilePicture, Roles role) {
        this.username = username;
        this.email = email;
        this.password = hashPassword(password);
        this.profilePicture = profilePicture;
        this.role = role;
    }

    public Users(String username, String email, String password, Roles role) {
        this.username = username;
        this.email = email;
        this.password = hashPassword(password);
        this.role = role;
    }

    public Users(String username, String password, Roles role) {
        this.username = username;
        this.password = hashPassword(password);
        this.role = role;
    }

    // Hash the password using BCrypt
    public String hashPassword(String password) {
        String salt = BCrypt.gensalt(12);
        return BCrypt.hashpw(password, salt);
    }

    // Authenticate the password using BCrypt
    public boolean authenticatePassword(String password) {
        return BCrypt.checkpw(password, this.password);
    }

}
