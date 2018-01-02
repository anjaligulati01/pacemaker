package models;

import static com.google.common.base.MoreObjects.toStringHelper;
import java.io.Serializable;
import com.google.common.base.Objects;

public class Activity implements Serializable {

  public String id;
  public String type;
  public String location;
  public double distance;

  public Activity() {
  }

  public Activity(String type, String location, double distance) {
    this.type = type;
    this.location = location;
    this.distance = distance;
  }

  public String getId() {
    return id;
  }

  public String getType() {
    return type;
  }

  public String getLocation() {
    return location;
  }

  public String getDistance() {
    return Double.toString(distance);
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj instanceof Activity) {
      final Activity other = (Activity) obj;
      return Objects.equal(type, other.type)
          && Objects.equal(location, other.location)
          && Objects.equal(distance, other.distance);
    } else {
      return false;
    }
  }

  @Override
  public String toString() {
    return toStringHelper(this).addValue(id)
        .addValue(type)
        .addValue(location)
        .addValue(distance)
        .toString();
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.id, this.type, this.location, this.distance);
  }
}