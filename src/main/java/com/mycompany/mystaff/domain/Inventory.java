package com.mycompany.mystaff.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * A Inventory.
 */
@Entity
@Table(name = "inventory")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "inventory")
public class Inventory implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @DecimalMin(value = "0")
  @Column(name = "quantity")
  private Double quantity;

  @NotNull
  @Column(name = "inventory_date", nullable = false)
  private LocalDate inventoryDate;

  @OneToOne(optional = false)
  @NotNull
  @JoinColumn(unique = true)
  private Location location;

  @OneToOne(optional = false)
  @NotNull
  @JoinColumn(unique = true)
  private Item item;

  // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Double getQuantity() {
    return quantity;
  }

  public Inventory quantity(Double quantity) {
    this.quantity = quantity;
    return this;
  }

  public void setQuantity(Double quantity) {
    this.quantity = quantity;
  }

  public LocalDate getInventoryDate() {
    return inventoryDate;
  }

  public Inventory inventoryDate(LocalDate inventoryDate) {
    this.inventoryDate = inventoryDate;
    return this;
  }

  public void setInventoryDate(LocalDate inventoryDate) {
    this.inventoryDate = inventoryDate;
  }

  public Location getLocation() {
    return location;
  }

  public Inventory location(Location location) {
    this.location = location;
    return this;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public Item getItem() {
    return item;
  }

  public Inventory item(Item item) {
    this.item = item;
    return this;
  }

  public void setItem(Item item) {
    this.item = item;
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
    Inventory inventory = (Inventory) o;
    if (inventory.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), inventory.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "Inventory{" + "id=" + getId() + ", quantity='" + getQuantity() + "'" + ", inventoryDate='" + getInventoryDate() + "'" + "}";
  }

}
