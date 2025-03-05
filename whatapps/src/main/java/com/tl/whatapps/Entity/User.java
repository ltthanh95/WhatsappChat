package com.tl.whatapps.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private String email;
    private String profile;
    private String password;

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", email=" + email + ", profile=" + profile + ", password="
                + password + "]";
    }

}
