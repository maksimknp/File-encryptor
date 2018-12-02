package com.mipt.app.database.repositories.employee;

import com.mipt.app.database.model.employee.Employee;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
}
