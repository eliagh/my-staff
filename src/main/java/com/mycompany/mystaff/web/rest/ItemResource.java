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
import com.mycompany.mystaff.domain.Item;
import com.mycompany.mystaff.security.jwt.JWTConfigurer;
import com.mycompany.mystaff.security.jwt.TokenProvider;
import com.mycompany.mystaff.service.ItemService;
import com.mycompany.mystaff.service.util.ResolveTokenUtil;
import com.mycompany.mystaff.web.rest.util.HeaderUtil;
import com.mycompany.mystaff.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing Item.
 */
@RestController
@RequestMapping("/api")
public class ItemResource {

  private final Logger log = LoggerFactory.getLogger(ItemResource.class);

  private static final String ENTITY_NAME = "item";

  private final HttpServletRequest request;

  private final ItemService itemService;

  private final TokenProvider tokenProvider;

  public ItemResource(ItemService itemService, HttpServletRequest request, TokenProvider tokenProvider) {
    this.itemService = itemService;
    this.request = request;
    this.tokenProvider = tokenProvider;
  }

  private Company createMyCompany() {
    final Long companyId = tokenProvider.getCompanyId(ResolveTokenUtil.resolveToken(request.getHeader(JWTConfigurer.AUTHORIZATION_HEADER)));
    return new Company(companyId);
  }

  /**
   * POST /items : Create a new item.
   *
   * @param item the item to create
   * @return the ResponseEntity with status 201 (Created) and with body the new item, or with status
   *         400 (Bad Request) if the item has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/items")
  @Timed
  public ResponseEntity<Item> createItem(@Valid @RequestBody Item item) throws URISyntaxException {
    log.debug("REST request to save Item : {}", item);

    if (item.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new item cannot already have an ID")).body(null);
    }

    item.setCompany(createMyCompany());

    Item result = itemService.save(item);
    return ResponseEntity.created(new URI("/api/items/" + result.getId())).headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
  }

  /**
   * PUT /items : Updates an existing item.
   *
   * @param item the item to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated item, or with status
   *         400 (Bad Request) if the item is not valid, or with status 500 (Internal Server Error)
   *         if the item couldn't be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/items")
  @Timed
  public ResponseEntity<Item> updateItem(@Valid @RequestBody Item item) throws URISyntaxException {
    log.debug("REST request to update Item : {}", item);

    if (item.getId() == null) {
      return createItem(item);
    }

    item.setCompany(createMyCompany());

    Item result = itemService.save(item);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, item.getId().toString())).body(result);
  }

  /**
   * GET /items : get all the items.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of items in body
   */
  @GetMapping("/items")
  @Timed
  public ResponseEntity<List<Item>> getAllItems(@ApiParam Pageable pageable) {
    log.debug("REST request to get a page of Items");

    final Long companyId = tokenProvider.getCompanyId(ResolveTokenUtil.resolveToken(request.getHeader(JWTConfigurer.AUTHORIZATION_HEADER)));
    Page<Item> page = itemService.findByCompanyId(pageable, companyId);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/items");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET /items/:id : get the "id" item.
   *
   * @param id the id of the item to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the item, or with status 404 (Not
   *         Found)
   */
  @GetMapping("/items/{id}")
  @Timed
  public ResponseEntity<Item> getItem(@PathVariable Long id) {
    log.debug("REST request to get Item : {}", id);

    final Long companyId = tokenProvider.getCompanyId(ResolveTokenUtil.resolveToken(request.getHeader(JWTConfigurer.AUTHORIZATION_HEADER)));
    Item item = itemService.findByIdAndCompanyId(id, companyId);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(item));
  }

  /**
   * DELETE /items/:id : delete the "id" item.
   *
   * @param id the id of the item to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/items/{id}")
  @Timed
  public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
    log.debug("REST request to delete Item : {}", id);

    final Long companyId = tokenProvider.getCompanyId(ResolveTokenUtil.resolveToken(request.getHeader(JWTConfigurer.AUTHORIZATION_HEADER)));
    itemService.deleteByIdAndCompanyId(id, companyId);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }

  /**
   * SEARCH /_search/items?query=:query : search for the item corresponding to the query.
   *
   * @param query the query of the item search
   * @param pageable the pagination information
   * @return the result of the search
   */
  @GetMapping("/_search/items")
  @Timed
  public ResponseEntity<List<Item>> searchItems(@RequestParam String query, @ApiParam Pageable pageable) {
    log.debug("REST request to search for a page of Items for query {}", query);

    final Long companyId = tokenProvider.getCompanyId(ResolveTokenUtil.resolveToken(request.getHeader(JWTConfigurer.AUTHORIZATION_HEADER)));
    Page<Item> page = itemService.search(query, pageable, companyId);
    HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/items");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

}
