package com.mycompany.mystaff.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * A Company.
 */
@Entity
@Table(name = "company")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "company")
public class Company implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Size(min = 3, max = 100)
  @Column(name = "name", length = 100, nullable = false)
  private String name;

  @Lob
  @Column(name = "logo")
  private byte[] logo;

  @Column(name = "logo_content_type")
  private String logoContentType;

  @Column(name = "sector")
  private String sector;

  @NotNull
  @Column(name = "thema", nullable = false)
  private String thema;

  public Company() {
  }

  public Company(Long id) {
    this.id = id;
  }

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public Company name(String name) {
    this.name = name;
    return this;
  }

  public void setName(String name) {
    this.name = name;
  }

  public byte[] getLogo() {
    return logo;
  }

  public Company logo(byte[] logo) {
    this.logo = logo;
    return this;
  }

  public void setLogo(byte[] logo) {
    this.logo = logo;
  }

  public String getLogoContentType() {
    return logoContentType;
  }

  public Company logoContentType(String logoContentType) {
    this.logoContentType = logoContentType;
    return this;
  }

  public void setLogoContentType(String logoContentType) {
    this.logoContentType = logoContentType;
  }

  public String getSector() {
    return sector;
  }

  public Company sector(String sector) {
    this.sector = sector;
    return this;
  }

  public void setSector(String sector) {
    this.sector = sector;
  }

  public String getThema() {
    return thema;
  }

  public Company thema(String thema) {
    this.thema = thema;
    return this;
  }

  public void setThema(String thema) {
    this.thema = thema;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Company company = (Company) o;
    if (company.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), company.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "Company{" + "id=" + getId() + ", name='" + getName() + "'" + ", logo='" + getLogo() + "'" + ", logoContentType='" + logoContentType + "'" + ", sector='" + getSector()
        + "'" + ", thema='" + getThema() + "'" + "}";
  }

}
