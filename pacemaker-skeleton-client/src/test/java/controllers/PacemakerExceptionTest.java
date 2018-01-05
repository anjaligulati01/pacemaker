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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.Matchers;
import models.User;

@RunWith(MockitoJUnitRunner.class)
public class PacemakerExceptionTest
{
    //@Mock
  PacemakerAPI pacemakerMock = new PacemakerAPI("http://localhost:7000");
  
  boolean thrown = false;
  
  @Test(expected = RuntimeException.class)
  public void testCreateUser(){
     //add the behavior to throw exception
     Mockito.doThrow(new RuntimeException("User not created"))
        .when(pacemakerMock).createUser(org.mockito.Matchers.anyString(),org.mockito.Matchers.anyString()
            ,org.mockito.Matchers.anyString(),org.mockito.Matchers.anyString());

     //test the add functionality
     User homer = new User("homer", "simpson", "homer@simpson.com", "secret");
     try {
       User user = pacemakerMock.createUser(homer.firstname, homer.lastname, homer.email, homer.password);
     }catch(Exception e) {
       thrown = true;
     }
     
     assertTrue(thrown);
  }
}
