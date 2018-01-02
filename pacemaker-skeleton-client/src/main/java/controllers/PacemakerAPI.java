package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import models.Activity;
import models.Location;
import models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

interface PacemakerInterface
{
  @GET("/users")
  Call<List<User>> getUsers();
  @GET("/users/{email}/")
  Call<List<User>> getUserByEmail(@Path("email") String email);
  @POST("/users")
  Call<User> registerUser(@Body User User);
  
  @GET("/users/{id}/activities")
  Call<List<Activity>> getActivities(@Path("id") String id);

  @POST("/users/{id}/activities")
  Call<Activity> addActivity(@Path("id") String id, @Body Activity activity);
  
  @GET("/users/{id}/activities/{activityId}")
  Call<Activity> getActivity(@Path("id") String id, @Path("activityId") String activityId);
  
  @POST("/users/{id}/activities/{activityId}/locations")
  Call<Location> addLocation(@Path("id") String id, @Path("activityId") String activityId, @Body Location location);
  
  @DELETE("/users")
  Call<User> deleteUsers();

  @DELETE("/users/{id}")
  Call<User> deleteUser(@Path("id") String id);

  @GET("/users/{id}")
  Call<User> getUser(@Path("id") String id);
  
  @DELETE("/users/{id}/activities")
  Call<String> deleteActivities(@Path("id") String id);
  
  @GET("/users/{id}/activities/{activityId}/locations")
  Call<List<Location>> getLocations(@Path("id") String id, @Path("activityId") String activityId);
  
  @GET("/activities/{activityId}/locations")
  Call<List<Location>> getActivityLocations(@Path("activityId") String activityId);
  
  @POST("/users/{id}/friend/{email}")
  Call<String> follow(@Path("id") String id, @Path("email") String email);
  
  @GET("/users/{id}/messages")
  Call<List<String>> listMessages(@Path("id") String id);
  
  @GET("/users/{id}/friends")
  Call<List<User>> listFriends(@Path("id") String id);
  
  @GET("/users/{id}/activityReport")
  Call<List<Activity>> listActivities(@Path("id") String id);
  
  @GET("/users/{id}/friends/{email}/activities")
  Call<List<Activity>> listActivitiesOfFriend(@Path("id") String id, @Path("email") String email);
  
  @DELETE("/users/{id}/friends/{email}")
  Call<String> unfollowFriend(@Path("id") String id, @Path("email") String email);
  
  @POST("/users/{id}/friend/{email}/message")
  Call<String> sendMessageToFriend(@Path("id") String id, @Path("email") String email, @Body String message);
  
  @POST("/users/{id}/friends/message")
  Call<String> sendMessageToAllFriends(@Path("id") String id, @Body String message);
  
  @GET("/users/{id}/dlb")
  Call<List<Activity>> getDistanceLeaderBoard(@Path("id") String id);
  
  @GET("/users/{id}/dlb/{type}")
  Call<List<Activity>> getDistanceLeaderBoardByType(@Path("id") String id, @Path("type") String type);
  
  @GET("/users/{id}/llb/{loc}")
  Call<List<Activity>> getLocationLeaderBoard(@Path("id") String id, @Path("loc") String loc);
  
  @POST("/user/disable/{email}")
  Call<String> disableUser(@Path("email") String email);
  
  @POST("/user/enable/{email}")
  Call<String> enableUser(@Path("email") String email);
  
 }

public class PacemakerAPI {

  PacemakerInterface pacemakerInterface;

