package testapp.core;

import java.util.List;


public interface UserRepository {

  List<User> findUsers();

  User findUser(Integer facultyNumber);

  void register(User user);

  void delete(Integer facultyNumber);
}
