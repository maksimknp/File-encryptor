package com.mipt.app.initialization;

import com.mipt.app.database.model.employee.Employee;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class EmployeeUtils {

  protected static List<Employee> createSomeEmployees() {
    return new ArrayList<Employee>(){{
      add(new Employee("Alex", "Lion"));
      add(new Employee("Tommy", "Cash"));
      add(new Employee("Timmy", "Terner"));
      add(new Employee("Mike", "Vazovski"));
    }};
  }
}
