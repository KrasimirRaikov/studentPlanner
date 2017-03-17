package testapp.adapter.http;

import com.google.sitebricks.headless.Request;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import testapp.core.User;
import testapp.core.UserDto;
import testapp.core.UserRepository;
import testapp.core.exceptions.UserNotFoundException;
import testapp.core.exceptions.UsernameAlreadyExistsException;

import java.util.ArrayList;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class UserServiceUnitTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  UserRepository repository;

  public UserService userService;


  @Before
  public void setUp() {
    userService = new UserService(repository);
  }

  @Test
  public void registerUser() throws Exception {
    final UserDto userDto = new UserDto(1, "Vasil", new ArrayList<Long>());
    Request request = new FakeRequest<UserDto>(userDto);
    context.checking(new Expectations() {{
      oneOf(repository).register(userDto.toUser());
    }});
    userService.registerUser(request);
  }

  @Test
  public void registerExistingUser() throws Exception {
    final UserDto userDto = new UserDto(1, "Vasil", new ArrayList<Long>());
    Request request = new FakeRequest<UserDto>(userDto);
    context.checking(new Expectations() {{
      oneOf(repository).register(userDto.toUser());
      will(throwException(new UsernameAlreadyExistsException()));
    }});
    userService.registerUser(request);
  }

  @Test
  public void findUser() throws Exception {
    context.checking(new Expectations() {{
      oneOf(repository).findUser(1);
      will(returnValue(new User()));
    }});
    userService.findUser(1);
  }

  @Test
  public void tryingToFindNonExistentUser() throws Exception {
    context.checking(new Expectations() {{
      oneOf(repository).findUser(1);
      will(throwException(new UserNotFoundException()));
    }});
    userService.findUser(1);
  }

  @Test
  public void deletingUser() throws Exception {
    context.checking(new Expectations() {{
      oneOf(repository).delete(1);
    }});
    userService.deleteUserById(1);
  }

  @Test
  public void tryingToDeleteNonExistingUser() throws Exception {
    context.checking(new Expectations() {{
      oneOf(repository).delete(1);
      will(throwException(new UserNotFoundException()));
    }});
    userService.deleteUserById(1);
  }

  @Test
  public void getUsers() throws Exception {
    context.checking(new Expectations() {{
      oneOf(repository).findUsers();
    }});
    userService.getUsers();
  }
}
