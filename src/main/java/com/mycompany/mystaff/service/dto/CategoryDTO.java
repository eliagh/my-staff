package com.mycompany.mystaff.service.dto;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A DTO for the Category entity.
 */
public class CategoryDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  @NotNull
  @Size(min = 2, max = 15)
  private String name;

  @Lob
  private String description;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CategoryDTO categoryDTO = (CategoryDTO) o;
    if (categoryDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), categoryDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "CategoryDTO{" + "id=" + getId() + ", name='" + getName() + "'" + ", description='" + getDescription() + "'" + "}";
  }

}
