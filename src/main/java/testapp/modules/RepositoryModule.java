package testapp.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import testapp.adapter.memory.InMemoryUserRepository;
import testapp.core.UserRepository;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class RepositoryModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(UserRepository.class).to(InMemoryUserRepository.class).in(Scopes.SINGLETON);
  }
}
