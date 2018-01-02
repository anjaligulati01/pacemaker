package models;

import static com.google.common.base.MoreObjects.toStringHelper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Objects;

public class User implements Serializable {

  public String id;
  public String firstname;
  public String lastname;
  public String email;
  public String password;
  public List<String> friends;
  public Map<String, List<String>> messagesFromFriends = new HashMap<>();
  public boolean disabled;

  public User() {
  }

  public String getId() {
    return id;
  }

  public String getFirstname() {
    return firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public String getEmail() {
    return email;
  }
  
  public List<String> getFriends() {
    return friends;
  }
  

  public User(String firstName, String lastName, String email, String password) {
    this.firstname = firstName;
    this.lastname = lastName;
    this.email = email;
    this.password = password;
    this.disabled = false;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj instanceof User) {
      final User other = (User) obj;
      return Objects.equal(firstname, other.firstname)
          && Objects.equal(lastname, other.lastname)
          && Objects.equal(email, other.email)
          && Objects.equal(password, other.password);
    } else {
      return false;
    }
  }

  @Override
  public String toString() {
    return toStringHelper(this).addValue(id)
        .addValue(firstname)
        .addValue(lastname)
        .addValue(password)
        .addValue(email)
        .addValue(friends)
        .toString();
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.id, this.lastname, this.firstname, this.email, this.password);
  }
}