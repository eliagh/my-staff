package com.mycompany.mystaff.service;

import com.mycompany.mystaff.domain.File;
import com.mycompany.mystaff.repository.FileRepository;
import com.mycompany.mystaff.repository.search.FileSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

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
     *  Get all the files.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<File> findAll(Pageable pageable) {
        log.debug("Request to get all Files");
        return fileRepository.findAll(pageable);
    }

    /**
     *  Get one file by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public File findOne(Long id) {
        log.debug("Request to get File : {}", id);
        return fileRepository.findOne(id);
    }

    /**
     *  Delete the  file by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete File : {}", id);
        fileRepository.delete(id);
        fileSearchRepository.delete(id);
    }

    /**
     * Search for the file corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<File> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Files for query {}", query);
        Page<File> result = fileSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
