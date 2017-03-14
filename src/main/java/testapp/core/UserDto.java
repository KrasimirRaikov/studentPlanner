package testapp.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class UserDto {
  private Integer id;
  private String name;
  private List<Integer> subjects;

  public UserDto(Integer id, String name, List<Integer> subjects) {
    this.id = id;
    this.name = name;
    this.subjects = subjects;
  }

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public List<Integer> getSubjects() {
    return subjects;
  }

  public User toUser() {
    return new User(id, name, subjects);
  }

  public static UserDto fromUser(User user) {
    return new UserDto(user.getId(), user.getName(), user.getSubjects());
  }

  public static List<UserDto> fromUsers(List<User> users) {
    List<UserDto> userDtoList = new ArrayList<>();
    for (User user : users) {
      userDtoList.add(fromUser(user));
    }
    return userDtoList;
  }
}
