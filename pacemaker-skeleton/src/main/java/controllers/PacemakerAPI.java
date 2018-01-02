package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.base.Optional;

import models.Activity;
import models.Location;
import models.User;


public class PacemakerAPI {

  private Map<String, User> emailIndex = new HashMap<>();
  private Map<String, User> userIndex = new HashMap<>();
  private Map<String, Activity> activitiesIndex = new HashMap<>();

  public PacemakerAPI() {
  }

  public Collection<User> getUsers() {
    return userIndex.values();
  }

  public void deleteUsers() {
    userIndex.clear();
    emailIndex.clear();
  }

  public User createUser(String firstName, String lastName, String email, String password) {
    User user = new User(firstName, lastName, email, password);
    emailIndex.put(email, user);
    userIndex.put(user.id, user);
    return user;
  }

  public Activity createActivity(String id, String type, String location, double distance) {
    Activity activity = null;
    Optional<User> user = Optional.fromNullable(userIndex.get(id));
    if (user.isPresent()) {
      activity = new Activity(type, location, distance);
      user.get().activities.put(activity.id, activity);
      activitiesIndex.put(activity.id, activity);
    }
    return activity;
  }

  public Activity getActivity(String id) {
    return activitiesIndex.get(id);
  }

  public Collection<Activity> getActivities(String id) {
    Collection<Activity> activities = null;
    Optional<User> user = Optional.fromNullable(userIndex.get(id));
    if (user.isPresent()) {
      activities = user.get().activities.values();
    }
    return activities;
  }
  
  public List<Activity> sortActivities(List<Activity> activities, String sortBy) {
    switch (sortBy) {
    case "type":
      activities.sort((a1, a2) -> a1.type.compareTo(a2.type));
      break;
    case "location":
      activities.sort((a1, a2) -> a1.location.compareTo(a2.location));
      break;
    case "distance":
      activities.sort((a1, a2) -> Double.compare(a1.distance, a2.distance));
      break;
    }
    return activities;
  }

  public List<Activity> listActivities(String userId, String sortBy) {
    List<Activity> activities = new ArrayList<>();
    activities.addAll(userIndex.get(userId).activities.values());
    /*switch (sortBy) {
      case "type":
        activities.sort((a1, a2) -> a1.type.compareTo(a2.type));
        break;
      case "location":
        activities.sort((a1, a2) -> a1.location.compareTo(a2.location));
        break;
      case "distance":
        activities.sort((a1, a2) -> Double.compare(a1.distance, a2.distance));
        break;
    }*/
    return sortActivities(activities, sortBy);
    
  }

  public void addLocation(String id, double latitude, double longitude) {
    Optional<Activity> activity = Optional.fromNullable(activitiesIndex.get(id));
    if (activity.isPresent()) {
      activity.get().route.add(new Location(latitude, longitude));
    }
  }

  public User getUserByEmail(String email) {
    return emailIndex.get(email);
  }

  public User getUser(String id) {
    return userIndex.get(id);
  }

  public User deleteUser(String id) {
    User user = userIndex.remove(id);
    return emailIndex.remove(user.email);
  }
  
  public void deleteActivities(String id) {
    Optional<User> user = Optional.fromNullable(userIndex.get(id));
    if (user.isPresent()) {
      user.get().activities.values().forEach(activity -> activitiesIndex.remove(activity.getId()));
      user.get().activities.clear();
    }
  }
  
  public void follow(String id, String email) {
    Optional<User> user = Optional.fromNullable(userIndex.get(id));
    if(user.isPresent()) {
      user.get().friends.add(email);
    }
  }
  
  public List<User> listFriends(String id) {
    List<User> friendsList = new ArrayList<User>();
    Optional<User> user = Optional.fromNullable(userIndex.get(id));
    if(user.isPresent()) {
      user.get().friends.forEach(friend -> friendsList.add(getUserByEmail(friend)));
    }
    return friendsList;
  }
  
  public List<Activity> friendActivityReport(String id, String email, String sortByType){
    List<Activity> activityList = new ArrayList<Activity>();
    Optional<User> user = Optional.fromNullable(userIndex.get(id));
    if(user.isPresent()) {
      if(user.get().friends.contains(email)) {//email is one of the friends of user
        User userFriend = getUserByEmail(email);
        if(sortByType != null)
          activityList = listActivities(userFriend.getId(), sortByType);
        else
          activityList = listActivities(userFriend.getId(), "type");
      }
    }
    return activityList;
  }
  
  public void unfollowFriend(String id, String email) {
    Optional<User> user = Optional.fromNullable(userIndex.get(id));
    if(user.isPresent()) {
      if(user.get().friends.contains(email)) {//email is one of the friends of user
        user.get().friends.remove(email);
      }
    }
  }
  
