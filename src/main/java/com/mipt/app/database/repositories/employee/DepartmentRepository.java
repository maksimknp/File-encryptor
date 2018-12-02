package com.mipt.app.database.repositories.employee;


import com.mipt.app.database.model.department.Department;
import org.springframework.data.repository.CrudRepository;

public interface DepartmentRepository extends CrudRepository<Department, Long>{

}
