package testapp.adapter.http;

import com.google.sitebricks.client.Transport;
import com.google.sitebricks.headless.Request.RequestRead;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class RequestReadOf implements RequestRead<Object> {
  private final Object object;

  public RequestReadOf(Object object) {
    this.object = object;
  }

  @Override
  public Object as(Class<? extends Transport> transport) {
    return object;
  }
}
