package com.mipt.app.database.model.location;

import com.mipt.app.database.model.department.Department;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "locations")
public class Location {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "country_name")
  private String countryName;

  @Column(name = "city_name")
  private String cityName;

  @OneToMany(mappedBy = "location")
  private List<Department> departments;

  public Location(String countryName, String cityName) {
    this.countryName = countryName;
    this.cityName = cityName;
  }

  public Location(){}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCountryName() {
    return countryName;
  }

  public void setCountryName(String countryName) {
    this.countryName = countryName;
  }

  public String getCityName() {
    return cityName;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

  public List<Department> getDepartments() {
    return departments;
  }

  public void setDepartments(
      List<Department> departments) {
    this.departments = departments;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Location location = (Location) o;

    if (id != null ? !id.equals(location.id) : location.id != null) {
      return false;
    }
    if (countryName != null ? !countryName.equals(location.countryName)
        : location.countryName != null) {
      return false;
    }
    if (cityName != null ? !cityName.equals(location.cityName) : location.cityName != null) {
      return false;
    }
    return departments != null ? departments.equals(location.departments)
        : location.departments == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (countryName != null ? countryName.hashCode() : 0);
    result = 31 * result + (cityName != null ? cityName.hashCode() : 0);
    result = 31 * result + (departments != null ? departments.hashCode() : 0);
    return result;
  }
}
