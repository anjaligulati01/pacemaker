package controllers;

import static org.junit.Assert.*;
import java.util.Collection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import models.User;
import static models.Fixtures.users;

public class UserTest {

  //PacemakerAPI pacemaker = new PacemakerAPI("https://warm-island-21310.herokuapp.com");
  PacemakerAPI pacemaker = new PacemakerAPI("http://localhost:7000");
  User homer = new User("homer", "simpson", "homer@simpson.com", "secret");

  @Before
  public void setup() {
    pacemaker.deleteUsers();
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testCreateUser() {
    User user = pacemaker.createUser(homer.firstname, homer.lastname, homer.email, homer.password);
    assertEquals(user, homer);
    User user2 = pacemaker.getUserByEmail(homer.email);
    assertEquals(user2, homer);
  }

  @Test
  public void testCreateUsers() {
    users.forEach(
        user -> pacemaker.createUser(user.firstname, user.lastname, user.email, user.password));
    Collection<User> returnedUsers = pacemaker.getUsers();
    assertEquals(users.size(), returnedUsers.size());
  }
  
  @Test
  public void testDeleteUser() {
    users.forEach(
        user -> pacemaker.createUser(user.firstname, user.lastname, user.email, user.password));
    Collection<User> returnedUsers = pacemaker.getUsers();
    pacemaker.deleteUser(users.get(0).id);
    assertEquals(pacemaker.getUsers().size(), returnedUsers.size());
  }
  
  @Test
  public void testDeleteUsers(){
    users.forEach(
        user -> pacemaker.createUser(user.firstname, user.lastname, user.email, user.password));
    Collection<User> returnedUsers = pacemaker.getUsers();
    pacemaker.deleteUsers();
    assertEquals(pacemaker.getUsers().size(), 0);
  }
  
  @Test
 public void testFollow() {
    users.forEach(
        user -> pacemaker.createUser(user.firstname, user.lastname, user.email, user.password));
    Collection<User> returnedUsers = pacemaker.getUsers();
    pacemaker.follow(users.get(0).id, users.get(1).email);
    System.out.println("User 1 id: " + users.get(1).id);
    int friendCount = pacemaker.listFriends(users.get(0).id).size();
    assertEquals(friendCount, users.get(0).friends.size());
  }
}