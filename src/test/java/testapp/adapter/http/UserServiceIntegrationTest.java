package testapp.adapter.http;


import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import org.junit.Test;
import testapp.adapter.memory.InMemoryUserRepository;
import testapp.core.User;
import testapp.core.UserDto;
import testapp.core.UserRepository;

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
    final UserDto userDto = new UserDto(1, "Vasil", Collections.<Integer>emptyList());
    Request request=new FakeRequest<UserDto>(userDto);
    Reply<?> registerReply = userService.registerUser(request);
    assertThat(registerReply, isOk());
  }

  @Test
  public void userAlreadyRegistered() throws Exception {
    userRepository.register(new User(1, "Vasil", Collections.<Integer>emptyList()));
    final UserDto userDto = new UserDto(1, "Vasil", Collections.<Integer>emptyList());
    Request request=new FakeRequest<UserDto>(userDto);
    Reply<?> registerReply = userService.registerUser(request);
    assertThat(registerReply, isBadRequest());
  }

  @Test
  public void deletingUsers() throws Exception {
    userRepository.register(new User(1, "Vasil", Collections.<Integer>emptyList()));
    Reply<?> deleteReply = userService.deleteUserById(1);
    assertThat(deleteReply, isOk());
  }

  @Test
  public void deletingNonExistentUser() throws Exception {
    UserRepository userRepository = new InMemoryUserRepository();
    UserService userService = new UserService(userRepository);
    userRepository.register(new User(2, "Qni", Collections.<Integer>emptyList()));
    Reply<?> deleteReply = userService.deleteUserById(1);
    assertThat(deleteReply, isBadRequest());
  }

  @Test
  public void findUserById() throws Exception {
    UserRepository userRepository = new InMemoryUserRepository();
    UserService userService = new UserService(userRepository);
    User user = new User(2, "Qni", Collections.<Integer>emptyList());
    userRepository.register(user);
    final UserDto userDto = new UserDto(2, "Qni", Collections.<Integer>emptyList());
    Reply<?> findUserReply = userService.findUserById(2);
    Reply<?> expectedReply = Reply.with(userDto).as(Json.class);
    assertThat(findUserReply, returns(expectedReply));
    assertThat(findUserReply, containsJson(aNewJson()
            .add("id", 2)
            .add("name", "Qni")
            .add("subjects", Collections.<String>emptyList())));
  }

  @Test
  public void tryingToFindNonExistentUser() throws Exception {
    UserRepository userRepository = new InMemoryUserRepository();
    UserService userService = new UserService(userRepository);
    userRepository.register(new User(2, "Qni", Collections.<Integer>emptyList()));
    Reply<?> findNonExistentReply = userService.findUserById(1);
    assertThat(findNonExistentReply, isBadRequest());
  }

  @Test
  public void getAllUsers() throws Exception {
    UserRepository userRepository = new InMemoryUserRepository(new LinkedList<User>() {{
      add(new User(1, "Vasko", Collections.<Integer>emptyList()));
      add(new User(2, "Qni", Collections.<Integer>emptyList()));
    }});
    UserService userService = new UserService(userRepository);
    Reply<?> findAllReply = userService.getUsers();
    assertThat(findAllReply, containsJson(
            aNewJsonArray().withElements(
                    aNewJson().add("id", 1).add("name", "Vasko").add("subjects", Collections.<String>emptyList()),
                    aNewJson().add("id", 2).add("name", "Qni").add("subjects", Collections.<String>emptyList()))));
  }
}