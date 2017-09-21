package com.mycompany.mystaff.web.rest;

import com.mycompany.mystaff.MystaffApp;

import com.mycompany.mystaff.domain.Inventory;
import com.mycompany.mystaff.domain.Location;
import com.mycompany.mystaff.domain.Item;
import com.mycompany.mystaff.repository.InventoryRepository;
import com.mycompany.mystaff.service.InventoryService;
import com.mycompany.mystaff.repository.search.InventorySearchRepository;
import com.mycompany.mystaff.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the InventoryResource REST controller.
 *
 * @see InventoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MystaffApp.class)
public class InventoryResourceIntTest {

    private static final Double DEFAULT_QUANTITY = 0D;
    private static final Double UPDATED_QUANTITY = 1D;

    private static final LocalDate DEFAULT_INVENTORY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INVENTORY_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventorySearchRepository inventorySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInventoryMockMvc;

    private Inventory inventory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InventoryResource inventoryResource = new InventoryResource(inventoryService);
        this.restInventoryMockMvc = MockMvcBuilders.standaloneSetup(inventoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inventory createEntity(EntityManager em) {
        Inventory inventory = new Inventory()
            .quantity(DEFAULT_QUANTITY)
            .inventoryDate(DEFAULT_INVENTORY_DATE);
        // Add required entity
        Location location = LocationResourceIntTest.createEntity(em);
        em.persist(location);
        em.flush();
        inventory.setLocation(location);
        // Add required entity
        Item item = ItemResourceIntTest.createEntity(em);
        em.persist(item);
        em.flush();
        inventory.setItem(item);
        return inventory;
    }

    @Before
    public void initTest() {
        inventorySearchRepository.deleteAll();
        inventory = createEntity(em);
    }

    @Test
    @Transactional
    public void createInventory() throws Exception {
        int databaseSizeBeforeCreate = inventoryRepository.findAll().size();

        // Create the Inventory
        restInventoryMockMvc.perform(post("/api/inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inventory)))
            .andExpect(status().isCreated());

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeCreate + 1);
        Inventory testInventory = inventoryList.get(inventoryList.size() - 1);
        assertThat(testInventory.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testInventory.getInventoryDate()).isEqualTo(DEFAULT_INVENTORY_DATE);

        // Validate the Inventory in Elasticsearch
        Inventory inventoryEs = inventorySearchRepository.findOne(testInventory.getId());
        assertThat(inventoryEs).isEqualToComparingFieldByField(testInventory);
    }

    @Test
    @Transactional
    public void createInventoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = inventoryRepository.findAll().size();

        // Create the Inventory with an existing ID
        inventory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInventoryMockMvc.perform(post("/api/inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inventory)))
            .andExpect(status().isBadRequest());

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkInventoryDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = inventoryRepository.findAll().size();
        // set the field null
        inventory.setInventoryDate(null);

        // Create the Inventory, which fails.

        restInventoryMockMvc.perform(post("/api/inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inventory)))
            .andExpect(status().isBadRequest());

        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInventories() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList
        restInventoryMockMvc.perform(get("/api/inventories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inventory.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.doubleValue())))
            .andExpect(jsonPath("$.[*].inventoryDate").value(hasItem(DEFAULT_INVENTORY_DATE.toString())));
    }

    @Test
    @Transactional
    public void getInventory() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get the inventory
        restInventoryMockMvc.perform(get("/api/inventories/{id}", inventory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(inventory.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.doubleValue()))
            .andExpect(jsonPath("$.inventoryDate").value(DEFAULT_INVENTORY_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInventory() throws Exception {
        // Get the inventory
        restInventoryMockMvc.perform(get("/api/inventories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInventory() throws Exception {
        // Initialize the database
        inventoryService.save(inventory);

        int databaseSizeBeforeUpdate = inventoryRepository.findAll().size();

        // Update the inventory
        Inventory updatedInventory = inventoryRepository.findOne(inventory.getId());
        updatedInventory
            .quantity(UPDATED_QUANTITY)
            .inventoryDate(UPDATED_INVENTORY_DATE);

        restInventoryMockMvc.perform(put("/api/inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedInventory)))
            .andExpect(status().isOk());

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate);
        Inventory testInventory = inventoryList.get(inventoryList.size() - 1);
        assertThat(testInventory.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testInventory.getInventoryDate()).isEqualTo(UPDATED_INVENTORY_DATE);

        // Validate the Inventory in Elasticsearch
        Inventory inventoryEs = inventorySearchRepository.findOne(testInventory.getId());
        assertThat(inventoryEs).isEqualToComparingFieldByField(testInventory);
    }

    @Test
    @Transactional
    public void updateNonExistingInventory() throws Exception {
        int databaseSizeBeforeUpdate = inventoryRepository.findAll().size();

        // Create the Inventory

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInventoryMockMvc.perform(put("/api/inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inventory)))
            .andExpect(status().isCreated());

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteInventory() throws Exception {
        // Initialize the database
        inventoryService.save(inventory);

        int databaseSizeBeforeDelete = inventoryRepository.findAll().size();

        // Get the inventory
        restInventoryMockMvc.perform(delete("/api/inventories/{id}", inventory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean inventoryExistsInEs = inventorySearchRepository.exists(inventory.getId());
        assertThat(inventoryExistsInEs).isFalse();

        // Validate the database is empty
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInventory() throws Exception {
        // Initialize the database
        inventoryService.save(inventory);

        // Search the inventory
        restInventoryMockMvc.perform(get("/api/_search/inventories?query=id:" + inventory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inventory.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.doubleValue())))
            .andExpect(jsonPath("$.[*].inventoryDate").value(hasItem(DEFAULT_INVENTORY_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Inventory.class);
        Inventory inventory1 = new Inventory();
        inventory1.setId(1L);
        Inventory inventory2 = new Inventory();
        inventory2.setId(inventory1.getId());
        assertThat(inventory1).isEqualTo(inventory2);
        inventory2.setId(2L);
        assertThat(inventory1).isNotEqualTo(inventory2);
        inventory1.setId(null);
        assertThat(inventory1).isNotEqualTo(inventory2);
    }
}
