package com.mipt.app.database.repositories.file;

import com.mipt.app.database.model.file.File;
import com.mipt.app.database.model.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends CrudRepository<File, Long> {

    boolean existsByPathAndUser (String path, User user);
}
