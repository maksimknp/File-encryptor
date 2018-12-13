package com.mipt.app.database.postgresql.model.user;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mipt.app.database.postgresql.model.file.File;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

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

    private String keyPath;

    private String drivePath;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<File> files = new ArrayList<>();

    public User(String username, String password, String drivePath, String keyPath) {
        this.username = username;
        this.password = password;
        this.drivePath = drivePath;
        this.keyPath = keyPath;
    }
}