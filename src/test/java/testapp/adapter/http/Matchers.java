package testapp.adapter.http;

import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.sitebricks.headless.Reply;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import testapp.core.User;

import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.util.List;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class Matchers {
  public static Matcher<List<User>> containsUser(final User user) {
    return new TypeSafeMatcher<List<User>>() {
      @Override
      protected boolean matchesSafely(List<User> item) {
        for (User each : item) {
          if (each.getId() == user.getId()) {
            return true;
          }
        }
        return false;
      }

      @Override
      protected void describeMismatchSafely(List<User> item, Description mismatchDescription) {
        mismatchDescription.appendText("value returns ");
        mismatchDescription.appendValue(null);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("contains value ");
        description.appendValue(user);
      }
    };
  }

  public static Matcher<Reply<?>> returns(final Reply<?> reply) {
    return new TypeSafeMatcher<Reply<?>>() {
      @Override
      protected boolean matchesSafely(Reply<?> item) {
        return item.equals(reply);
      }

      @Override
      protected void describeMismatchSafely(Reply<?> item, Description mismatchDescription) {
        mismatchDescription.appendText("returns ");
        mismatchDescription.appendValue(item);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("expect to be ");
        description.appendValue(reply);
      }
    };
  }

  public static Matcher<User> sameAs(final User user) {
    return new TypeSafeMatcher<User>() {
      @Override
      protected boolean matchesSafely(User item) {
        return user.getId() == item.getId();
      }

      @Override
      protected void describeMismatchSafely(User item, Description mismatchDescription) {
        mismatchDescription.appendText("but was ");
        mismatchDescription.appendValue(item);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("expect to be ");
        description.appendValue(user);
      }
    };
  }

  public static Matcher<Reply<?>> isOk() {
    return new TypeSafeMatcher<Reply<?>>() {
      @Override
      protected boolean matchesSafely(Reply<?> reply) {
        int actualStatus = getStatus(reply);
        return HttpURLConnection.HTTP_OK == actualStatus;
      }

      private int getStatus(Reply<?> reply) {
        try {
          Field status = reply.getClass().getDeclaredField("status");
          status.setAccessible(true);
          return (int) status.get(reply);
        } catch (NoSuchFieldException | IllegalAccessException e) {
          throw new IllegalStateException("Reply has no status information");
        }
      }

      @Override
      protected void describeMismatchSafely(Reply<?> item, Description description) {
        description.appendText("was ");
        description.appendValue(getStatus(item));
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("status code to be 200");
      }
    };
  }

  public static Matcher<Reply<?>> isBadRequest() {
    return new TypeSafeMatcher<Reply<?>>() {
      @Override
      protected boolean matchesSafely(Reply<?> reply) {
        int actualStatus = getStatus(reply);
        return HttpURLConnection.HTTP_BAD_REQUEST == actualStatus;
      }

      private int getStatus(Reply<?> reply) {
        try {
          Field status = reply.getClass().getDeclaredField("status");
          status.setAccessible(true);
          return (int) status.get(reply);
        } catch (NoSuchFieldException | IllegalAccessException e) {
          throw new IllegalStateException("Reply has no status information");
        }
      }

      @Override
      protected void describeMismatchSafely(Reply<?> item, Description description) {
        description.appendText("was ");
        description.appendValue(getStatus(item));
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("status code to be 400");
      }
    };
  }

  public static Matcher<Reply<?>> sameAs(final Object object) {
    return new TypeSafeMatcher<Reply<?>>() {
      @Override
      protected boolean matchesSafely(Reply<?> reply) {
        Object replyObject = getObject(reply);
        return replyObject.equals(object);
      }

      private Object getObject(Reply<?> reply) {
        try {
          Field field = reply.getClass().getDeclaredField("entity");
          field.setAccessible(true);
          return field.get(reply);
        } catch (NoSuchFieldException | IllegalAccessException e) {
          throw new IllegalStateException("Reply has no entity information");
        }
      }

      @Override
      protected void describeMismatchSafely(Reply<?> item, Description description) {
        description.appendText("was ");
        description.appendValue(getObject(item));
      }

      @Override
      public void describeTo(Description description) {
        description.appendText(" is " + object);
      }
    };
  }

  public static Matcher<Reply<?>> containsJson(final JsonBuilder content) {
    return new TypeSafeMatcher<Reply<?>>() {
      @Override
      protected boolean matchesSafely(Reply<?> item) {
        Gson gson = new Gson();
        Object value = property("entity", item);
        String jsonContent = gson.toJson(value);

        return jsonContent.equalsIgnoreCase(content.build());
      }

      @Override
      public void describeTo(Description description) {
        description.appendText(content.build());
      }

      @Override
      protected void describeMismatchSafely(Reply<?> item, Description mismatchDescription) {
        String jsonContent = asJsonContent(item);
        mismatchDescription.appendText("was ");
        mismatchDescription.appendText(jsonContent);
      }

      private String asJsonContent(Reply<?> reply) {
        Gson gson = new Gson();
        Object value = property("entity", reply);
        String jsonContent = gson.toJson(value);

        return jsonContent;
      }

      private Object property(String fieldName, Reply<?> reply) {
        try {
          Field field = reply.getClass().getDeclaredField(fieldName);
          field.setAccessible(true);
          return field.get(reply);
        } catch (NoSuchFieldException | IllegalAccessException e) {
          throw new IllegalStateException("Reply has no entity information");
        }
      }
    };
  }

}
