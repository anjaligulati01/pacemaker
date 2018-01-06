package controllers;

import static org.junit.Assert.*;
import java.util.Collection;
import java.util.List;

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
    users.forEach(
        user -> pacemaker.createUser(user.firstname, user.lastname, user.email, user.password));
    //Collection<User> returnedUsers = pacemaker.getUsers();
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
    /*users.forEach(
        user -> pacemaker.createUser(user.firstname, user.lastname, user.email, user.password));*/
    Collection<User> returnedUsers = pacemaker.getUsers();
    assertEquals(users.size(), returnedUsers.size());
  }
  
  @Test
  public void testDeleteUser() {
    /*users.forEach(
        user -> pacemaker.createUser(user.firstname, user.lastname, user.email, user.password));*/
    Collection<User> returnedUsers = pacemaker.getUsers();
    //pacemaker.deleteUser(users.get(0).id);
    //assertEquals(pacemaker.getUsers().size(), returnedUsers.size());
    returnedUsers.forEach(user -> pacemaker.deleteUser(user.id));
    assertEquals(pacemaker.getUsers().size(), 0);
  }
  
  @Test
  public void testDeleteUsers(){
    /*users.forEach(
        user -> pacemaker.createUser(user.firstname, user.lastname, user.email, user.password));*/
    //Collection<User> returnedUsers = pacemaker.getUsers();
    pacemaker.deleteUsers();
    assertEquals(pacemaker.getUsers().size(), 0);
  }
  
  @Test
 public void testFollow() {
    
    List<User> returnedUsers = (List<User>)pacemaker.getUsers();
    pacemaker.follow(returnedUsers.get(0).id, returnedUsers.get(1).email);
    int friendCount = pacemaker.listFriends(returnedUsers.get(0).id).size();
    assertEquals(friendCount, 1);
  }
  
  @Test
  public void testUnfollow() {
    /*users.forEach(
        user -> pacemaker.createUser(user.firstname, user.lastname, user.email, user.password));*/
    List<User> returnedUsers = (List<User>)pacemaker.getUsers();
    int friendCountBeforeFollow = pacemaker.listFriends(returnedUsers.get(0).id).size();
    pacemaker.follow(returnedUsers.get(0).id, returnedUsers.get(1).email);
    int friendCountAfterFollow = pacemaker.listFriends(returnedUsers.get(0).id).size();
    assertNotEquals(friendCountBeforeFollow, friendCountAfterFollow);
    pacemaker.unfollowFriend(returnedUsers.get(0).id, returnedUsers.get(1).email);
    int friendCountAfterUnfollow = pacemaker.listFriends(returnedUsers.get(0).id).size();
    assertNotEquals(friendCountAfterFollow, friendCountAfterUnfollow);
  }
  
  @Test
  public void testListFriends() {
    /*users.forEach(
        user -> pacemaker.createUser(user.firstname, user.lastname, user.email, user.password));*/
    List<User> returnedUsers = (List<User>)pacemaker.getUsers();
    pacemaker.follow(returnedUsers.get(0).id, returnedUsers.get(1).email);
    pacemaker.follow(returnedUsers.get(0).id, returnedUsers.get(2).email);
    int friendCount = pacemaker.listFriends(returnedUsers.get(0).id).size();
    assertEquals(friendCount, 2);
  }
  
  @Test
  public void testSendMessageToFriend() {
    /*users.forEach(
        user -> pacemaker.createUser(user.firstname, user.lastname, user.email, user.password));*/
    List<User> returnedUsers = (List<User>)pacemaker.getUsers();
    pacemaker.follow(returnedUsers.get(0).id, returnedUsers.get(1).email);
    pacemaker.sendMessageToFriend(returnedUsers.get(0).id, returnedUsers.get(1).email, "Hello Friend");
    List<String> messages = (List<String>)pacemaker.listMessages(returnedUsers.get(1).id);
    assertEquals(messages.size(), 1);
  }
  
  @Test 
  public void testSendMessageToAllFriends() {
    /*users.forEach(
        user -> pacemaker.createUser(user.firstname, user.lastname, user.email, user.password));*/
    List<User> returnedUsers = (List<User>)pacemaker.getUsers();
    pacemaker.follow(returnedUsers.get(0).id, returnedUsers.get(1).email);
    pacemaker.follow(returnedUsers.get(0).id, returnedUsers.get(2).email);
    pacemaker.follow(returnedUsers.get(0).id, returnedUsers.get(3).email);
    pacemaker.sendMessageToAllFriends(returnedUsers.get(0).id, "Hi All");
    //assertEquals(returnedUsers.get(1).messagesFromFriends.get(returnedUsers.get(0).email)., returnedUsers.get(2).messagesFromFriends.get(returnedUsers.get(0).email));
    assertEquals(pacemaker.listMessages(returnedUsers.get(1).id).size(), pacemaker.listMessages(returnedUsers.get(2).id).size());
  }
  
  @Test
  public void testDisableUser() {
    User user = pacemaker.createUser(homer.firstname, homer.lastname, homer.email, homer.password);
    pacemaker.disableUser(user.email);
    assertTrue(pacemaker.getUser(user.id).disabled);
    pacemaker.enableUser(user.email);
    assertFalse(pacemaker.getUser(user.id).disabled);
  }
  
  
  
  
  
 
  
}