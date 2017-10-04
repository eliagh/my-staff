package com.mycompany.mystaff.service.dto;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A DTO for the Location entity.
 */
public class LocationDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  @NotNull
  @Size(min = 2, max = 100)
  private String name;

  private String address1;

  private String address2;

  private String address3;

  private Integer number;

  @Size(min = 2, max = 15)
  private String postcode;

  private String city;

  private String stateProvince;

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

  public String getAddress1() {
    return address1;
  }

  public void setAddress1(String address1) {
    this.address1 = address1;
  }

  public String getAddress2() {
    return address2;
  }

  public void setAddress2(String address2) {
    this.address2 = address2;
  }

  public String getAddress3() {
    return address3;
  }

  public void setAddress3(String address3) {
    this.address3 = address3;
  }

  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }

  public String getPostcode() {
    return postcode;
  }

  public void setPostcode(String postcode) {
    this.postcode = postcode;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getStateProvince() {
    return stateProvince;
  }

  public void setStateProvince(String stateProvince) {
    this.stateProvince = stateProvince;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    LocationDTO locationDTO = (LocationDTO) o;
    if (locationDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), locationDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "LocationDTO{" + "id=" + getId() + ", name='" + getName() + "'" + ", address1='" + getAddress1() + "'" + ", address2='" + getAddress2() + "'" + ", address3='"
        + getAddress3() + "'" + ", number='" + getNumber() + "'" + ", postcode='" + getPostcode() + "'" + ", city='" + getCity() + "'" + ", stateProvince='" + getStateProvince()
        + "'" + "}";
  }

}
