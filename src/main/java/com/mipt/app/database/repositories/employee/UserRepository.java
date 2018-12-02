package com.mipt.app.database.repositories.employee;

import com.mipt.app.database.model.user.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

}
