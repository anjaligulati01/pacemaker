package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import models.Activity;
import models.Location;
import models.User;
import models.Fixtures;

public class ActivityTest {

  PacemakerAPI pacemaker = new PacemakerAPI("https://warm-island-21310.herokuapp.com");
  //PacemakerAPI pacemaker = new PacemakerAPI("http://localhost:7000");
  User homer = new User("homer", "simpson", "homer@simpson.com", "secret");

  @Before
  public void setup() {
    pacemaker.deleteUsers();
    homer = pacemaker.createUser(homer.firstname, homer.lastname, homer.email, homer.password);
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testCreateActivity() {
    Activity activity = new Activity("walk", "shop", 2.5);

    Activity returnedActivity = pacemaker.createActivity(homer.id, activity.type, activity.location, activity.distance);
    assertEquals(activity.type, returnedActivity.type);
    assertEquals(activity.location, returnedActivity.location);
    assertEquals(activity.distance, returnedActivity.distance, 0.001);
    assertNotNull(returnedActivity.id);
  }
  
  @Test
  public void testGetActivity() {
    Activity activity = new Activity("run", "fridge", 0.5);
    Activity returnedActivity1 = pacemaker.createActivity(homer.id, activity.type, activity.location, activity.distance);
    Activity returnedActivity2 = pacemaker.getActivity(homer.id, returnedActivity1.id);
    assertEquals(returnedActivity1, returnedActivity2);
  }

  @Test
  public void testDeleteActivity() {
    Activity activity = new Activity("sprint", "pub", 4.5);
    Activity returnedActivity = pacemaker.createActivity(homer.id, activity.type, activity.location, activity.distance);
    assertNotNull (returnedActivity);
    pacemaker.deleteActivities(homer.id);
    returnedActivity = pacemaker.getActivity(homer.id, returnedActivity.id);
    assertNull (returnedActivity);
  }
  
  @Test
  public void testCreateActivityWithSingleLocation() {
    pacemaker.deleteActivities(homer.id);
    Activity activity = new Activity("walk", "shop", 2.5);
    Location location = new Location(12.0, 33.0);

    Activity returnedActivity = pacemaker.createActivity(homer.id, activity.type, activity.location, activity.distance);
    pacemaker.addLocation(homer.id, returnedActivity.id, location.latitude, location.longitude);

    List<Location> locations = pacemaker.getLocations(returnedActivity.id);
    //System.out.println("Loc size" + locations.size());
    assertEquals (locations.size(), 1);
    assertEquals (locations.get(0), location);
  }
  
  @Test
  public void testCreateActivityWithMultipleLocation() {
    pacemaker.deleteActivities(homer.id);
    Activity activity = new Activity("walk", "shop", 2.5);
    Activity returnedActivity = pacemaker.createActivity(homer.id, activity.type, activity.location, activity.distance);

    Fixtures.locations.forEach (location ->  pacemaker.addLocation(homer.id, returnedActivity.id, location.latitude, location.longitude));
    List<Location> returnedLocations = pacemaker.getActivityLocations(returnedActivity.id);
    assertEquals (Fixtures.locations.size(), returnedLocations.size());
    assertEquals(Fixtures.locations, returnedLocations);
  }
  
  @Test
  public void testDeleteActivities() {
    //Activity activity = new Activity("walk", "shop", 2.5);
    Fixtures.activities.forEach(activity -> 
      pacemaker.createActivity(homer.id, activity.type, activity.location, activity.distance));
    int countActivities = pacemaker.getActivities(homer.id).size();
    assertTrue(countActivities > 0);
    pacemaker.deleteActivities(homer.id);
    countActivities = pacemaker.getActivities(homer.id).size();
    assertEquals(countActivities, 0);
  }
  
  @Test
  public void testListActivities() {
    Fixtures.activities.forEach(activity -> 
    pacemaker.createActivity(homer.id, activity.type, activity.location, activity.distance));
    int countActivities = pacemaker.listActivities(homer.id).size();
    assertTrue(countActivities > 0);
  }
  
  @Test
  public void testListActivitiesOfFriend() {
    Fixtures.activities.forEach(activity -> 
      pacemaker.createActivity(homer.id, activity.type, activity.location, activity.distance));
    User marge = pacemaker.createUser("Marge", "Simpson", "marge@simpson.com", "password");
    pacemaker.follow(marge.id, homer.email);
    int countActivities = pacemaker.listActivitiesOfFriend(marge.id, homer.email).size();
    assertTrue(countActivities > 0);
  }
  
  @Test
  public void testGetDistanceLeaderBoard() {
    Fixtures.users.forEach(
        user -> pacemaker.createUser(user.firstname, user.lastname, user.email, user.password));
    pacemaker.getUsers().forEach(user ->
      Fixtures.activities.forEach(activity -> 
        pacemaker.createActivity(user.id, activity.type, activity.location, activity.distance))
    );
    
    List<User> returnedUsers = (List<User>)pacemaker.getUsers();
    returnedUsers.forEach(user ->
      pacemaker.follow(homer.id, user.email)
     );
    List<Activity> distanceLeaderBoard = (List<Activity>)pacemaker.getDistanceLeaderBoard(homer.id);
    assertTrue(distanceLeaderBoard.get(0).distance >= distanceLeaderBoard.get(5).distance);
  }
  
  @Test
  public void testGetDistanceLeaderBoardByType() {
    Fixtures.users.forEach(
        user -> pacemaker.createUser(user.firstname, user.lastname, user.email, user.password));
    pacemaker.getUsers().forEach(user ->
      Fixtures.activities.forEach(activity -> 
        pacemaker.createActivity(user.id, activity.type, activity.location, activity.distance)) 
    );
    
    List<User> returnedUsers = (List<User>)pacemaker.getUsers();
    returnedUsers.forEach(user ->
      pacemaker.follow(homer.id, user.email)
     );
    List<Activity> distanceLeaderBoard = (List<Activity>)pacemaker.getDistanceLeaderBoardByType(homer.id, "walk");
    assertEquals(distanceLeaderBoard.get(0).type, "walk");
  }
  
  @Test
  public void testLocationLeaderBoard() {
    User margeUser = pacemaker.createUser(Fixtures.users.get(0).getFirstname(), Fixtures.users.get(0).getLastname(),
        Fixtures.users.get(0).email, Fixtures.users.get(0).password);
    User lisaUser = pacemaker.createUser(Fixtures.users.get(1).getFirstname(), Fixtures.users.get(1).getLastname(),
        Fixtures.users.get(1).email, Fixtures.users.get(1).password);
    Fixtures.margeActivities.forEach(activity ->
      pacemaker.createActivity(margeUser.id, activity.type, activity.location,
          activity.distance));
    Fixtures.lisasActivities.forEach(activity ->
    pacemaker.createActivity(lisaUser.id, activity.type, activity.location,
        activity.distance));
    pacemaker.follow(homer.id, margeUser.email);
    pacemaker.follow(homer.id, lisaUser.email);
    
    List<Activity> locationLeaderBoard = (List<Activity>)pacemaker.getLocationLeaderBoard(homer.id, Fixtures.activities.get(1).location);
    assertEquals(locationLeaderBoard.get(0).location, Fixtures.activities.get(1).location);
    
  }
  
}