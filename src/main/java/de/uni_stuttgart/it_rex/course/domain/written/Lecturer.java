package de.uni_stuttgart.it_rex.course.domain.written;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table
public class Lecturer implements Serializable {

  /**
   * id.
   */
  @Id
  @GeneratedValue
  private UUID id;

  /**
   * Name.
   */
  @Column
  private String name;

  /**
   * Getter.
   *
   * @return the id.
   */
  public UUID getId() {
    return id;
  }

  /**
   * Getter.
   *
   * @return the name.
   */
  public String getName() {
    return name;
  }

  /**
   * Setter.
   *
   * @param newName the name
   */
  public void setName(final String newName) {
    this.name = newName;
  }

  /**
   * Equals method.
   *
   * @param o the other object.
   * @return if they are equal.
   */
  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Lecturer)) {
      return false;
    }
    Lecturer lecturer = (Lecturer) o;
    return Objects.equals(getId(), lecturer.getId())
        && Objects.equals(getName(), lecturer.getName());
  }

  /**
   * Hash code.
   *
   * @return
   */
  @Override
  public int hashCode() {
    return Objects.hash(getId(), getName());
  }

  /**
   * Converts the instance to a string.
   *
   * @return the string.
   */
  @Override
  public String toString() {
    return "Lecturer{"
        + "id=" + id
        + ", name='" + name + '\''
        + '}';
  }
}
