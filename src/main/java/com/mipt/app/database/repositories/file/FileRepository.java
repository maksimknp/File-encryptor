package com.mipt.app.database.repositories.file;

import com.mipt.app.database.model.file.File;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends CrudRepository<File, Long> {

    boolean existsByPath (String path);
}
