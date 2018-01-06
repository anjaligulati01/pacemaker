package controllers;

import static models.Fixtures.users;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.anyString;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.Matchers;
import models.User;


public class PacemakerExceptionTest
{
    //@Mock
  PacemakerAPI pacemaker = new PacemakerAPI("http://localhost:7000");
  
  PacemakerAPI realPacemaker = new PacemakerAPI("http://localhost:7000");
  
  PacemakerInterface pacemakerInterface = Mockito.mock(PacemakerInterface.class);
  
  boolean thrown = false;
  
  @Before
  public void setup() {
    pacemaker.deleteUsers();
    users.forEach(
        user -> realPacemaker.createUser(user.firstname, user.lastname, user.email, user.password));
    List<User> returnedUsers = (List<User>)pacemaker.getUsers();
  }
  
  @Test
  public void testCreateUser(){
   
    User homer = new User("homer", "simpson", "homer@simpson.com", "secret");
    pacemaker.pacemakerInterface = pacemakerInterface;
     when(pacemakerInterface.registerUser(homer)).thenThrow(new RuntimeException("User not created because of exception"));
    
     User user = pacemaker.createUser(homer.firstname, homer.lastname, homer.email, homer.password);
       
     assertNull(user);
  }
  
  @Test
  public void testGetUsers(){
   
   //User homer = new User("homer", "simpson", "homer@simpson.com", "secret");
   pacemaker.pacemakerInterface = pacemakerInterface;
   
    when(pacemakerInterface.getUsers()).thenThrow(new RuntimeException("No users returned because of exception"));
    
    Collection<User> users = pacemaker.getUsers();
 
    assertNull(users);
 }
  
  /*@Test
  public void testDeleteUser(){
   //User homer = new User("homer", "simpson", "homer@simpson.com", "secret");
   pacemaker.pacemakerInterface = pacemakerInterface;
   when(pacemakerInterface.deleteUser(anyString())).thenThrow(new RuntimeException("No users deleted because of exception"));
   List<User> returnedUsers = (List<User>)pacemaker.getUsers();
   returnedUsers.forEach(user -> pacemaker.deleteUser(user.id));
   assertNotEquals(realPacemaker.getUsers().size(), 0);
 };*/
  
  
}
