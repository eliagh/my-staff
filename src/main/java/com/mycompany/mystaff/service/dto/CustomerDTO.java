package com.mycompany.mystaff.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A DTO for the Customer entity.
 */
public class CustomerDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  @NotNull
  @Size(min = 2, max = 50)
  private String firstName;

  @Size(min = 2, max = 50)
  private String midleName;

  @Size(min = 2, max = 50)
  private String lastName;

  @Size(min = 3, max = 60)
  private String login;

  @Size(min = 6, max = 60)
  private String passwordHash;

  @Size(min = 5, max = 50)
  private String phone;

  @Size(min = 5, max = 100)
  private String email;

  @Size(max = 100)
  private String imageUrl;

  @NotNull
  private Boolean activated;

  @Size(max = 5)
  private String langKey;

  @Size(max = 5)
  private String activationKey;

  @Size(max = 5)
  private String resetKey;

  @NotNull
  private ZonedDateTime createdDate;

  private ZonedDateTime resetDate;

  @Size(min = 2, max = 50)
  private String lastModifiedBy;

  private ZonedDateTime lastModifiedDate;

  private Set<CompanyDTO> companies = new HashSet<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getMidleName() {
    return midleName;
  }

  public void setMidleName(String midleName) {
    this.midleName = midleName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public Boolean isActivated() {
    return activated;
  }

  public void setActivated(Boolean activated) {
    this.activated = activated;
  }

  public String getLangKey() {
    return langKey;
  }

  public void setLangKey(String langKey) {
    this.langKey = langKey;
  }

  public String getActivationKey() {
    return activationKey;
  }

  public void setActivationKey(String activationKey) {
    this.activationKey = activationKey;
  }

  public String getResetKey() {
    return resetKey;
  }

  public void setResetKey(String resetKey) {
    this.resetKey = resetKey;
  }

  public ZonedDateTime getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(ZonedDateTime createdDate) {
    this.createdDate = createdDate;
  }

  public ZonedDateTime getResetDate() {
    return resetDate;
  }

  public void setResetDate(ZonedDateTime resetDate) {
    this.resetDate = resetDate;
  }

  public String getLastModifiedBy() {
    return lastModifiedBy;
  }

  public void setLastModifiedBy(String lastModifiedBy) {
    this.lastModifiedBy = lastModifiedBy;
  }

  public ZonedDateTime getLastModifiedDate() {
    return lastModifiedDate;
  }

  public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }

  public Set<CompanyDTO> getCompanies() {
    return companies;
  }

  public void setCompanies(Set<CompanyDTO> companies) {
    this.companies = companies;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CustomerDTO customerDTO = (CustomerDTO) o;
    if (customerDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), customerDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "CustomerDTO{" + "id=" + getId() + ", firstName='" + getFirstName() + "'" + ", midleName='" + getMidleName() + "'" + ", lastName='" + getLastName() + "'" + ", login='"
        + getLogin() + "'" + ", passwordHash='" + getPasswordHash() + "'" + ", phone='" + getPhone() + "'" + ", email='" + getEmail() + "'" + ", imageUrl='" + getImageUrl() + "'"
        + ", activated='" + isActivated() + "'" + ", langKey='" + getLangKey() + "'" + ", activationKey='" + getActivationKey() + "'" + ", resetKey='" + getResetKey() + "'"
        + ", createdDate='" + getCreatedDate() + "'" + ", resetDate='" + getResetDate() + "'" + ", lastModifiedBy='" + getLastModifiedBy() + "'" + ", lastModifiedDate='"
        + getLastModifiedDate() + "'" + "}";
  }

}
