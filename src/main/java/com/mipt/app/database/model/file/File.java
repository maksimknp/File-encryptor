package com.mipt.app.database.model.file;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mipt.app.database.model.user.User;
import com.mipt.app.enums.FileStatus;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "files")
public class File implements Serializable {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
}