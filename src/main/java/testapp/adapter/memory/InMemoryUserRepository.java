package testapp.adapter.memory;

import testapp.core.User;
import testapp.core.UserRepository;
import testapp.core.exceptions.UserNotFoundException;
import testapp.core.exceptions.UsernameAlreadyExistsException;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class InMemoryUserRepository implements UserRepository {
  private List<User> userList = new LinkedList<>();

  public InMemoryUserRepository(List<User> userList) {
    this.userList = userList;
  }

  public InMemoryUserRepository() {
  }

  @Override
  public List<User> findUsers() {
    return userList;
  }

  @Override
  public User findUser(Integer facultyNumber) {
    for (User user : userList) {
      if (user.getFacultyNumber().equals(facultyNumber)) {
        return user;
      }
    }
    throw new UserNotFoundException();
  }

  @Override
  public void register(User user) {
    for (User each : userList) {
      if (Objects.equals(each.getName(), user.getName())) {
        throw new UsernameAlreadyExistsException();
      }
    }
    userList.add(user);
  }

  @Override
  public void delete(Integer facultyNumber) {
    for (User each : userList) {
      if (Objects.equals(each.getFacultyNumber(), facultyNumber)) {
        userList.remove(each);
        return;
      } else {
        throw new UserNotFoundException();
      }
    }
  }
}
