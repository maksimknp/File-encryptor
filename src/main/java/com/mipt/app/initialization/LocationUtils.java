package com.mipt.app.initialization;

import com.mipt.app.database.model.location.Location;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class LocationUtils {

  protected static List<Location> createSomeLocations(){
    return new ArrayList<Location>(){{
      add(new Location("Russia", "Moscow"));
      add(new Location("Belarus", "Minsk"));
    }};
  }
}
