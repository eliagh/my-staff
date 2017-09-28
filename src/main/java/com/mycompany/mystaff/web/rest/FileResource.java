package com.mycompany.mystaff.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.mystaff.domain.Company;
import com.mycompany.mystaff.domain.File;
import com.mycompany.mystaff.security.jwt.JWTConfigurer;
import com.mycompany.mystaff.security.jwt.TokenProvider;
import com.mycompany.mystaff.service.FileService;
import com.mycompany.mystaff.service.util.ResolveTokenUtil;
import com.mycompany.mystaff.web.rest.util.HeaderUtil;
import com.mycompany.mystaff.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing File.
 */
@RestController
@RequestMapping("/api")
public class FileResource {

  private final Logger log = LoggerFactory.getLogger(FileResource.class);

  private static final String ENTITY_NAME = "file";

  private final HttpServletRequest request;

  private final FileService fileService;

  private final TokenProvider tokenProvider;

  public FileResource(FileService fileService, HttpServletRequest request, TokenProvider tokenProvider) {
    this.fileService = fileService;
    this.request = request;
    this.tokenProvider = tokenProvider;
  }

  private Company createMyCompany() {
    final Long companyId = tokenProvider.getCompanyId(ResolveTokenUtil.resolveToken(request.getHeader(JWTConfigurer.AUTHORIZATION_HEADER)));
    return new Company(companyId);
  }

  /**
   * POST /files : Create a new file.
   *
   * @param file the file to create
   * @return the ResponseEntity with status 201 (Created) and with body the new file, or with status
   *         400 (Bad Request) if the file has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/files")
  @Timed
  public ResponseEntity<File> createFile(@Valid @RequestBody File file) throws URISyntaxException {
    log.debug("REST request to save File : {}", file);

    if (file.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new file cannot already have an ID")).body(null);
    }

    file.setCompany(createMyCompany());

    File result = fileService.save(file);
    return ResponseEntity.created(new URI("/api/files/" + result.getId())).headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
  }

  /**
   * PUT /files : Updates an existing file.
   *
   * @param file the file to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated file, or with status
   *         400 (Bad Request) if the file is not valid, or with status 500 (Internal Server Error)
   *         if the file couldn't be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/files")
  @Timed
  public ResponseEntity<File> updateFile(@Valid @RequestBody File file) throws URISyntaxException {
    log.debug("REST request to update File : {}", file);

    if (file.getId() == null) {
      return createFile(file);
    }

    file.setCompany(createMyCompany());

    File result = fileService.save(file);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, file.getId().toString())).body(result);
  }

  /**
   * GET /files : get all the files.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of files in body
   */
  @GetMapping("/files")
  @Timed
  public ResponseEntity<List<File>> getAllFiles(@ApiParam Pageable pageable) {
    log.debug("REST request to get a page of Files");

    final Long companyId = tokenProvider.getCompanyId(ResolveTokenUtil.resolveToken(request.getHeader(JWTConfigurer.AUTHORIZATION_HEADER)));
    Page<File> page = fileService.findByCompanyId(pageable, companyId);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/files");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET /files/:id : get the "id" file.
   *
   * @param id the id of the file to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the file, or with status 404 (Not
   *         Found)
   */
  @GetMapping("/files/{id}")
  @Timed
  public ResponseEntity<File> getFile(@PathVariable Long id) {
    log.debug("REST request to get File : {}", id);

    final Long companyId = tokenProvider.getCompanyId(ResolveTokenUtil.resolveToken(request.getHeader(JWTConfigurer.AUTHORIZATION_HEADER)));
    File file = fileService.findByIdAndCompanyId(id, companyId);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(file));
  }

  /**
   * DELETE /files/:id : delete the "id" file.
   *
   * @param id the id of the file to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/files/{id}")
  @Timed
  public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
    log.debug("REST request to delete File : {}", id);

    final Long companyId = tokenProvider.getCompanyId(ResolveTokenUtil.resolveToken(request.getHeader(JWTConfigurer.AUTHORIZATION_HEADER)));
    fileService.deleteByIdAndCompanyId(id, companyId);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }

  /**
   * SEARCH /_search/files?query=:query : search for the file corresponding to the query.
   *
   * @param query the query of the file search
   * @param pageable the pagination information
   * @return the result of the search
   */
  @GetMapping("/_search/files")
  @Timed
  public ResponseEntity<List<File>> searchFiles(@RequestParam String query, @ApiParam Pageable pageable) {
    log.debug("REST request to search for a page of Files for query {}", query);

    final Long companyId = tokenProvider.getCompanyId(ResolveTokenUtil.resolveToken(request.getHeader(JWTConfigurer.AUTHORIZATION_HEADER)));
    Page<File> page = fileService.search(query, pageable, companyId);
    HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/files");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

}
