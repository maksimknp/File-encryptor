package com.mipt.app.database.service.location;

import com.mipt.app.database.model.location.Location;
import java.util.List;

public interface LocationService  {

  public Location saveLocation(Location location);

  public void deleteLocation(Long id);

  public Location getById(Long id);

  public List<Location> getByCountry(String countryName);

  public Location getByCity(String cityName);

  public List<Location> getAllLocations();
}
