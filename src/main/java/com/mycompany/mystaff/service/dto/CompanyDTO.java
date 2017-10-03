package com.mycompany.mystaff.service.dto;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A DTO for the Company entity.
 */
public class CompanyDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  @NotNull
  @Size(min = 3, max = 100)
  private String name;

  @Lob
  private byte[] logo;
  private String logoContentType;

  private String sector;

  @NotNull
  private String thema;

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

  public byte[] getLogo() {
    return logo;
  }

  public void setLogo(byte[] logo) {
    this.logo = logo;
  }

  public String getLogoContentType() {
    return logoContentType;
  }

  public void setLogoContentType(String logoContentType) {
    this.logoContentType = logoContentType;
  }

  public String getSector() {
    return sector;
  }

  public void setSector(String sector) {
    this.sector = sector;
  }

  public String getThema() {
    return thema;
  }

  public void setThema(String thema) {
    this.thema = thema;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CompanyDTO companyDTO = (CompanyDTO) o;
    if (companyDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), companyDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "CompanyDTO{" + "id=" + getId() + ", name='" + getName() + "'" + ", logo='" + getLogo() + "'" + ", sector='" + getSector() + "'" + ", thema='" + getThema() + "'" + "}";
  }

}
