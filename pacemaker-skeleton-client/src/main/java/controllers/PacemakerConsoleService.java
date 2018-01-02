package controllers;

import com.google.common.base.Optional;

import asg.cliche.Command;
import asg.cliche.Param;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import models.Activity;
import models.Location;
import models.User;
import parsers.AsciiTableParser;
import parsers.Parser;

public class PacemakerConsoleService {

  //private PacemakerAPI paceApi = new PacemakerAPI("https://warm-island-21310.herokuapp.com/");
  private PacemakerAPI paceApi = new PacemakerAPI("http://localhost:7000/");
  private Parser console = new AsciiTableParser();
  private User loggedInUser = null;
  private User adminUser = null;
  
  public PacemakerConsoleService() {
  }

  // Starter Commands

  @Command(description = "Register: Create an account for a new user")
  public void register(@Param(name = "first name") String firstName,
      @Param(name = "last name") String lastName,
      @Param(name = "email") String email, @Param(name = "password") String password) {
    console.renderUser(paceApi.createUser(firstName, lastName, email, password));
  }

  @Command(description = "List Users: List all users emails, first and last names")
  public void listUsers() {
    console.renderUsers(paceApi.getUsers());
  }

  @Command(description = "Login: Log in a registered user in to pacemaker")
  public void login(@Param(name = "email") String email,
      @Param(name = "password") String password) {
    Optional<User> user = Optional.fromNullable(paceApi.getUserByEmail(email));
    if (user.isPresent()) {
      if(!user.get().disabled) {
        if (user.get().password.equals(password)) {
          //cannot login a disabled user
          if(loggedInUser == null && adminUser == null) {//whoever logs in for the first time becomes the admin
            adminUser = user.get();
            System.out.println(adminUser.email +" is now an admin");
          }
          loggedInUser = user.get();
          console.println("Logged in " + loggedInUser.email);
          console.println("ok");
        } else {
          console.println("Error on login");
        }
      }
      else {
        console.println("LOGIN ERROR: User is disabled");     
      }
    }
  }

  @Command(description = "Logout: Logout current user")
  public void logout() {
    console.println("Logging out " + loggedInUser.email);
    console.println("ok");
    loggedInUser = null;
  }

  @Command(description = "Add activity: create and add an activity for the logged in user")
  public void addActivity(
      @Param(name = "type") String type,
      @Param(name = "location") String location,
      @Param(name = "distance") double distance) {
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if (user.isPresent()) {
      console
          .renderActivity(paceApi.createActivity(user.get().id, type, location, distance));
    }
  }

  @Command(description = "List Activities: List all activities for logged in user")
  public void listActivities() {
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if (user.isPresent()) {
      console
          .renderActivities(paceApi.getActivities(user.get().id));
    }
  }

  // Baseline Commands

  @Command(description = "Add location: Append location to an activity")
  public void addLocation(@Param(name = "activity-id") String id,
      @Param(name = "longitude") double longitude, @Param(name = "latitude") double latitude) {
    Optional<Activity> activity = Optional.fromNullable(paceApi.getActivity(loggedInUser.getId(), id));
    if (activity.isPresent()) {
      paceApi.addLocation(loggedInUser.getId(), activity.get().id, latitude, longitude);
      console.println("ok");
    } else {
      console.println("not found");
    }
  }
  
  

  @Command(description = "ActivityReport: List all activities for logged in user, sorted alphabetically by type")
  public void activityReport() {
    Optional<User> user = Optional.fromNullable(loggedInUser);
    System.out.println("logged in user: " + user.get().email);
    if(user.isPresent()) {
      console.renderActivities(paceApi.listActivities(user.get().id));
    }
  }

  @Command(description = "Activity Report: List all activities for logged in user by type. Sorted longest to shortest distance")
  public void activityReport(@Param(name = "byType: type") String type) {
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if(user.isPresent()) {
      Collection<Activity> usersActivities = paceApi.getActivities(user.get().id);
      List<Activity> resultActivities = new ArrayList<Activity>();
      usersActivities.forEach(a -> {
        if(a.type.equals(type))
          resultActivities.add(a);
      });
      resultActivities.sort((a1, a2) -> {
        if (a1.distance >= a2.distance)
          return -1;
        else
          return 1;
      });
      console.renderActivities(resultActivities);
    }
  }

  @Command(description = "List all locations for a specific activity")
  public void listActivityLocations(@Param(name = "activity-id") String id) {
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if(user.isPresent()) {
      Optional<Activity> activity = Optional.fromNullable(paceApi.getActivity(user.get().id, id));
      if (activity.isPresent()) {
        console.renderLocations(paceApi.getActivityLocations(activity.get().id));
      }
    }
  }