  public PacemakerAPI(String url) {
    Gson gson = new GsonBuilder().create();
    Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create(gson)).build();
    pacemakerInterface = retrofit.create(PacemakerInterface.class);
  }

  public Collection<User> getUsers() {
    Collection<User> users = null;
    try {
      Call<List<User>> call = pacemakerInterface.getUsers();
      Response<List<User>> response = call.execute();
      users = response.body();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return users;
  }

  public User createUser(String firstName, String lastName, String email, String password) {
    User returnedUser = null;
    try {
      Call<User> call = pacemakerInterface.registerUser(new User(firstName, lastName, email, password));
      Response<User> response = call.execute();
      returnedUser = response.body();    
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return returnedUser;
  }

  public Activity createActivity(String id, String type, String location, double distance) {
    Activity returnedActivity = null;
    try {
      Call<Activity> call = pacemakerInterface.addActivity(id, new Activity(type, location, distance));
      Response<Activity> response = call.execute();
      returnedActivity = response.body();    
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return returnedActivity;
  }

  //...

  public Collection<Activity> getActivities(String id) {
    Collection<Activity> activities = null;
    try {
      Call<List<Activity>> call = pacemakerInterface.getActivities(id);
      Response<List<Activity>> response = call.execute();
      activities = response.body();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return activities;
  }
  
  public Activity getActivity(String id) {
    return null;
  }
  
  public Activity getActivity(String userId, String activityId) {
    Activity activity = null;
     try {
       Call<Activity> call = pacemakerInterface.getActivity(userId, activityId);
       Response<Activity> response = call.execute();
       activity = response.body();
     } catch (Exception e) {
       System.out.println(e.getMessage());
     }
     return activity;
   }

  public void addLocation(String id, String activityId, double latitude, double longitude) {
    try {
      Call<Location> call = pacemakerInterface.addLocation(id, activityId, new Location(latitude, longitude));
      call.execute();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public User getUserByEmail(String email) { //to change NOTE
    Collection<User> users = getUsers();
    User foundUser = null;
    for (User user : users) {
      if(user.email.equals(email))
        foundUser = user;
    }
    return foundUser;
    /*Stream<User> sUsers = users.stream().filter(user -> user.email.equals(email));
    return sUsers.collect(Collectors.toList()).get(0);*/
  }


  public User getUser(String id) {
    User user = null;
    try {
      Call<User> call = pacemakerInterface.getUser(id);
      Response<User> response = call.execute();
      user = response.body();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return user;
  }

  public void deleteUsers() {
    try {
      Call<User> call = pacemakerInterface.deleteUsers();
      call.execute();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public User deleteUser(String id) {
    User user = null;
    try {
      Call<User> call = pacemakerInterface.deleteUser(id);
      Response<User> response = call.execute();
      user = response.body();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return user;
  }
  
  public void deleteActivities(String id) {
    try {
      Call<String> call = pacemakerInterface.deleteActivities(id);
      call.execute();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
  
  public List<Location> getLocations(String id, String activityId) {
    List<Location> locations = null;
    try {
      Call<List<Location>> call = pacemakerInterface.getLocations(id, activityId);
      Response<List<Location>> response = call.execute();
      locations = response.body();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return locations;
  }
  
  public List<Location> getActivityLocations(String activityId) {
    List<Location> locations = null;
    try {
      System.out.println("Getting locations for activity: " + activityId);
      Call<List<Location>> call = pacemakerInterface.getActivityLocations(activityId);
      Response<List<Location>> response = call.execute();
      locations = response.body();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return locations;
  }
  
  public void follow(String id, String email) {
    try {
      Call<String> call = pacemakerInterface.follow(id, email);
      call.execute();
    }
    catch(Exception e) {
      System.out.println(e.getMessage());
    }
  }
  
  public Collection<String> listMessages(String id){
    List<String> messages = null;
    try {
      Call<List<String>> call = pacemakerInterface.listMessages(id);
      Response<List<String>> response = call.execute();
      messages = response.body();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return messages;
  }
  
  public Collection<User> listFriends(String id){
    List<User> friends = null;
    try {
      Call<List<User>> call = pacemakerInterface.listFriends(id);
      Response<List<User>> response = call.execute();
      friends = response.body();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return friends;
  }
  
  public Collection<Activity> listActivities(String id) {
    Collection<Activity> activities = null;
    try {
      Call<List<Activity>> call = pacemakerInterface.listActivities(id);
      Response<List<Activity>> response = call.execute();
      activities = response.body();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return activities;
  }
  
  public Collection<Activity> listActivitiesOfFriend(String id, String email) {
    Collection<Activity> activities = null;
    try {
      Call<List<Activity>> call = pacemakerInterface.listActivitiesOfFriend(id, email);
      Response<List<Activity>> response = call.execute();
      activities = response.body();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return activities;
  }
  
  public void unfollowFriend(String id, String email) {
    try {
      Call<String> call = pacemakerInterface.unfollowFriend(id, email);
      call.execute();
    }
    catch(Exception e) {
      System.out.println(e.getMessage());
    }
  }
  
  public void sendMessageToFriend(String id, String email, String message) {
    try {
      Call<String> call = pacemakerInterface.sendMessageToFriend(id, email, message);
      call.execute();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
  
  public void sendMessageToAllFriends(String id, String message) {
    try {
      Call<String> call = pacemakerInterface.sendMessageToAllFriends(id, message);
      call.execute();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
  
  public Collection<Activity> getDistanceLeaderBoard(String id) {
    Collection<Activity> activities = null;
    try {
      Call<List<Activity>> call = pacemakerInterface.getDistanceLeaderBoard(id);
      Response<List<Activity>> response = call.execute();
      activities = response.body();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return activities;
  }
  
  public Collection<Activity> getDistanceLeaderBoardByType(String id, String type) {
    Collection<Activity> activities = null;
    try {
      Call<List<Activity>> call = pacemakerInterface.getDistanceLeaderBoardByType(id, type);
      Response<List<Activity>> response = call.execute();
      activities = response.body();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return activities;
  }
  
  public Collection<Activity> getLocationLeaderBoard(String id, String loc) {
    Collection<Activity> activities = null;
    try {
      Call<List<Activity>> call = pacemakerInterface.getLocationLeaderBoard(id, loc);
      Response<List<Activity>> response = call.execute();
      activities = response.body();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return activities;
  }
  
  public void disableUser(String email) {
   try {
     Call<String> call = pacemakerInterface.disableUser(email);
     call.execute();
   }
    catch (Exception e) {
    System.out.println(e.getMessage());
    }
  }
  
  public void enableUser(String email) {
    try {
      Call<String> call = pacemakerInterface.enableUser(email);
      call.execute();
    }
     catch (Exception e) {
     System.out.println(e.getMessage());
     }
   }
  
}