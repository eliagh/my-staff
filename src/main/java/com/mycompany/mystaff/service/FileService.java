package com.mycompany.mystaff.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.mystaff.domain.File;
import com.mycompany.mystaff.repository.FileRepository;
import com.mycompany.mystaff.repository.search.FileSearchRepository;

/**
 * Service Implementation for managing File.
 */
@Service
@Transactional
public class FileService {

  private final Logger log = LoggerFactory.getLogger(FileService.class);

  private final FileRepository fileRepository;

  private final FileSearchRepository fileSearchRepository;

  public FileService(FileRepository fileRepository, FileSearchRepository fileSearchRepository) {
    this.fileRepository = fileRepository;
    this.fileSearchRepository = fileSearchRepository;
  }

  /**
   * Save a file.
   *
   * @param file the entity to save
   * @return the persisted entity
   */
  public File save(File file) {
    log.debug("Request to save File : {}", file);

    File result = fileRepository.save(file);
    fileSearchRepository.save(result);
    return result;
  }

  /**
   * Get all the files.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Transactional(readOnly = true)
  public Page<File> findByCompanyId(Pageable pageable, Long companyId) {
    log.debug("Request to get all Files by CompanyId : {}", companyId);

    return fileRepository.findByCompanyId(pageable, companyId);
  }

  /**
   * Get one file by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Transactional(readOnly = true)
  public File findByIdAndCompanyId(Long id, Long companyId) {
    log.debug("Request to get File : {}, CompanyId : {}", id, companyId);

    return fileRepository.findByIdAndCompanyId(id, companyId);
  }

  /**
   * Delete the file by id.
   *
   * @param id the id of the entity
   */
  public void deleteByIdAndCompanyId(Long id, Long companyId) {
    log.debug("Request to delete File : {}, CompanyId : {}", id, companyId);

    fileRepository.deleteByIdAndCompanyId(id, companyId);
    fileSearchRepository.deleteByIdAndCompanyId(id, companyId);
  }

  /**
   * Search for the file corresponding to the query.
   *
   * @param query the query of the search
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Transactional(readOnly = true)
  public Page<File> search(String query, Pageable pageable, Long companyId) {
    log.debug("Request to search for a page of Files for query {}", query);

    Page<File> result = fileSearchRepository.search(queryStringQuery(query), pageable);
    return result;
  }

}
