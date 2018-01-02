package parsers;

import com.bethecoder.ascii_table.ASCIITable;
import com.bethecoder.ascii_table.impl.CollectionASCIITableAware;
import com.bethecoder.ascii_table.spec.IASCIITableAware;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import models.Activity;
import models.Location;
import models.User;

public class AsciiTableParser extends Parser {

  public void renderUser(User user) {
    if (user != null) {
      renderUsers(Arrays.asList(user));
      System.out.println("ok");
    } else {
      System.out.println("not found");
    }
  }

  public void renderUsers(Collection<User> users) {
    if (users != null) {
      if (!users.isEmpty()) {
        List<User> userList = new ArrayList<User>(users);
        IASCIITableAware asciiTableAware = new CollectionASCIITableAware<User>(userList, "id",
            "firstname",
            "lastname", "email");
        System.out.println(ASCIITable.getInstance().getTable(asciiTableAware));
      }
      System.out.println("ok");
    } else {
      System.out.println("not found");
    }
  }

  public void renderActivity(Activity activity) {
    if (activity != null) {
      renderActivities(Arrays.asList(activity));
      System.out.println("ok");
    } else {
      System.out.println("not found");
    }
  }

  public void renderActivities(Collection<Activity> activities) {
    if (activities != null) {
      if (!activities.isEmpty()) {
        List<Activity> activityList = new ArrayList(activities);
        IASCIITableAware asciiTableAware = new CollectionASCIITableAware<Activity>(activityList,
            "id",
            "type", "location", "distance", "starttime", "duration");
        System.out.println(ASCIITable.getInstance().getTable(asciiTableAware));
      }
      System.out.println("ok");
    } else {
      System.out.println("not found");
    }
  }

  public void renderLocations(List<Location> locations) {
    if (locations != null) {
      if (!locations.isEmpty()) {
        IASCIITableAware asciiTableAware = new CollectionASCIITableAware<Location>(locations,
            "id",
            "latitude", "longitude");
        System.out.println(ASCIITable.getInstance().getTable(asciiTableAware));
      }
      System.out.println("ok");
    } else {
      System.out.println("not found");
    }
  }
  
  public void renderMessages(List<String> messages) {
    if (messages != null) {
      if (!messages.isEmpty()) {
        IASCIITableAware asciiTableAware = new CollectionASCIITableAware<String>(messages,
            "message");
        System.out.println(ASCIITable.getInstance().getTable(asciiTableAware));
      }
      System.out.println("ok");
    } else {
      System.out.println("not found");
    }
  }
}