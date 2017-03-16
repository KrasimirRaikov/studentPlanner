package testapp.adapter.http;


import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import org.junit.Test;
import testapp.adapter.memory.InMemoryUserRepository;
import testapp.core.User;
import testapp.core.UserDto;
import testapp.core.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import static org.junit.Assert.*;
import static testapp.adapter.http.JsonBuilder.aNewJson;
import static testapp.adapter.http.JsonBuilder.aNewJsonArray;
import static testapp.adapter.http.Matchers.containsJson;
import static testapp.adapter.http.Matchers.isBadRequest;
import static testapp.adapter.http.Matchers.isOk;
import static testapp.adapter.http.Matchers.returns;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class UserServiceIntegrationTest {
  private UserRepository userRepository = new InMemoryUserRepository();
  private UserService userService = new UserService(userRepository);


  @Test
  public void registerUser() throws Exception {
    final UserDto userDto = new UserDto(1, "Vasil", new ArrayList<Long>());
    Request request = new FakeRequest<UserDto>(userDto);
    Reply<?> registerReply = userService.registerUser(request);
    assertThat(registerReply, isOk());
  }

  @Test
  public void userAlreadyRegistered() throws Exception {
    userRepository.register(new User(1, "Vasil", new ArrayList<Long>()));
    final UserDto userDto = new UserDto(1, "Vasil", new ArrayList<Long>());
    Request request = new FakeRequest<UserDto>(userDto);
    Reply<?> registerReply = userService.registerUser(request);
    assertThat(registerReply, isBadRequest());
  }

  @Test
  public void deletingUsers() throws Exception {
    userRepository.register(new User(1, "Vasil", new ArrayList<Long>()));
    Reply<?> deleteReply = userService.deleteUserById(1);
    assertThat(deleteReply, isOk());
  }

  @Test
  public void deletingNonExistentUser() throws Exception {
    UserRepository userRepository = new InMemoryUserRepository();
    UserService userService = new UserService(userRepository);
    userRepository.register(new User(2, "Qni", new ArrayList<Long>()));
    Reply<?> deleteReply = userService.deleteUserById(1);
    assertThat(deleteReply, isBadRequest());
  }

  @Test
  public void findUserById() throws Exception {
    UserRepository userRepository = new InMemoryUserRepository();
    UserService userService = new UserService(userRepository);
    User user = new User(2, "Qni", new ArrayList<Long>());
    userRepository.register(user);
    final UserDto userDto = new UserDto(2, "Qni", new ArrayList<Long>());
    Reply<?> findUserReply = userService.findUser(2);
    Reply<?> expectedReply = Reply.with(userDto).as(Json.class);
    assertThat(findUserReply, returns(expectedReply));
    assertThat(findUserReply, containsJson(aNewJson()
            .add("facultyNumber", 2)
            .add("name", "Qni")
            .add("subjects", Collections.<String>emptyList())));
  }

  @Test
  public void tryingToFindNonExistentUser() throws Exception {
    UserRepository userRepository = new InMemoryUserRepository();
    UserService userService = new UserService(userRepository);
    userRepository.register(new User(2, "Qni", new ArrayList<Long>()));
    Reply<?> findNonExistentReply = userService.findUser(1);
    assertThat(findNonExistentReply, isBadRequest());
  }

  @Test
  public void getAllUsers() throws Exception {
    UserRepository userRepository = new InMemoryUserRepository(new LinkedList<User>() {{
      add(new User(1, "Vasko", new ArrayList<Long>()));
      add(new User(2, "Qni", new ArrayList<Long>()));
    }});
    UserService userService = new UserService(userRepository);
    Reply<?> findAllReply = userService.getUsers();
    assertThat(findAllReply, containsJson(
            aNewJsonArray().withElements(
                    aNewJson().add("facultyNumber", 1).add("name", "Vasko").add("subjects", Collections.<String>emptyList()),
                    aNewJson().add("facultyNumber", 2).add("name", "Qni").add("subjects", Collections.<String>emptyList()))));
  }
}