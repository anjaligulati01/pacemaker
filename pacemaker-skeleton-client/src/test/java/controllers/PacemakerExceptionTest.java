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
  
  PacemakerInterface pacemakerInterface = Mockito.mock(PacemakerInterface.class);
  
  boolean thrown = false;
  
  @Test
  public void testCreateUser(){
     //add the behavior to throw exception
//     Mockito.doThrow(new RuntimeException("User not created"))
//        .when(pacemakerInterface).registerUser(new User(org.mockito.Matchers.anyString(),org.mockito.Matchers.anyString()
//            ,org.mockito.Matchers.anyString(),org.mockito.Matchers.anyString()));
    User homer = new User("homer", "simpson", "homer@simpson.com", "secret");
    pacemaker.pacemakerInterface = pacemakerInterface;
     when(pacemakerInterface.registerUser(homer)).thenThrow(new RuntimeException("User not created because of exception"));
     //org.mockito.Matchers.anyString(),org.mockito.Matchers.anyString()
     //,org.mockito.Matchers.anyString(),org.mockito.Matchers.anyString()
     //test the add functionality
     //User homer = new User("homer", "simpson", "homer@simpson.com", "secret");
     User user = pacemaker.createUser(homer.firstname, homer.lastname, homer.email, homer.password);
     //System.out.println(user.toString());
     
     
     assertNull(user);
  }
}
