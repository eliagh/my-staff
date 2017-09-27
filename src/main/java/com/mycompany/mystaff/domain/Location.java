package com.mycompany.mystaff.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * A Location.
 */
@Entity
@Table(name = "location")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "location")
public class Location implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Size(min = 2, max = 100)
  @Column(name = "name", length = 100, nullable = false)
  private String name;

  @Column(name = "address_1")
  private String address1;

  @Column(name = "address_2")
  private String address2;

  @Column(name = "address_3")
  private String address3;

  @Column(name = "jhi_number")
  private Integer number;

  @Size(min = 2, max = 15)
  @Column(name = "postcode", length = 15)
  private String postcode;

  @Column(name = "city")
  private String city;

  @Column(name = "state_province")
  private String stateProvince;

  @ManyToOne(optional = false)
  @NotNull
  private Company company;

  // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public Location name(String name) {
    this.name = name;
    return this;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress1() {
    return address1;
  }

  public Location address1(String address1) {
    this.address1 = address1;
    return this;
  }

  public void setAddress1(String address1) {
    this.address1 = address1;
  }

  public String getAddress2() {
    return address2;
  }

  public Location address2(String address2) {
    this.address2 = address2;
    return this;
  }

  public void setAddress2(String address2) {
    this.address2 = address2;
  }

  public String getAddress3() {
    return address3;
  }

  public Location address3(String address3) {
    this.address3 = address3;
    return this;
  }

  public void setAddress3(String address3) {
    this.address3 = address3;
  }

  public Integer getNumber() {
    return number;
  }

  public Location number(Integer number) {
    this.number = number;
    return this;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }

  public String getPostcode() {
    return postcode;
  }

  public Location postcode(String postcode) {
    this.postcode = postcode;
    return this;
  }

  public void setPostcode(String postcode) {
    this.postcode = postcode;
  }

  public String getCity() {
    return city;
  }

  public Location city(String city) {
    this.city = city;
    return this;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getStateProvince() {
    return stateProvince;
  }

  public Location stateProvince(String stateProvince) {
    this.stateProvince = stateProvince;
    return this;
  }

  public void setStateProvince(String stateProvince) {
    this.stateProvince = stateProvince;
  }

  public Company getCompany() {
    return company;
  }

  public Location company(Company company) {
    this.company = company;
    return this;
  }

  public void setCompany(Company company) {
    this.company = company;
  }

  // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Location location = (Location) o;
    if (location.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), location.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "Location{" + "id=" + getId() + ", name='" + getName() + "'" + ", address1='" + getAddress1() + "'" + ", address2='" + getAddress2() + "'" + ", address3='"
        + getAddress3() + "'" + ", number='" + getNumber() + "'" + ", postcode='" + getPostcode() + "'" + ", city='" + getCity() + "'" + ", stateProvince='" + getStateProvince()
        + "'" + "}";
  }

}
