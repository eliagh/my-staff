package com.mycompany.mystaff.service.dto;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Lob;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A DTO for the Item entity.
 */
public class ItemDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  @NotNull
  @Size(min = 3, max = 100)
  private String name;

  private String pictureUrl;

  @DecimalMin(value = "0")
  private Double pricePerUnit;

  @NotNull
  @Size(min = 3, max = 15)
  private String unit;

  @Lob
  private String code;

  @Lob
  private String description;

  @NotNull
  private Boolean showInShop;

  private Long companyId;

  private String companyName;

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

  public String getPictureUrl() {
    return pictureUrl;
  }

  public void setPictureUrl(String pictureUrl) {
    this.pictureUrl = pictureUrl;
  }

  public Double getPricePerUnit() {
    return pricePerUnit;
  }

  public void setPricePerUnit(Double pricePerUnit) {
    this.pricePerUnit = pricePerUnit;
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Boolean isShowInShop() {
    return showInShop;
  }

  public void setShowInShop(Boolean showInShop) {
    this.showInShop = showInShop;
  }

  public Long getCompanyId() {
    return companyId;
  }

  public void setCompanyId(Long companyId) {
    this.companyId = companyId;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ItemDTO itemDTO = (ItemDTO) o;
    if (itemDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), itemDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "ItemDTO{" + "id=" + getId() + ", name='" + getName() + "'" + ", pictureUrl='" + getPictureUrl() + "'" + ", pricePerUnit='" + getPricePerUnit() + "'" + ", unit='"
        + getUnit() + "'" + ", code='" + getCode() + "'" + ", description='" + getDescription() + "'" + ", showInShop='" + isShowInShop() + "'" + "}";
  }

}
