package com.mipt.app.database.repositories.employee;

import com.mipt.app.database.model.location.Location;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Maks on 19.05.2018.
 */
public interface LocationRepository extends CrudRepository<Location, Long> {

}
