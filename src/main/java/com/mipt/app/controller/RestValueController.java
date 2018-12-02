package com.mipt.app.controller;

import com.mipt.app.database.model.department.Department;
import com.mipt.app.database.model.employee.Employee;
import com.mipt.app.database.model.location.Location;
import com.mipt.app.database.service.departament.DepartamentService;
import com.mipt.app.database.service.employee.EmployeeServiceImpl;
import com.mipt.app.database.service.location.LocationServiceImpl;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/rest")
@CrossOrigin
public class RestValueController {

  @Autowired
  private EmployeeServiceImpl employeeService;

  @Autowired
  private LocationServiceImpl locationService;

  @Autowired
  private DepartamentService departamentService;

  //------------------------------------------
  //rest requests for work with employee table
  //------------------------------------------

  private JSONObject createEmployeeJSON(Employee employee) {
    JSONObject resultJson = new JSONObject();
    resultJson.put("id", employee.getId());
    resultJson.put("firstName", employee.getFirstName());
    resultJson.put("lastName", employee.getLastName());
    resultJson.put("department", employee.getDepartment().getName());
    resultJson.put("departmentLocation", employee.getDepartment().getLocation().getCityName());
    return resultJson;
  }

  private JSONObject createJSONResponseForEmployees(List<Employee> employees) {
    JSONObject result = new JSONObject();
    JSONArray arr = new JSONArray();
    for (Employee employee : employees) {
      arr.put(createEmployeeJSON(employee));
    }
    return result.put("employees", arr);
  }

  @RequestMapping(value = "/employee/findById", method = RequestMethod.GET)
  public String getEmployeeById(@RequestParam Long id) {
    Employee emp = employeeService.getEmployeeById(id);
    if (emp == null)
    {
      return "No employee with such id";
    }
    return createEmployeeJSON(emp).toString();
  }

  @RequestMapping(value = "/employee/findByFirstName", method = RequestMethod.GET)
  public String getEmployeeByFirstName(@RequestParam String firstName)
  {
    List<Employee> employees = employeeService.getEmployeeByFirstName(firstName);
    return createJSONResponseForEmployees(employees).toString();
  }

  @RequestMapping(value = "/employee/findByLastName", method = RequestMethod.GET)
  public String getEmployeeByLastName(@RequestParam String lastName) {
    List<Employee> employees = employeeService.getEmployeeByLastName(lastName);
    return createJSONResponseForEmployees(employees).toString();
  }

  @RequestMapping(value = "/employee/findByDepartmentName", method = RequestMethod.GET)
  public String getEmployeeByDepartmentName(@RequestParam String departmentName) {
    List<Employee> employees = employeeService.getEmployeeByDepartmentName(departmentName);
    return createJSONResponseForEmployees(employees).toString();
  }

  @RequestMapping(value = "/employee/findByFullName", method = RequestMethod.GET)
  public String getEmployeeByFulName(@RequestParam String firstName, String lastName) {
    List<Employee> employees = employeeService.getEmployeeByFullName(firstName, lastName);
    return createJSONResponseForEmployees(employees).toString();
  }

  @RequestMapping(value = "/employee/findByAllParameters", method = RequestMethod.GET)
  public String getEmployeeByAllParameters(@RequestParam String firstName, String lastName,
      String departmentName) {
    List<Employee> employees = employeeService
        .getEmployeeByAllParametrs(firstName, lastName, departmentName);
    return createJSONResponseForEmployees(employees).toString();
  }

  @RequestMapping(value = "/employee/findAll", method = RequestMethod.GET)
  public String getAllEmployee() {
    List<Employee> employees = employeeService.getAllEmployees();
    return createJSONResponseForEmployees(employees).toString();
  }

  @RequestMapping(value = "/employee/delete", method = RequestMethod.GET)
  public String deleteEmployeeById(@RequestParam Long id) {
    employeeService.deleteEmployee(id);
    return employeeService.getEmployeeById(id) == null ? "OK" : "NOT OK";
  }

  @RequestMapping(value = "/employee/add", method = RequestMethod.GET)
  public String saveEmployee(@RequestParam String firstName, String lastName,
      String departmentName, String departmentLocation) {
    Department department = departamentService.getByName(departmentName).stream()
        .filter(department1 -> department1.getLocation().getCityName().equals(departmentLocation))
        .findFirst().orElse(null);
    if (department == null) {
      return "Department with such parameters do not exist";
    }
    employeeService.saveEmployee(new Employee(firstName, lastName, department));
    return "OK";
  }

  //--------------------------------------------
  //rest requests for work with department table
  //--------------------------------------------

  private JSONObject createDepartmentJSON(Department department) {
    JSONObject resultJson = new JSONObject();
    resultJson.put("id", department.getId());
    resultJson.put("departmentName", department.getName());
    resultJson.put("departmentLocation", department.getLocation().getCityName());
    return resultJson;
  }

