package com.mycompany.mystaff.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.mystaff.domain.Company;
import com.mycompany.mystaff.repository.CompanyRepository;
import com.mycompany.mystaff.service.dto.CompanyDTO;
import com.mycompany.mystaff.service.mapper.CompanyMapper;

/**
 * Service Implementation for managing Company.
 */
@Service
@Transactional
public class CompanyService {

  private final Logger log = LoggerFactory.getLogger(CompanyService.class);

  private final CompanyRepository companyRepository;

  private final CompanyMapper companyMapper;

  private final MessageSource messageSource;

  public CompanyService(CompanyRepository companyRepository, CompanyMapper companyMapper, MessageSource messageSource) {
    this.companyRepository = companyRepository;
    this.companyMapper = companyMapper;
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
   * @param companyDTO the entity to save
   * @return the persisted entity
   */
  public CompanyDTO save(CompanyDTO companyDTO) {
    log.debug("Request to save Company : {}", companyDTO);

    Company company = companyMapper.toEntity(companyDTO);
    company = companyRepository.save(company);
    CompanyDTO result = companyMapper.toDto(company);

    return result;
  }

  /**
   * Get all the companies.
   *
   * @return the list of entities
   */
  @Transactional(readOnly = true)
  public List<CompanyDTO> findAll() {
    log.debug("Request to get all Companies");

    return companyRepository.findAll().stream().map(companyMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one company by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Transactional(readOnly = true)
  public CompanyDTO findOne(Long id) {
    log.debug("Request to get Company : {}", id);

    Company company = companyRepository.findOne(id);
    return companyMapper.toDto(company);
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
