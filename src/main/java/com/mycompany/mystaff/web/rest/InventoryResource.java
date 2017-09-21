package com.mycompany.mystaff.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.mystaff.domain.Inventory;
import com.mycompany.mystaff.service.InventoryService;
import com.mycompany.mystaff.web.rest.util.HeaderUtil;
import com.mycompany.mystaff.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Inventory.
 */
@RestController
@RequestMapping("/api")
public class InventoryResource {

    private final Logger log = LoggerFactory.getLogger(InventoryResource.class);

    private static final String ENTITY_NAME = "inventory";

    private final InventoryService inventoryService;

    public InventoryResource(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    /**
     * POST  /inventories : Create a new inventory.
     *
     * @param inventory the inventory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new inventory, or with status 400 (Bad Request) if the inventory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/inventories")
    @Timed
    public ResponseEntity<Inventory> createInventory(@Valid @RequestBody Inventory inventory) throws URISyntaxException {
        log.debug("REST request to save Inventory : {}", inventory);
        if (inventory.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new inventory cannot already have an ID")).body(null);
        }
        Inventory result = inventoryService.save(inventory);
        return ResponseEntity.created(new URI("/api/inventories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /inventories : Updates an existing inventory.
     *
     * @param inventory the inventory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated inventory,
     * or with status 400 (Bad Request) if the inventory is not valid,
     * or with status 500 (Internal Server Error) if the inventory couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/inventories")
    @Timed
    public ResponseEntity<Inventory> updateInventory(@Valid @RequestBody Inventory inventory) throws URISyntaxException {
        log.debug("REST request to update Inventory : {}", inventory);
        if (inventory.getId() == null) {
            return createInventory(inventory);
        }
        Inventory result = inventoryService.save(inventory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, inventory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /inventories : get all the inventories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of inventories in body
     */
    @GetMapping("/inventories")
    @Timed
    public ResponseEntity<List<Inventory>> getAllInventories(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Inventories");
        Page<Inventory> page = inventoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/inventories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /inventories/:id : get the "id" inventory.
     *
     * @param id the id of the inventory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the inventory, or with status 404 (Not Found)
     */
    @GetMapping("/inventories/{id}")
    @Timed
    public ResponseEntity<Inventory> getInventory(@PathVariable Long id) {
        log.debug("REST request to get Inventory : {}", id);
        Inventory inventory = inventoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(inventory));
    }

    /**
     * DELETE  /inventories/:id : delete the "id" inventory.
     *
     * @param id the id of the inventory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/inventories/{id}")
    @Timed
    public ResponseEntity<Void> deleteInventory(@PathVariable Long id) {
        log.debug("REST request to delete Inventory : {}", id);
        inventoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/inventories?query=:query : search for the inventory corresponding
     * to the query.
     *
     * @param query the query of the inventory search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/inventories")
    @Timed
    public ResponseEntity<List<Inventory>> searchInventories(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Inventories for query {}", query);
        Page<Inventory> page = inventoryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/inventories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
