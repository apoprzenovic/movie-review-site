package com.itsa.moviereviewsite.spring.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Roles")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Roles {

    @Id
    @Column(name = "role_id")
    private long roleId;

    @Column(name = "role_name")
    private String roleName;

}
