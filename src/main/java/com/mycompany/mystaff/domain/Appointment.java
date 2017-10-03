package com.mycompany.mystaff.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * A Appointment.
 */
@Entity
@Table(name = "appointment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "appointment")
public class Appointment implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(name = "jhi_when", nullable = false)
  private ZonedDateTime when;

  @Size(min = 2, max = 20)
  @Column(name = "jhi_label", length = 20)
  private String label;

  @NotNull
  @Column(name = "is_recurring", nullable = false)
  private Boolean isRecurring;

  @NotNull
  @Column(name = "is_flexible", nullable = false)
  private Boolean isFlexible;

  @Lob
  @Column(name = "notes")
  private String notes;

  @Column(name = "reminder")
  private String reminder;

  @OneToOne(optional = false)
  @NotNull
  @JoinColumn(unique = true)
  private Customer customer;

  @OneToOne(optional = false)
  @NotNull
  @JoinColumn(unique = true)
  private Activity activityBooked;

  @OneToOne(optional = false)
  @NotNull
  @JoinColumn(unique = true)
  private Location location;

  @OneToOne(optional = false)
  @NotNull
  @JoinColumn(unique = true)
  private User provider;

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ZonedDateTime getWhen() {
    return when;
  }

  public Appointment when(ZonedDateTime when) {
    this.when = when;
    return this;
  }

  public void setWhen(ZonedDateTime when) {
    this.when = when;
  }

  public String getLabel() {
    return label;
  }

  public Appointment label(String label) {
    this.label = label;
    return this;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public Boolean isIsRecurring() {
    return isRecurring;
  }

  public Appointment isRecurring(Boolean isRecurring) {
    this.isRecurring = isRecurring;
    return this;
  }

  public void setIsRecurring(Boolean isRecurring) {
    this.isRecurring = isRecurring;
  }

  public Boolean isIsFlexible() {
    return isFlexible;
  }

  public Appointment isFlexible(Boolean isFlexible) {
    this.isFlexible = isFlexible;
    return this;
  }

  public void setIsFlexible(Boolean isFlexible) {
    this.isFlexible = isFlexible;
  }

  public String getNotes() {
    return notes;
  }

  public Appointment notes(String notes) {
    this.notes = notes;
    return this;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public String getReminder() {
    return reminder;
  }

  public Appointment reminder(String reminder) {
    this.reminder = reminder;
    return this;
  }

  public void setReminder(String reminder) {
    this.reminder = reminder;
  }

  public Customer getCustomer() {
    return customer;
  }

  public Appointment customer(Customer customer) {
    this.customer = customer;
    return this;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public Activity getActivityBooked() {
    return activityBooked;
  }

  public Appointment activityBooked(Activity activity) {
    this.activityBooked = activity;
    return this;
  }

  public void setActivityBooked(Activity activity) {
    this.activityBooked = activity;
  }

  public Location getLocation() {
    return location;
  }

  public Appointment location(Location location) {
    this.location = location;
    return this;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public User getProvider() {
    return provider;
  }

  public Appointment provider(User user) {
    this.provider = user;
    return this;
  }

  public void setProvider(User user) {
    this.provider = user;
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
    Appointment appointment = (Appointment) o;
    if (appointment.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), appointment.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "Appointment{" + "id=" + getId() + ", when='" + getWhen() + "'" + ", label='" + getLabel() + "'" + ", isRecurring='" + isIsRecurring() + "'" + ", isFlexible='"
        + isIsFlexible() + "'" + ", notes='" + getNotes() + "'" + ", reminder='" + getReminder() + "'" + "}";
  }

}
