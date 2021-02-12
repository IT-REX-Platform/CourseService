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

  @Id
  @GeneratedValue
  private UUID id;

  @Column
  private String name;

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Lecturer)) return false;
    Lecturer lecturer = (Lecturer) o;
    return Objects.equals(getId(), lecturer.getId()) && Objects.equals(getName(), lecturer.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getName());
  }

  @Override
  public String toString() {
    return "Lecturer{" +
        "id=" + id +
        ", name='" + name + '\'' +
        '}';
  }
}
