package com.mycompany.mystaff.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * A Item.
 */
@Entity
@Table(name = "item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "item")
public class Item implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Size(min = 3, max = 100)
  @Column(name = "name", length = 100, nullable = false)
  private String name;

  @Column(name = "picture_url")
  private String pictureUrl;

  @DecimalMin(value = "0")
  @Column(name = "price_per_unit")
  private Double pricePerUnit;

  @NotNull
  @Size(min = 3, max = 15)
  @Column(name = "unit", length = 15, nullable = false)
  private String unit;

  @Lob
  @Column(name = "code")
  private String code;

  @Lob
  @Column(name = "description")
  private String description;

  @NotNull
  @Column(name = "show_in_shop", nullable = false)
  private Boolean showInShop;

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

  public Item name(String name) {
    this.name = name;
    return this;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPictureUrl() {
    return pictureUrl;
  }

  public Item pictureUrl(String pictureUrl) {
    this.pictureUrl = pictureUrl;
    return this;
  }

  public void setPictureUrl(String pictureUrl) {
    this.pictureUrl = pictureUrl;
  }

  public Double getPricePerUnit() {
    return pricePerUnit;
  }

  public Item pricePerUnit(Double pricePerUnit) {
    this.pricePerUnit = pricePerUnit;
    return this;
  }

  public void setPricePerUnit(Double pricePerUnit) {
    this.pricePerUnit = pricePerUnit;
  }

  public String getUnit() {
    return unit;
  }

  public Item unit(String unit) {
    this.unit = unit;
    return this;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public String getCode() {
    return code;
  }

  public Item code(String code) {
    this.code = code;
    return this;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getDescription() {
    return description;
  }

  public Item description(String description) {
    this.description = description;
    return this;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Boolean isShowInShop() {
    return showInShop;
  }

  public Item showInShop(Boolean showInShop) {
    this.showInShop = showInShop;
    return this;
  }

  public void setShowInShop(Boolean showInShop) {
    this.showInShop = showInShop;
  }

  public Company getCompany() {
    return company;
  }

  public Item company(Company company) {
    this.company = company;
    return this;
  }

  public void setCompany(Company company) {
    this.company = company;
  }

    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Item item = (Item) o;
    if (item.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), item.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "Item{" + "id=" + getId() + ", name='" + getName() + "'" + ", pictureUrl='" + getPictureUrl() + "'" + ", pricePerUnit='" + getPricePerUnit() + "'" + ", unit='"
        + getUnit() + "'" + ", code='" + getCode() + "'" + ", description='" + getDescription() + "'" + ", showInShop='" + isShowInShop() + "'" + "}";
  }

}
