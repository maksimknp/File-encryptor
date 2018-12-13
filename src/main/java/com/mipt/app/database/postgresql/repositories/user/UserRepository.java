package com.mipt.app.database.postgresql.repositories.user;

import com.mipt.app.database.postgresql.model.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> getUserByUsername(String userName);

    boolean existsByUsername(String username);

}
