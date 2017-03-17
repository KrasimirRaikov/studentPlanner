package testapp.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class UserDto {
  private Integer facultyNumber;
  private String name;
  private List<Long> subjects;

  public UserDto() {
  }

  public UserDto(Integer facultyNumber, String name, List<Long> subjects) {
    this.facultyNumber = facultyNumber;
    this.name = name;
    this.subjects = subjects;
  }

  public Integer getFacultyNumber() {
    return facultyNumber;
  }

  public String getName() {
    return name;
  }

  public List<Long> getSubjects() {
    return subjects;
  }

  public User toUser() {
    return new User(facultyNumber, name,  subjects);
  }

  public static UserDto fromUser(User user) {
    return new UserDto(user.getFacultyNumber(), user.getName(), user.getSubjects());
  }

  public static List<UserDto> fromUsers(List<User> users) {
    List<UserDto> userDtoList = new ArrayList<>();
    for (User user : users) {
      userDtoList.add(fromUser(user));
    }
    return userDtoList;
  }
}
