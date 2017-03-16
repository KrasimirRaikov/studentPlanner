package testapp.adapter.memory;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import testapp.core.User;
import testapp.core.exceptions.UserNotFoundException;
import testapp.core.exceptions.UsernameAlreadyExistsException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class PersistentUserRepositoryTest {
  private PersistentUserRepository repository = new PersistentUserRepository();
  private LocalServiceTestHelper localServiceTestHelper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  @Before
  public void setUp() throws Exception {
    localServiceTestHelper.setUp();
  }

  @After
  public void tearDown() throws Exception {
    localServiceTestHelper.tearDown();
  }

  @Test
  public void registerUser() throws Exception {
    //Note: if we store an empty list Datastore stores it as null and returns a null.
    User registeredUser = new User(18087, "Vasko", new ArrayList<Long>() {{
      add(123L);
    }});
    repository.register(registeredUser);
    User repositoryUser = repository.findUser(18087);
    assertThat(registeredUser.getSubjects(), is(equalTo(repositoryUser.getSubjects())));
  }

  @Test(expected = UsernameAlreadyExistsException.class)
  public void tryingToRegisterUserNameThatAlreadyExists() throws Exception {
    User registeredUser = new User(18087, "Vasko", new ArrayList<Long>() {{
      add(123L);
    }});
    repository.register(registeredUser);
    repository.register(registeredUser);
  }

  @Test(expected = UserNotFoundException.class)
  public void tryingToFindUserThatDoesNotExist() throws Exception {
    repository.findUser(18087);
  }

  @Test(expected = UserNotFoundException.class)
  public void deleteUser() throws Exception {
    User registeredUser = new User(18087, "Vasko", new ArrayList<Long>() {{
      add(123L);
    }});
    repository.register(registeredUser);
    repository.delete(18087);
    repository.findUser(18087);
  }

  @Test(expected = UserNotFoundException.class)
  public void tryingToDeleteUserThatDoesNotExist() throws Exception {
    repository.delete(18087);
  }

  @Test
  public void getAllUsers() throws Exception {
    User student1 = new User(18087, "Vasko", new ArrayList<Long>() {{
      add(123L);
    }});
    User student2 = new User(18088, "Qni", new ArrayList<Long>() {{
      add(123L);
    }});
    User student3 = new User(18089, "Krasi", new ArrayList<Long>() {{
      add(123L);
    }});
    List expectedUsers = Arrays.asList(student1, student2, student3);
    repository.register(student1);
    repository.register(student2);
    repository.register(student3);
    List<User> result = repository.findUsers();
    assertThat(result, is(expectedUsers));
  }
}
