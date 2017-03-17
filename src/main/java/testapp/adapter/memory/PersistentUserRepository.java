package testapp.adapter.memory;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import testapp.core.User;
import testapp.core.UserRepository;
import testapp.core.exceptions.UserNotFoundException;
import testapp.core.exceptions.UsernameAlreadyExistsException;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class PersistentUserRepository implements UserRepository {
  private DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();


  @Override
  public List<User> findUsers() {
    Query query = new Query("user");
    FetchOptions fetchOptions = FetchOptions.Builder.withDefaults();
    List<Entity> datastoreResult = dataStore.prepare(query).asList(fetchOptions);
    List<User> result = new LinkedList<>();
    for (Entity entity : datastoreResult) {
      result.add(User.fromEntity(entity));
    }
    return result;
  }


  @Override
  public User findUser(Integer facultyNumber) {
    Entity result = getEntity(facultyNumber);
    if (result == null) {
      throw new UserNotFoundException();
    }
    return User.fromEntity(result);
  }

  @Override
  public void register(User user) {
    if (userNameIsAvailable(user.getName())) {
      dataStore.put(user.toEntity("user"));
    } else {
      throw new UsernameAlreadyExistsException();
    }
  }

  @Override
  public void delete(Integer facultyNumber) {
    Entity entity = getEntity(facultyNumber);
    if (entity == null) {
      throw new UserNotFoundException();
    }
    dataStore.delete(entity.getKey());

  }

  private Entity getEntity(Integer facultyNumber) {
    Query query = new Query("user").setFilter(new FilterPredicate("facultyNumber", FilterOperator.EQUAL, facultyNumber));
    return dataStore.prepare(query).asSingleEntity();
  }

  private boolean userNameIsAvailable(String username) {
    Query query = new Query("user").setFilter(new FilterPredicate("name", FilterOperator.EQUAL, username));
    Entity result = dataStore.prepare(query).asSingleEntity();
    if (result == null) {
      return true;
    }
    return false;
  }
}