  private JSONObject createJSONResponseForDepartments(List<Department> departments) {
    JSONObject result = new JSONObject();
    JSONArray arr = new JSONArray();
    for (Department department : departments) {
      arr.put(createDepartmentJSON(department));
    }
    return result.put("departments", arr);
  }

  @RequestMapping(value = "/department/findById", method = RequestMethod.GET)
  public String getDepartmentById(@RequestParam Long id) {
    Department dep = departamentService.getById(id);
    if (dep == null)
    {
      return "No department with such id";
    }
    return createDepartmentJSON(dep).toString();
  }

  @RequestMapping(value = "/department/findByName", method = RequestMethod.GET)
  public String getDepartmentByName(@RequestParam String departmentName) {
    List<Department> departments = departamentService.getByName(departmentName);
    return createJSONResponseForDepartments(departments).toString();
  }

  @RequestMapping(value = "/department/findByLocation", method = RequestMethod.GET)
  public String getDepartmentByLocation(@RequestParam String departmentLocation) {
    List<Department> departments = departamentService.getByLocation(departmentLocation);
    return createJSONResponseForDepartments(departments).toString();
  }

  @RequestMapping(value = "/department/findByAllParameters", method = RequestMethod.GET)
  public String getDepartmentByAllParameters(@RequestParam String departmentName,
      String departmentLocation) {
    return createDepartmentJSON(
        departamentService.getAccureDepartment(departmentName, departmentLocation)).toString();
  }

  @RequestMapping(value = "/department/findAll", method = RequestMethod.GET)
  public String getAllDepartments() {
    List<Department> departments = departamentService.getAllDepartments();
    return createJSONResponseForDepartments(departments).toString();
  }

  @RequestMapping(value = "/department/delete", method = RequestMethod.GET)
  public String deleteDepartmentById(@RequestParam Long id) {
    departamentService.deleteDepartamentById(id);
    return departamentService.getById(id) == null ? "OK" : "NOT OK";
  }

  @RequestMapping(value = "/department/add", method = RequestMethod.GET)
  public String saveDepartment(@RequestParam String departmentName, String departmentLocation) {
    Department department = departamentService
        .getAccureDepartment(departmentName, departmentLocation);
    if (department != null) {
      return "Department with such parameters already exists";
    }
    Location location = locationService.getByCity(departmentLocation);
    if (location == null) {
      return "Location %s do not exist";
    }
    departamentService.saveDepartament(new Department(departmentName, location));
    return "OK";
  }

  //------------------------------------------
  //rest requests for work with location table
  //------------------------------------------

  private JSONObject createLocationJSON(Location location) {
    JSONObject resultJson = new JSONObject();
    resultJson.put("id", location.getId());
    resultJson.put("country", location.getCountryName());
    resultJson.put("city", location.getCityName());
    return resultJson;
  }

  private JSONObject createJSONResponseForLocations(List<Location> locations) {
    JSONObject result = new JSONObject();
    JSONArray arr = new JSONArray();
    for (Location location : locations) {
      arr.put(createLocationJSON(location));
    }
    return result.put("locations", arr);
  }

  @RequestMapping(value = "/location/findById", method = RequestMethod.GET)
  public String getLocationById(@RequestParam Long id) {
    Location location = locationService.getById(id);
    if (location == null) {
      return "Location with such id does not exist";
    }
    return createLocationJSON(location).toString();
  }

  @RequestMapping(value = "/location/findByCity", method = RequestMethod.GET)
  public String getLocationByCity(@RequestParam String city) {
    return createLocationJSON(locationService.getByCity(city)).toString();
  }

  @RequestMapping(value = "/location/findByCountry", method = RequestMethod.GET)
  public String getLocationByCountry(@RequestParam String country) {
    List<Location> locations = locationService.getByCountry(country);
    return createJSONResponseForLocations(locations).toString();
  }

  @RequestMapping(value = "/location/findAll", method = RequestMethod.GET)
  public String getAllLocations() {
    List<Location> locations = locationService.getAllLocations();
    return createJSONResponseForLocations(locations).toString();
  }

  @RequestMapping(value = "/location/delete", method = RequestMethod.GET)
  public boolean deleteLocationById(@RequestParam Long id) {
    locationService.deleteLocation(id);
    return locationService.getById(id) == null;
  }

  @RequestMapping(value = "/location/deleteByCity", method = RequestMethod.GET)
  public String deleteLocationByCity(@RequestParam String city) {
    Location location = locationService.getByCity(city);
    if (location == null) {
      return String.format("Location %s already doesn't exist", city);
    }
    locationService.deleteLocation(location.getId());
    return locationService.getById(location.getId()) == null ? "OK" : "NOT OK";
  }

  @RequestMapping(value = "/location/add", method = RequestMethod.GET)
  public String saveLocation(@RequestParam String country, String city) {
    Location location = locationService.getByCity(city);
    if (location != null) {
      return String.format("Location %s already exists", city);
    }
    locationService.saveLocation(new Location(country, city));
    return "OK";
  }
}