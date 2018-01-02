package models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Fixtures {

  public static List<User> users =
      new ArrayList<>(Arrays.asList(new User("marge", "simpson", "marge@simpson.com", "secret"),
          new User("lisa", "simpson", "lisa@simpson.com", "secret"),
          new User("bart", "simpson", "bart@simpson.com", "secret"),
          new User("maggie", "simpson", "maggie@simpson.com", "secret")));
}