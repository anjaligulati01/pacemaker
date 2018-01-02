package controllers;

import io.javalin.Context;
import models.Activity;
import models.Location;
import models.User;

import static models.Fixtures.users;

public class PacemakerRestService {

  PacemakerAPI pacemaker = new PacemakerAPI();

  PacemakerRestService() {
    users.forEach(
        user -> pacemaker.createUser(user.firstname, user.lastname, user.email, user.password));
    
  }

  public void listUsers(Context ctx) {
    ctx.json(pacemaker.getUsers());
    System.out.println("list users requested");
  }
  
  public void createUser(Context ctx) {
    User user = ctx.bodyAsClass(User.class);
    User newUser = pacemaker
        .createUser(user.firstname, user.lastname, user.email, user.password);
    ctx.json(newUser);
  }
  
  public void listUser(Context ctx) {
    String id = ctx.param("id");
    ctx.json(pacemaker.getUser(id));
  }
  
  public void getActivities(Context ctx) {
    String id = ctx.param("id");
    User user = pacemaker.getUser(id);
    if (user != null) {
      ctx.json(user.activities.values());
    } else {
      ctx.status(404);
    }
  }

  public void createActivity(Context ctx) {
    String id = ctx.param("id");
    User user = pacemaker.getUser(id);
    if (user != null) {
      Activity activity = ctx.bodyAsClass(Activity.class);
      Activity newActivity = pacemaker
          .createActivity(id, activity.type, activity.location, activity.distance);
      ctx.json(newActivity);
    } else {
      ctx.status(404);
    }
  }
  
  public void getActivity(Context ctx) {
    String id = ctx.param("activityId");
    Activity activity = pacemaker.getActivity(id);
    if (activity != null) {
      ctx.json(activity);
    } else {
      ctx.status(404);
    }
  }
  
  public void getActivityLocations(Context ctx) {
    String id = ctx.param("activityId");
    System.out.println("Printing activity locations for: " +id);
    Activity activity = pacemaker.getActivity(id);
    if (activity != null) {
      ctx.json(activity.route);
    } else {
      ctx.status(404);
    }
  }

  public void addLocation(Context ctx) {
    String id = ctx.param("activityId");
    Activity activity = pacemaker.getActivity(id);
    if (activity != null) {
      Location location = ctx.bodyAsClass(Location.class);
      activity.route.add(location);
      ctx.json(location);
    } else {
      ctx.status(404);
    }
  }
  
  public void deletetUser(Context ctx) {
    String id = ctx.param("id");
    ctx.json(pacemaker.deleteUser(id));
  }

  public void deleteUsers(Context ctx) {
    pacemaker.deleteUsers();
    ctx.json(204);
  }
  
  public void deleteActivities(Context ctx) {
    String id = ctx.param("id");
    pacemaker.deleteActivities(id);
    ctx.json(204);
  }
  
  public void follow(Context ctx) {
    String id = ctx.param("id");
    System.out.println("Follow: " + id);
    String email = ctx.param("email");
    System.out.println("Follow: " + email);
    if(pacemaker.getUserByEmail(email) != null) {
      pacemaker.follow(id, email);
    }
  }
  
  public void listFriends(Context ctx) {
    String id = ctx.param("id");
    User user = pacemaker.getUser(id);
    if(user != null) {
      ctx.json(pacemaker.listFriends(id));
    }
    else {
      ctx.status(404);
    }  
  }
  
  public void friendActivityReport(Context ctx) {
    String id = ctx.param("id");
    String email = ctx.param("email");
    User user = pacemaker.getUser(id);
    if (user != null) {
      ctx.json(pacemaker.friendActivityReport(id, email, null));
    }
    else {
      ctx.status(404);
    }  
  }
  
  public void friendActivityReportByType(Context ctx) {
    String id = ctx.param("id");
    String email = ctx.param("email");
    String type = ctx.param("type");
    User user = pacemaker.getUser(id);
    if (user != null) {
      ctx.json(pacemaker.friendActivityReport(id, email, type));
    }
    else {
      ctx.status(404);
    }  
  }
  
  public void unfollowFriend(Context ctx) {
    String id = ctx.param("id");
    String email = ctx.param("email");
    pacemaker.unfollowFriend(id, email);
    ctx.json(204);
  }
  
  public void unfollowFriends(Context ctx) {
    String id = ctx.param("id");
    //String email = ctx.param("email");
    pacemaker.unfollowFriends(id);
    ctx.json(204);
  }
  
  public void getActivityReport(Context ctx) {
    String id = ctx.param("id");
    System.out.println("getActivityReport of user " + id);
    ctx.json(pacemaker.listActivities(id, "type"));
  }
  
  public void getActivityReportByType(Context ctx) {
    String id = ctx.param("id");
    String type = ctx.param("type");
    System.out.println("getActivityReport of user " + id + ", type:" + type);
    ctx.json(pacemaker.getActivityReportByType(id, type));
  }
  
  public void sendMessageToFriend(Context ctx) {
    String id = ctx.param("id");
    String email = ctx.param("email");
    String message = ctx.bodyAsClass(String.class);
    pacemaker.sendMessageToFriend(id, email, message);
    ctx.json(204);
  }
  
  public void listMessages(Context ctx) {
    String id = ctx.param("id");
    ctx.json(pacemaker.listMessages(id));
  }
  
  public void sendMessageToAllFriends(Context ctx) {
    String id = ctx.param("id");
    String message = ctx.body(); //ctx.bodyAsClass(String.class);
    System.out.println("sending message to all friends sendMessageToAllFriends(): " + message);
    pacemaker.sendMessageToAllFriends(id, message);
    ctx.json(204);
  }
  
  public void getDistanceLeaderBoard(Context ctx) {
    String id = ctx.param("id");
    User user = pacemaker.getUser(id);
    if (user != null) {
      ctx.json(pacemaker.getDistanceLeaderBoard(id));
    }
    else {
      ctx.status(404);
    }  
  }
  
  public void getDistanceLeaderBoardByType(Context ctx) {
    String id = ctx.param("id");
    String type = ctx.param("type");
    User user = pacemaker.getUser(id);
    if (user != null) {
      ctx.json(pacemaker.getDistanceLeaderBoardByType(id, type));
    }
    else {
      ctx.status(404);
    }  
  }
  
  public void getLocationLeaderBoard(Context ctx) {
    String id = ctx.param("id");
    String loc = ctx.param("loc");
    User user = pacemaker.getUser(id);
    if (user != null) {
      ctx.json(pacemaker.getLocationLeaderBoard(id, loc));
    }
    else {
      ctx.status(404);
    }  
  }
  
  public void disableUser(Context ctx) {
    String email = ctx.param("email");
    pacemaker.disableUser(email);
    ctx.json(204);  
  }
  
  public void enableUser(Context ctx) {
    String email = ctx.param("email");
    pacemaker.enableUser(email);
    ctx.json(204);  
  }
  
}