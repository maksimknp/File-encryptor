package com.mipt.app.database.postgresql.repositories.file;

import com.mipt.app.database.postgresql.model.file.File;
import com.mipt.app.database.postgresql.model.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends CrudRepository<File, Long> {

    boolean existsByPathAndUser (String path, User user);
}
