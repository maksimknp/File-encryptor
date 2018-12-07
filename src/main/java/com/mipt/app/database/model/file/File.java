package com.mipt.app.database.model.file;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mipt.app.database.model.user.User;
import com.mipt.app.enums.FileStatus;
import lombok.*;

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
    @Column(name = "status")
    private FileStatus status;

    @NonNull
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public File(String path, User user) {
        this.path = path;
        this.user = user;
        this.status = FileStatus.DECRYPTED;
        initialFileName(path);
    }

    private void initialFileName(String path) {
        String[] pathParts = path.split("/");
        this.name = pathParts[pathParts.length - 1];
    }
}