package com.mipt.app.database.service.departament;


import com.mipt.app.database.model.department.Department;
import com.mipt.app.database.model.employee.Employee;
import java.util.List;

public interface DepartamentService {

  public Department saveDepartament(Department department);

  public void deleteDepartamentById(Long id);

  public List<Employee> getAllEmployeesInDepartmaenById(Long id);

  public Department getById(Long id);

  public List<Department> getByName(String name);

  public List<Department> getByLocation(String location);

  public Department getAccureDepartment(String name, String location);

  public List<Department> getAllDepartments();
}
