package com.mipt.app.database.service.location;

import com.mipt.app.database.model.location.Location;
import com.mipt.app.database.repositories.employee.LocationRepository;
import com.mipt.app.database.service.departament.DepartamentService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationServiceImpl implements LocationService {

  private LocationRepository locationRepository;

  private DepartamentService departamentService;

  @Autowired
  public LocationServiceImpl(
      LocationRepository locationRepository,
      DepartamentService departamentService) {
    this.locationRepository = locationRepository;
    this.departamentService = departamentService;
  }

  @Override
  public Location saveLocation(Location location) {
    return locationRepository.save(location);
  }

  @Override
  public void deleteLocation(Long id) {
    Location location = locationRepository.findOne(id);
    location.getDepartments().forEach(department -> departamentService.deleteDepartamentById(department.getId()));
  }

  @Override
  public Location getById(Long id) {
    return locationRepository.findOne(id);
  }

  @Override
  public List<Location> getByCountry(String countryName) {
    return getAllLocations().stream()
        .filter(location -> location.getCountryName().equals(countryName)).collect(
            Collectors.toList());
  }

  @Override
  public Location getByCity(String cityName) {
    return getAllLocations().stream().filter(location -> location.getCityName().equals(cityName))
        .findFirst().orElse(null);
  }

  @Override
  public List<Location> getAllLocations() {
    List<Location> locations = new ArrayList<>();
    this.locationRepository.findAll().forEach(locations::add);
    return locations;
  }
}
