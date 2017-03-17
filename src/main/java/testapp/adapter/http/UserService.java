package testapp.adapter.http;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.sitebricks.At;
import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import com.google.sitebricks.http.Delete;
import com.google.sitebricks.http.Get;
import com.google.sitebricks.http.Post;
import testapp.core.User;
import testapp.core.UserDto;
import testapp.core.UserRepository;
import testapp.core.exceptions.UserNotFoundException;
import testapp.core.exceptions.UsernameAlreadyExistsException;

import java.util.List;

@At("/v1/users")
public class UserService {
  private UserRepository repository;

  @Inject
  public UserService(UserRepository repository) {
    this.repository = repository;
  }

  @Get
  public Reply<?> getUsers() {
    List<User> userList = repository.findUsers();
    return Reply.with(UserDto.fromUsers(userList)).as(Json.class);
  }

  @Get
  @At("/v1/:facultyNumber")
  public Reply<?> findUser(@Named("facultyNumber")Integer facultyNumber) {
    try {
      User user = repository.findUser(facultyNumber);
      return Reply.with(UserDto.fromUser(user)).as(Json.class);
    } catch (UserNotFoundException e) {
      return Reply.saying().badRequest();
    }
  }

  @Post
  public Reply<?> registerUser(Request request) {
    UserDto userDto = request.read(UserDto.class).as(Json.class);
    try {
      repository.register(userDto.toUser());
    } catch (UsernameAlreadyExistsException e) {
      return Reply.saying().badRequest();
    }
    return Reply.saying().ok();
  }

  @Delete
  @At("/v1/:facultyNumber")
  public Reply<?> deleteUserById(@Named("facultyNumber")Integer facultyNumber) {
    try {
      repository.delete(facultyNumber);
    } catch (UserNotFoundException e) {
      return Reply.saying().badRequest();
    }
    return Reply.saying().ok();
  }
}
