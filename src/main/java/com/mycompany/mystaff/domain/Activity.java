package com.mycompany.mystaff.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * A Activity.
 */
@Entity
@Table(name = "activity")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "activity")
public class Activity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Size(min = 2, max = 50)
  @Column(name = "name", length = 50, nullable = false)
  private String name;

  @Lob
  @Column(name = "logo")
  private byte[] logo;

  @Column(name = "logo_content_type")
  private String logoContentType;

  @Lob
  @Column(name = "description")
  private String description;

  @DecimalMin(value = "0")
  @Column(name = "price")
  private Double price;

  @NotNull
  @Column(name = "duration_minutes", nullable = false)
  private Integer durationMinutes;

  @Column(name = "pre_duration_minutes")
  private Integer preDurationMinutes;

  @Column(name = "post_duration_minutes")
  private Integer postDurationMinutes;

  @NotNull
  @Column(name = "is_nrivate", nullable = false)
  private Boolean isNrivate;

  @NotNull
  @Column(name = "color_code", nullable = false)
  private String colorCode;

  @Column(name = "cancellation_time")
  private Long cancellationTime;

  @ManyToMany
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  @JoinTable(
      name = "activity_location",
      joinColumns = @JoinColumn(name = "activities_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "locations_id", referencedColumnName = "id"))
  private Set<Location> locations = new HashSet<>();

  @ManyToMany
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  @JoinTable(
      name = "activity_category",
      joinColumns = @JoinColumn(name = "activities_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "categories_id", referencedColumnName = "id"))
  private Set<Category> categories = new HashSet<>();

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

  public Activity name(String name) {
    this.name = name;
    return this;
  }

  public void setName(String name) {
    this.name = name;
  }

  public byte[] getLogo() {
    return logo;
  }

  public Activity logo(byte[] logo) {
    this.logo = logo;
    return this;
  }

  public void setLogo(byte[] logo) {
    this.logo = logo;
  }

  public String getLogoContentType() {
    return logoContentType;
  }

  public Activity logoContentType(String logoContentType) {
    this.logoContentType = logoContentType;
    return this;
  }

  public void setLogoContentType(String logoContentType) {
    this.logoContentType = logoContentType;
  }

  public String getDescription() {
    return description;
  }

  public Activity description(String description) {
    this.description = description;
    return this;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Double getPrice() {
    return price;
  }

  public Activity price(Double price) {
    this.price = price;
    return this;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public Integer getDurationMinutes() {
    return durationMinutes;
  }

  public Activity durationMinutes(Integer durationMinutes) {
    this.durationMinutes = durationMinutes;
    return this;
  }

  public void setDurationMinutes(Integer durationMinutes) {
    this.durationMinutes = durationMinutes;
  }

  public Integer getPreDurationMinutes() {
    return preDurationMinutes;
  }

  public Activity preDurationMinutes(Integer preDurationMinutes) {
    this.preDurationMinutes = preDurationMinutes;
    return this;
  }

  public void setPreDurationMinutes(Integer preDurationMinutes) {
    this.preDurationMinutes = preDurationMinutes;
  }

  public Integer getPostDurationMinutes() {
    return postDurationMinutes;
  }

  public Activity postDurationMinutes(Integer postDurationMinutes) {
    this.postDurationMinutes = postDurationMinutes;
    return this;
  }

  public void setPostDurationMinutes(Integer postDurationMinutes) {
    this.postDurationMinutes = postDurationMinutes;
  }

  public Boolean isIsNrivate() {
    return isNrivate;
  }

  public Activity isNrivate(Boolean isNrivate) {
    this.isNrivate = isNrivate;
    return this;
  }

  public void setIsNrivate(Boolean isNrivate) {
    this.isNrivate = isNrivate;
  }

  public String getColorCode() {
    return colorCode;
  }

  public Activity colorCode(String colorCode) {
    this.colorCode = colorCode;
    return this;
  }

  public void setColorCode(String colorCode) {
    this.colorCode = colorCode;
  }

  public Long getCancellationTime() {
    return cancellationTime;
  }

  public Activity cancellationTime(Long cancellationTime) {
    this.cancellationTime = cancellationTime;
    return this;
  }

  public void setCancellationTime(Long cancellationTime) {
    this.cancellationTime = cancellationTime;
  }

  public Set<Location> getLocations() {
    return locations;
  }

  public Activity locations(Set<Location> locations) {
    this.locations = locations;
    return this;
  }

  public Activity addLocation(Location location) {
    this.locations.add(location);
    return this;
  }

  public Activity removeLocation(Location location) {
    this.locations.remove(location);
    return this;
  }

  public void setLocations(Set<Location> locations) {
    this.locations = locations;
  }

  public Set<Category> getCategories() {
    return categories;
  }

  public Activity categories(Set<Category> categories) {
    this.categories = categories;
    return this;
  }

  public Activity addCategory(Category category) {
    this.categories.add(category);
    return this;
  }

  public Activity removeCategory(Category category) {
    this.categories.remove(category);
    return this;
  }

  public void setCategories(Set<Category> categories) {
    this.categories = categories;
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
    Activity activity = (Activity) o;
    if (activity.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), activity.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "Activity{" + "id=" + getId() + ", name='" + getName() + "'" + ", logo='" + getLogo() + "'" + ", logoContentType='" + logoContentType + "'" + ", description='"
        + getDescription() + "'" + ", price='" + getPrice() + "'" + ", durationMinutes='" + getDurationMinutes() + "'" + ", preDurationMinutes='" + getPreDurationMinutes() + "'"
        + ", postDurationMinutes='" + getPostDurationMinutes() + "'" + ", isNrivate='" + isIsNrivate() + "'" + ", colorCode='" + getColorCode() + "'" + ", cancellationTime='"
        + getCancellationTime() + "'" + "}";
  }

}
