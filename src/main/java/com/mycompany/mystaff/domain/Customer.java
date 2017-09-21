package com.mycompany.mystaff.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "customer")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2, max = 50)
    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Size(min = 2, max = 50)
    @Column(name = "midle_name", length = 50)
    private String midleName;

    @Size(min = 2, max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Size(min = 3, max = 60)
    @Column(name = "login", length = 60)
    private String login;

    @Size(min = 6, max = 60)
    @Column(name = "password_hash", length = 60)
    private String passwordHash;

    @Size(min = 5, max = 50)
    @Column(name = "phone", length = 50)
    private String phone;

    @Size(min = 5, max = 100)
    @Column(name = "email", length = 100)
    private String email;

    @Size(max = 100)
    @Column(name = "image_url", length = 100)
    private String imageUrl;

    @NotNull
    @Column(name = "activated", nullable = false)
    private Boolean activated;

    @Size(max = 5)
    @Column(name = "lang_key", length = 5)
    private String langKey;

    @Size(max = 5)
    @Column(name = "activation_key", length = 5)
    private String activationKey;

    @Size(max = 5)
    @Column(name = "reset_key", length = 5)
    private String resetKey;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private ZonedDateTime createdDate;

    @Size(min = 2, max = 50)
    @Column(name = "reset_date", length = 50)
    private String resetDate;

    @NotNull
    @Column(name = "last_modified_by", nullable = false)
    private ZonedDateTime lastModifiedBy;

    @NotNull
    @Column(name = "last_modified_date", nullable = false)
    private ZonedDateTime lastModifiedDate;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @NotNull
    @JoinTable(name = "customer_company",
               joinColumns = @JoinColumn(name="customers_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="companies_id", referencedColumnName="id"))
    private Set<Company> companies = new HashSet<>();

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public Customer firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMidleName() {
        return midleName;
    }

    public Customer midleName(String midleName) {
        this.midleName = midleName;
        return this;
    }

    public void setMidleName(String midleName) {
        this.midleName = midleName;
    }

    public String getLastName() {
        return lastName;
    }

    public Customer lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public Customer login(String login) {
        this.login = login;
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Customer passwordHash(String passwordHash) {
        this.passwordHash = passwordHash;
        return this;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPhone() {
        return phone;
    }

    public Customer phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public Customer email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Customer imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean isActivated() {
        return activated;
    }

    public Customer activated(Boolean activated) {
        this.activated = activated;
        return this;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public String getLangKey() {
        return langKey;
    }

    public Customer langKey(String langKey) {
        this.langKey = langKey;
        return this;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public Customer activationKey(String activationKey) {
        this.activationKey = activationKey;
        return this;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public String getResetKey() {
        return resetKey;
    }

    public Customer resetKey(String resetKey) {
        this.resetKey = resetKey;
        return this;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public Customer createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getResetDate() {
        return resetDate;
    }

    public Customer resetDate(String resetDate) {
        this.resetDate = resetDate;
        return this;
    }

    public void setResetDate(String resetDate) {
        this.resetDate = resetDate;
    }

    public ZonedDateTime getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Customer lastModifiedBy(ZonedDateTime lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public void setLastModifiedBy(ZonedDateTime lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public ZonedDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Customer lastModifiedDate(ZonedDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<Company> getCompanies() {
        return companies;
    }

    public Customer companies(Set<Company> companies) {
        this.companies = companies;
        return this;
    }

    public Customer addCompany(Company company) {
        this.companies.add(company);
        return this;
    }

    public Customer removeCompany(Company company) {
        this.companies.remove(company);
        return this;
    }

    public void setCompanies(Set<Company> companies) {
        this.companies = companies;
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
        Customer customer = (Customer) o;
        if (customer.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", midleName='" + getMidleName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", login='" + getLogin() + "'" +
            ", passwordHash='" + getPasswordHash() + "'" +
            ", phone='" + getPhone() + "'" +
            ", email='" + getEmail() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            ", activated='" + isActivated() + "'" +
            ", langKey='" + getLangKey() + "'" +
            ", activationKey='" + getActivationKey() + "'" +
            ", resetKey='" + getResetKey() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", resetDate='" + getResetDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
