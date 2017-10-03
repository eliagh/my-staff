package com.mycompany.mystaff.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import com.mycompany.mystaff.domain.enumeration.FileType;

/**
 * A File.
 */
@Entity
@Table(name = "file")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "file")
public class File implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Size(min = 2, max = 30)
  @Column(name = "name", length = 30)
  private String name;

  @Column(name = "url")
  private String url;

  @Enumerated(EnumType.STRING)
  @Column(name = "file_type")
  private FileType fileType;

  @Lob
  @Column(name = "jhi_file")
  private byte[] file;

  @Column(name = "jhi_file_content_type")
  private String fileContentType;

  @ManyToOne(optional = false)
  @NotNull
  private Company company;

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

  public File name(String name) {
    this.name = name;
    return this;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUrl() {
    return url;
  }

  public File url(String url) {
    this.url = url;
    return this;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public FileType getFileType() {
    return fileType;
  }

  public File fileType(FileType fileType) {
    this.fileType = fileType;
    return this;
  }

  public void setFileType(FileType fileType) {
    this.fileType = fileType;
  }

  public byte[] getFile() {
    return file;
  }

  public File file(byte[] file) {
    this.file = file;
    return this;
  }

  public void setFile(byte[] file) {
    this.file = file;
  }

  public String getFileContentType() {
    return fileContentType;
  }

  public File fileContentType(String fileContentType) {
    this.fileContentType = fileContentType;
    return this;
  }

  public void setFileContentType(String fileContentType) {
    this.fileContentType = fileContentType;
  }

  public Company getCompany() {
    return company;
  }

  public File company(Company company) {
    this.company = company;
    return this;
  }

  public void setCompany(Company company) {
    this.company = company;
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
    File file = (File) o;
    if (file.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), file.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "File{" + "id=" + getId() + ", name='" + getName() + "'" + ", url='" + getUrl() + "'" + ", fileType='" + getFileType() + "'" + ", file='" + getFile() + "'"
        + ", fileContentType='" + fileContentType + "'" + "}";
  }

}
