package com.mipt.app.database.service.departament;

import com.mipt.app.database.repositories.employee.DepartmentRepository;
import com.mipt.app.database.model.department.Department;
import com.mipt.app.database.model.employee.Employee;
import com.mipt.app.database.service.employee.EmployeeService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl implements DepartamentService {

  private DepartmentRepository departmentRepository;

  private EmployeeService employeeService;

  @Autowired
  public DepartmentServiceImpl(
      DepartmentRepository departmentRepository,
      EmployeeService employeeService) {
    this.departmentRepository = departmentRepository;
    this.employeeService = employeeService;
  }

  @Override
  public Department saveDepartament(Department department) {
    return departmentRepository.save(department);
  }

  @Override
  public void deleteDepartamentById(Long id) {
    Department department = departmentRepository.findOne(id);
    department.getEmployees().forEach(employee -> employeeService.deleteEmployee(employee.getId()));
    departmentRepository.delete(id);
  }

  @Override
  public List<Employee> getAllEmployeesInDepartmaenById(Long id) {
    return getById(id).getEmployees();
  }

  @Override
  public Department getById(Long id) {
    return departmentRepository.findOne(id);
  }

  @Override
  public List<Department> getByName(String name) {
    return getAllDepartments().stream().filter(department -> department.getName().equals(name))
        .collect(
            Collectors.toList());
  }

  @Override
  public List<Department> getByLocation(String location) {
    return getAllDepartments().stream()
        .filter(department -> department.getLocation().getCityName().equals(location))
        .collect(
            Collectors.toList());
  }

  @Override
  public Department getAccureDepartment(String name, String city) {
    return getAllDepartments().stream()
        .filter(department -> department.getLocation().getCityName().equals(city)
            && department.getName().equals(name))
        .findFirst().orElse(null);
  }

  @Override
  public List<Department> getAllDepartments() {
    List<Department> departments = new ArrayList<>();
    departmentRepository.findAll().forEach(departments::add);
    return departments;
  }
}
