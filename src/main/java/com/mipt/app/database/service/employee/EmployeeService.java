package com.mipt.app.database.service.employee;

import com.mipt.app.database.model.employee.Employee;
import java.util.List;

public interface EmployeeService {

  public Employee saveEmployee(Employee employee);

  public void deleteEmployee(Long id);

  public Employee getEmployeeById(Long id);

  public List<Employee> getAllEmployees();

  public List<Employee> getEmployeeByFirstName(String firstName);

  public List<Employee> getEmployeeByLastName(String lastName);

  public List<Employee> getEmployeeByDepartmentName(String departmentName);

  public List<Employee> getEmployeeByAllParametrs(String firstName, String lastName, String departmentName);

  public List<Employee> getEmployeeByFullName(String firstName, String lastName);
}
