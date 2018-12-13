package com.mipt.app.database.postgresql.model.file;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mipt.app.database.postgresql.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "files")
public class File implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @NonNull
    @Column(name = "path")
    private String path;

    @NonNull
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public File(String path, User user) {
        this.path = path;
        this.user = user;
        initialFileName(path);
    }

    private void initialFileName(String path) {
        String[] pathParts = path.split("/");
        this.name = pathParts[pathParts.length - 1];
    }
}