package com.mipt.app.database.model.user;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mipt.app.database.model.file.File;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name = "username")
    private String username;

    @NonNull
    @JsonIgnore
    @Column(name = "password")
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<File> files = new ArrayList<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}