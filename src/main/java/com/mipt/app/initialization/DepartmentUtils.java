package com.mipt.app.initialization;

import com.mipt.app.database.model.department.Department;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class DepartmentUtils {

  public static List<Department> createSomeDepartments(){
    return new ArrayList<Department>(){{
      add(new Department("Software development"));
      add(new Department("Technical suport"));
      add(new Department("Marketing"));
    }};
  }
}
