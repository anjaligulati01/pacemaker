package models;

import static com.google.common.base.MoreObjects.toStringHelper;
import java.io.Serializable;
import com.google.common.base.Objects;

public class Location implements Serializable {

  //public String id;
  public double longitude;
  public double latitude;

  public Location() {
  }
  
  public double getLongitude() {
    return longitude;
  }

  public double getLatitude() {
    return latitude;
  }

  public Location(double latitude, double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }


  @Override
  public boolean equals(final Object obj) {
    if (obj instanceof Location) {
      final Location other = (Location) obj;
      return Objects.equal(latitude, other.latitude)
          && Objects.equal(longitude, other.longitude);
    } else {
      return false;
    }
  }

  @Override
  public String toString() {
    return toStringHelper(this)
        .addValue(latitude)
        .addValue(longitude)
        .toString();
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.latitude, this.longitude);
  }
}