  @Command(description = "Follow Friend: Follow a specific friend")
  public void follow(@Param(name = "email") String email) {
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if(user.isPresent()) {
      paceApi.follow(user.get().id, email);
    }
  }

  @Command(description = "List Friends: List all of the friends of the logged in user")
  public void listFriends() {
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if(user.isPresent()) {
      console.renderUsers(paceApi.listFriends(user.get().id));
    }
  }

  @Command(description = "Friend Activity Report: List all activities of specific friend, sorted alphabetically by type)")
  public void friendActivityReport(@Param(name = "email") String email) {
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if(user.isPresent()) {
      console.renderActivities(paceApi.listActivitiesOfFriend(user.get().id, email));
    }
  }

  // Good Commands

  @Command(description = "Unfollow Friends: Stop following a friend")
  public void unfollowFriend(@Param(name = "email") String email) {
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if(user.isPresent()) {
      paceApi.unfollowFriend(user.get().id, email);
    }
  }

  @Command(description = "Message Friend: send a message to a friend")
  public void messageFriend(@Param(name = "email") String email,
      @Param(name = "message") String message) {
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if(user.isPresent()) {
      paceApi.sendMessageToFriend(user.get().id, email, message);
    }
  }

  @Command(description = "List Messages: List all messages for the logged in user")
  public void listMessages() {
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if(user.isPresent()) {
      console.renderMessages(paceApi.listMessages(user.get().id));
    }
  }

  @Command(description = "Distance Leader Board: list summary distances of all friends, sorted longest to shortest")
  public void distanceLeaderBoard() {
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if(user.isPresent()) {
      console.renderActivities(paceApi.getDistanceLeaderBoard(user.get().id));
    }
  }

  // Excellent Commands

  @Command(description = "Distance Leader Board: distance leader board refined by type")
  public void distanceLeaderBoardByType(@Param(name = "byType: type") String type) {
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if(user.isPresent()) {
      console.renderActivities(paceApi.getDistanceLeaderBoardByType((user.get().id), type));
    }
  }

  @Command(description = "Message All Friends: send a message to all friends")
  public void messageAllFriends(@Param(name = "message") String message) {
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if(user.isPresent()) {
      System.out.println("sending message to all friends");
      paceApi.sendMessageToAllFriends(user.get().id, message);
    }
  }

  @Command(description = "Location Leader Board: list sorted summary distances of all friends in named location")
  public void locationLeaderBoard(@Param(name = "location") String loc) {
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if(user.isPresent()) {
      console.renderActivities(paceApi.getLocationLeaderBoard(user.get().id, loc));
    }
  }

  // Outstanding Commands
  /*
   * Assumptions:
   * - the first user to login is the admin -> code in login method
   * - There is only 1 admin at a time
   * - admin can make another user an admin, in which case he's no longer an admin
   */
  
  @Command(description = "Register As Admin: Mark user as admin")
  public void registerAsAdmin(@Param(name = "email") String email) {
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if(user.isPresent()) {
      if(user.get().id.equals(adminUser.id))
        adminUser = paceApi.getUserByEmail(email);
    }
  }
  
  //any user can invoke the command to see the current admin
  @Command(description = "Get Admin User: Get admin user details")
  public void getAdminUser() {
    Optional<User> user = Optional.fromNullable(adminUser);
    if(user.isPresent()) {
        console.renderUser(paceApi.getUser(adminUser.id));
    }
  }

  @Command(description = "Remove User: Remove a user")
  public void removeUser(@Param(name = "email") String email) {
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if(user.isPresent()) {
      if(user.get().id.equals(adminUser.id)) {
        paceApi.deleteUser(paceApi.getUserByEmail(email).id);
      }
      else {
        System.out.println("Only admin has right to delete a user!");
      }
    }
  }
  
  @Command(description = "Disable User: Disable a user")
  public void disableUser(@Param(name = "email") String email) {
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if(user.isPresent()) {
      if(user.get().id.equals(adminUser.id)) {
        if(paceApi.getUserByEmail(email).id.equals(adminUser.id)) {
          console.println("Cannot disable the admin user");
        }
        else {
          paceApi.disableUser(email);
        }       
      }
      else {
        System.out.println("Only admin has right to disable a user!");
      }
    }
  }
  
  @Command(description = "Enable User: Disable a user")
  public void enableUser(@Param(name = "email") String email) {
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if(user.isPresent()) {
      if(user.get().id.equals(adminUser.id)) {
        paceApi.enableUser(email);
      }
      else {
        System.out.println("Only admin has right to enable a user!");
      }
    }
  }
  
}