package com.mipt.app.initialization;

import com.mipt.app.database.model.employee.Employee;
import com.mipt.app.database.model.location.Location;
import com.mipt.app.database.service.departament.DepartamentService;
import com.mipt.app.database.service.employee.EmployeeServiceImpl;
import com.mipt.app.database.service.location.LocationServiceImpl;
import com.mipt.app.database.model.department.Department;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
public class InitDataBases implements ApplicationRunner {

  @Autowired
  private EmployeeServiceImpl employeeService;

  @Autowired
  private LocationServiceImpl locationService;

  @Autowired
  private DepartamentService departamentService;

  public void run(ApplicationArguments args) {

    addLocationsInDataBase();
    addDepartmentsInDataBase();
    addEmployeesInDataBase();

    List<Location> locations = locationService.getAllLocations();
    List<Department> departments = departamentService.getAllDepartments();
    List<Employee> employees = employeeService.getAllEmployees();

    departments.get(0).setLocation(locations.get(0));
    departments.get(1).setLocation(locations.get(0));
    departments.get(2).setLocation(locations.get(1));

    employees.get(0).setDepartment(departments.get(0));
    employees.get(1).setDepartment(departments.get(2));
    employees.get(2).setDepartment(departments.get(1));
    employees.get(3).setDepartment(departments.get(2));

    departamentService.saveDepartament(departments.get(0));
    departamentService.saveDepartament(departments.get(1));
    departamentService.saveDepartament(departments.get(2));

    employeeService.saveEmployee(employees.get(0));
    employeeService.saveEmployee(employees.get(1));
    employeeService.saveEmployee(employees.get(2));
    employeeService.saveEmployee(employees.get(3));
  }

  private void addEmployeesInDataBase() {
    List<Employee> employees = EmployeeUtils.createSomeEmployees();
    employees.forEach(employee -> employeeService.saveEmployee(employee));
  }

  private void addLocationsInDataBase() {
    List<Location> locations = LocationUtils.createSomeLocations();
  locations.forEach(location -> locationService.saveLocation(location));
  }

  private void addDepartmentsInDataBase() {
    List<Department> departments = DepartmentUtils.createSomeDepartments();
  departments.forEach(department -> departamentService.saveDepartament(department));
  }
}
