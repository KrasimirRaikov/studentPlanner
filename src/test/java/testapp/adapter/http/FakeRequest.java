package testapp.adapter.http;

import com.google.appengine.labs.repackaged.com.google.common.base.Joiner;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import com.google.inject.TypeLiteral;
import com.google.sitebricks.client.Transport;
import com.google.sitebricks.headless.Request;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class FakeRequest<E> implements Request {  public static <E> FakeRequest<E> newRequest(E value) {
  return new FakeRequest<E>(value);
}  public static <E> FakeRequest<E> newRequest(Map<String, String> request) {
  return new FakeRequest<E>(request);
}  public static <E> FakeRequest<E> newRequest(Map<String, String> params, Map<String, List<String>> multiParams) {
  return new FakeRequest<E>(params, multiParams);
}  public static Builder newRequest() {
  return new Builder();
}  public static class Builder {
  private ImmutableMap.Builder<String, String> parameterMap = ImmutableMap.builder();
  private ImmutableMap.Builder<String, List<String>> multiParameterMap = ImmutableMap.builder();
  private ImmutableMap.Builder<String, List<String>> headersMap = ImmutableMap.builder();    public Builder parameter(String key, String value) {
    parameterMap.put(key, value);
    return this;
  }    public Builder parameter(String key, List<String> values) {
    multiParameterMap.put(key, values);
    return this;
  }    public Builder header(String key, List<String> values) {
    headersMap.put(key, values);
    return this;
  }    public FakeRequest build() {
    return new FakeRequest(parameterMap.build(), multiParameterMap.build(), headersMap.build());
  }
}  public E value;  public Map<String, String> params;
  public Map<String, List<String>> multiParams;
  public Map<String, List<String>> headers;  public FakeRequest(Map<String, String> params, Map<String, List<String>> multiParams, Map<String, List<String>> headers) {
    this.params = params;
    this.multiParams = multiParams;
    this.headers = headers;
  }  public FakeRequest(Map<String, String> params, Map<String, List<String>> multiParams) {
    this.params = params;
    this.multiParams = multiParams;
  }  public FakeRequest(Map<String, String> params) {
    this.params = params;
  }  public FakeRequest(E value) {
    this.value = value;
  }  public <E> RequestRead<E> read(Class<E> type) {
    return new RequestRead<E>() {
      public E as(Class<? extends Transport> aClass) {
        return (E) value;
      }
    };
  }  public <E> RequestRead<E> read(TypeLiteral<E> typeLiteral) {
    return new RequestRead<E>() {
      public E as(Class<? extends Transport> aClass) {
        return (E) value;
      }
    };
  }  public void readTo(OutputStream out) throws IOException {
  }  public Multimap<String, String> headers() {
    Multimap<String, String> multimap = ArrayListMultimap.create();
    for (String key : headers.keySet()) {
      multimap.putAll(key, headers.get(key));
    }
    return multimap;
  }  public Multimap<String, String> params() {
    Multimap<String, String> multimap = ArrayListMultimap.create();
    for (String key : multiParams.keySet()) {
      multimap.putAll(key, multiParams.get(key));
    }    return multimap;
  }  public Multimap<String, String> matrix() {
    return null;
  }  public String matrixParam(String s) {
    return null;
  }  public String param(String name) {
    return params.get(name);
  }  public String header(String s) {
    Map<String, Collection<String>> map = headers().asMap();
    if(map == null) {
      return null;
    }    Collection<String> value = map.get(s);
    if(value == null) {
      return null;
    }
    return Joiner.on(",").join(value);
  }  public String uri() {
    return null;
  }  public String path() {
    return null;
  }  public String context() {
    return null;
  }  public String method() {
    return null;
  }  @Override
  public void validate(Object o) {  }
}
