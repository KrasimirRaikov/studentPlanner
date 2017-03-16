package testapp.modules;

import com.google.sitebricks.SitebricksModule;
import testapp.adapter.http.UserService;

public class AppModule extends SitebricksModule {

  @Override
  protected void configureSitebricks() {
    at("/v1/users").serve(UserService.class);
  }
}
