package testapp.core;

import com.google.appengine.api.datastore.Entity;

import java.util.List;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class User {
  private Integer id;
  private String name;
  private List<Integer> subjects;

  public User() {
  }

  public User(Integer id, String name, List<Integer> subjects) {
    this.id = id;
    this.name = name;
    this.subjects = subjects;
  }

  private User(Builder builder) {
    this.id = builder.id;
    this.name = builder.name;
    this.subjects = builder.subjects;
  }

  public static Builder newUser() {
    return new Builder();
  }

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public List<Integer> getSubjects() {
    return subjects;
  }

  public Entity toEntity(String kind) {
    Entity userEntity = new Entity(kind, id);
    userEntity.setProperty("id", this.id);
    userEntity.setProperty("name", this.name);
    userEntity.setProperty("subjects", this.subjects);
    return userEntity;
  }

  public User fromEntity(Entity entity) {
    Integer id = (Integer) entity.getProperty("id");
    String name = (String) entity.getProperty("name");
    List<Integer> subjects = (List<Integer>) entity.getProperty("subjects");
    return new User(id, name, subjects);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    User user = (User) o;

    if (id != null ? !id.equals(user.id) : user.id != null) return false;
    if (name != null ? !name.equals(user.name) : user.name != null) return false;
    return subjects != null ? subjects.equals(user.subjects) : user.subjects == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (subjects != null ? subjects.hashCode() : 0);
    return result;
  }

  public static final class Builder {
    private Integer id;
    private String name;
    private List<Integer> subjects;

    private Builder() {
    }

    public User build() {
      return new User(this);
    }

    public Builder id(Integer id) {
      this.id = id;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder subjects(List<Integer> subjects) {
      this.subjects = subjects;
      return this;
    }
  }
}