  public void unfollowFriends(String id) {
    Optional<User> user = Optional.fromNullable(userIndex.get(id));
    if(user.isPresent()) {
      
        user.get().friends.clear();

    }
  }
    public List<Activity> getActivityReportByType(String id, String type){
      List<Activity> filteredActivities = new ArrayList<Activity>();
      Optional<User> user = Optional.fromNullable(userIndex.get(id));
      if(user.isPresent()) {
        List<Activity> sortedActivities = listActivities(id, "distance");
        filteredActivities = sortedActivities.stream().filter(a -> a.type.equals(type)).collect(Collectors.toList());
      }
      return filteredActivities;
    }
    
    public void sendMessageToFriend(String id, String email, String message) {
      Optional<User> user = Optional.fromNullable(userIndex.get(id));
      if(user.isPresent()) {
        if(user.get().friends.contains(email)) {
          List<String> msgList = getUserByEmail(email).messagesFromFriends.get(user.get().email);
          if(msgList != null) {// some messages from user already exists in the map
            msgList.add(message);
            getUserByEmail(email).messagesFromFriends.put(user.get().email, msgList);
          }
          else {//1st message being posted
            msgList = new ArrayList<String>();
            msgList.add(message);
            getUserByEmail(email).messagesFromFriends.put(user.get().email, msgList);
          }
        }
      }
    }
    
    public List<String> listMessages(String id) {
      List<String> messages = new ArrayList<String>();
      Optional<User> user = Optional.fromNullable(userIndex.get(id));
      if(user.isPresent()) {
        for (Map.Entry<String, List<String>> entry : user.get().messagesFromFriends.entrySet()) {
          messages.addAll(entry.getValue());
        }
        return messages;
      }
      else return null;
    }
    
    public void sendMessageToAllFriends(String id, String message) {
      Optional<User> user = Optional.fromNullable(userIndex.get(id));
      if(user.isPresent()) {
        user.get().friends.forEach(f -> {
          System.out.println("Message friend: " + f);
          /*List<String> msgList = getUserByEmail(f).messagesFromFriends.get(user.get().email); 
          if(msgList != null) {// some messages from user already exists in the map
            System.out.println("Unempty messageList from user: " + user.get().email);
            msgList.add(message);
            getUserByEmail(f).messagesFromFriends.put(user.get().email, msgList);
          }
          else {//1st message being posted
            System.out.println("Empty messageList from user: " + user.get().email);
            msgList = new ArrayList<String>();
            msgList.add(message);
            getUserByEmail(f).messagesFromFriends.put(user.get().email, msgList);
          }*/
          sendMessageToFriend(user.get().id, f, message);
        });
      }
    }
    
    public List<Activity> getDistanceLeaderBoard(String id){
      List<Activity> activities = new ArrayList<Activity>();
      //Stream<Activity> streamActivities = Stream.of();
      Optional<User> user = Optional.fromNullable(userIndex.get(id));
      if(user.isPresent()) {
        user.get().friends.forEach(f -> {
           //getUserByEmail(f).activities.values() call getActivities method
          activities.addAll(getUserByEmail(f).activities.values());
          //streamActivities = Stream.concat(streamActivities, getUserByEmail(f).activities.values().stream());
        });
        
        activities.sort((a1, a2) -> Double.compare(a2.distance, a1.distance));
        //sortActivities(activities, "distance");
      }
      return activities;
    }
    
    public List<Activity> getDistanceLeaderBoardByType(String id, String type){
      List<Activity> activities = new ArrayList<Activity>();
      //Stream<Activity> streamActivities = Stream.of();
      Optional<User> user = Optional.fromNullable(userIndex.get(id));
      if(user.isPresent()) {
        List<Activity> allActivities = new ArrayList<Activity>();
        user.get().friends.forEach(f -> {
           //getUserByEmail(f).activities.values() call getActivities method
          allActivities.addAll(getUserByEmail(f).activities.values());
          //streamActivities = Stream.concat(streamActivities, getUserByEmail(f).activities.values().stream());
        });
        activities = allActivities.stream().filter(a -> a.type.equals(type)).collect(Collectors.toList());
      }
      return activities;
    }
    
    public List<Activity> getLocationLeaderBoard(String id, String location){
      List<Activity> activities = new ArrayList<Activity>();
      //Stream<Activity> streamActivities = Stream.of();
      Optional<User> user = Optional.fromNullable(userIndex.get(id));
      if(user.isPresent()) {
        System.out.println("Getting location leader board for "  + user.get().email);
        List<Activity> allActivities = new ArrayList<Activity>();
        user.get().friends.forEach(f -> {
          allActivities.addAll(getUserByEmail(f).activities.values());
        });
        activities = sortActivities(allActivities, "distance").stream().filter(a -> a.location.equals(location)).collect(Collectors.toList());
      }
      return activities;
    }
    
    //Outstanding admin methods
    public void disableUser(String email) {
      Optional<User> user = Optional.fromNullable(userIndex.get(getUserByEmail(email).id));
      if(user.isPresent()) {
        user.get().disabled = true;
      }
    }
    
    public void enableUser(String email) {
      Optional<User> user = Optional.fromNullable(userIndex.get(getUserByEmail(email).id));
      if(user.isPresent()) {
        user.get().disabled = false; 
      }
    }
    
    
    
}