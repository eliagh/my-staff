package com.mycompany.mystaff.service;

import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.mystaff.domain.Company;
import com.mycompany.mystaff.repository.CompanyRepository;

/**
 * Service Implementation for managing Company.
 */
@Service
@Transactional
public class CompanyService {

  private final Logger log = LoggerFactory.getLogger(CompanyService.class);

  private final CompanyRepository companyRepository;

  private final MessageSource messageSource;

  public CompanyService(CompanyRepository companyRepository, MessageSource messageSource) {
    this.companyRepository = companyRepository;
    this.messageSource = messageSource;
  }

  /**
   * Create a company.
   *
   * @param company the entity to save
   * @return the persisted entity
   */
  public Company create(String langKey) {
    log.debug("Request to create Company");
    Locale locale = Locale.forLanguageTag(langKey);
    String companyDefaultName = messageSource.getMessage("company.defaulet.name", null, locale);
    String companyDefaultThema = messageSource.getMessage("company.defaulet.thema", null, locale);
    Company company = new Company();
    company.setName(companyDefaultName);
    company.setThema(companyDefaultThema);
    return companyRepository.save(company);
  }

  /**
   * Save a company.
   *
   * @param company the entity to save
   * @return the persisted entity
   */
  public Company save(Company company) {
    log.debug("Request to save Company : {}", company);
    return companyRepository.save(company);
  }

  /**
   * Get all the companies.
   *
   * @return the list of entities
   */
  @Transactional(readOnly = true)
  public List<Company> findAll() {
    log.debug("Request to get all Companies");
    return companyRepository.findAll();
  }

  /**
   * Get one company by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Transactional(readOnly = true)
  public Company findOne(Long id) {
    log.debug("Request to get Company : {}", id);
    return companyRepository.findOne(id);
  }

  /**
   * Delete the company by id.
   *
   * @param id the id of the entity
   */
  public void delete(Long id) {
    log.debug("Request to delete Company : {}", id);
    companyRepository.delete(id);
  }

}
