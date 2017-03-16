package testapp.core;

import com.google.appengine.api.datastore.Entity;
import com.google.common.base.Objects;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class User {
  private Integer facultyNumber;
  private String name;
  private List<Long> subjects;

  public User() {
  }

  public User(Integer facultyNumber, String name, List<Long> subjects) {
    this.facultyNumber = facultyNumber;
    this.name = name;
    this.subjects = subjects;
  }

  private User(Builder builder) {
    this.facultyNumber = builder.id;
    this.name = builder.name;
    this.subjects = builder.subjects;
  }

  public static Builder newUser() {
    return new Builder();
  }

  public Integer getFacultyNumber() {
    return facultyNumber;
  }

  public String getName() {
    return name;
  }

  public List<Long> getSubjects() {
    return subjects;
  }

  public Entity toEntity(String kind) {
    Entity userEntity = new Entity(kind);
    userEntity.setProperty("facultyNumber", this.facultyNumber);
    userEntity.setProperty("name", this.name);
    userEntity.setProperty("subjects", this.subjects);
    return userEntity;
  }

  public static User fromEntity(Entity entity) {
    Integer facultyNumber = ((Long) entity.getProperty("facultyNumber")).intValue();
    String name = (String) entity.getProperty("name");
    ArrayList<Long> subjects = (ArrayList<Long>) entity.getProperty("subjects");
    return new User(facultyNumber, name, subjects);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equal(facultyNumber, user.facultyNumber) &&
            Objects.equal(name, user.name) &&
            Objects.equal(subjects, user.subjects);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(facultyNumber, name, subjects);
  }

  public static final class Builder {
    private Integer id;
    private String name;
    private List<Long> subjects;

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

    public Builder subjects(List<Long> subjects) {
      this.subjects = subjects;
      return this;
    }
  }

  @Override
  public String toString() {
    return "User{" +
            "facultyNumber=" + facultyNumber +
            ", name='" + name + '\'' +
            ", subjects=" + subjects +
            '}';
  }
}
