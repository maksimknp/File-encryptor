package com.mipt.app.database.service.employee;

import com.mipt.app.database.model.employee.Employee;
import com.mipt.app.database.repositories.employee.EmployeeRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

  private EmployeeRepository employeeRepository;

  @Autowired
  public EmployeeServiceImpl(
      EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  @Override
  public Employee saveEmployee(Employee employee) {
    return this.employeeRepository.save(employee);
  }

  @Override
  public void deleteEmployee(Long id) {
    this.employeeRepository.delete(id);
  }

  @Override
  public Employee getEmployeeById(Long id) {
    return this.employeeRepository.findOne(id);
  }


  @Override
  public List<Employee> getAllEmployees() {
    List<Employee> employees = new ArrayList<>();
    this.employeeRepository.findAll().forEach(employees::add);
    return employees;
  }

  @Override
  public List<Employee> getEmployeeByFirstName(String firstName) {
    return getAllEmployees().stream().filter(employee -> employee.getFirstName().equals(firstName))
        .collect(Collectors.toList());
  }

  @Override
  public List<Employee> getEmployeeByLastName(String lastName) {
    return getAllEmployees().stream().filter(employee -> employee.getLastName().equals(lastName))
        .collect(Collectors.toList());
  }

  @Override
  public List<Employee> getEmployeeByDepartmentName(String departmentName) {
    return getAllEmployees().stream()
        .filter(employee -> employee.getDepartment().getName().equals(departmentName))
        .collect(Collectors.toList());
  }

  @Override
  public List<Employee> getEmployeeByAllParametrs(String firstName, String lastName, String departmentName) {
    return getEmployeeByDepartmentName(departmentName).stream().filter(
        employee -> employee.getFirstName().equals(firstName) && employee.getLastName()
            .equals(lastName)).collect(Collectors.toList());
  }

  @Override
  public List<Employee> getEmployeeByFullName(String firstName, String lastName) {
    return getAllEmployees().stream().filter(
        employee -> employee.getFirstName().equals(firstName) && employee.getLastName()
            .equals(lastName)).collect(Collectors.toList());
  }


}
