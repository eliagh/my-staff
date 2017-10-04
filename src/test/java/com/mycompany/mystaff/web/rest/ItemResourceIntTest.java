package com.mycompany.mystaff.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

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

import com.mycompany.mystaff.MystaffApp;
import com.mycompany.mystaff.domain.Company;
import com.mycompany.mystaff.domain.Item;
import com.mycompany.mystaff.repository.ItemRepository;
import com.mycompany.mystaff.repository.search.ItemSearchRepository;
import com.mycompany.mystaff.security.jwt.TokenProvider;
import com.mycompany.mystaff.service.ItemService;
import com.mycompany.mystaff.service.dto.ItemDTO;
import com.mycompany.mystaff.service.mapper.ItemMapper;
import com.mycompany.mystaff.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the ItemResource REST controller.
 *
 * @see ItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MystaffApp.class)
public class ItemResourceIntTest {

  private static final String DEFAULT_NAME = "AAAAAAAAAA";
  private static final String UPDATED_NAME = "BBBBBBBBBB";

  private static final String DEFAULT_PICTURE_URL = "AAAAAAAAAA";
  private static final String UPDATED_PICTURE_URL = "BBBBBBBBBB";

  private static final Double DEFAULT_PRICE_PER_UNIT = 0D;
  private static final Double UPDATED_PRICE_PER_UNIT = 1D;

  private static final String DEFAULT_UNIT = "AAAAAAAAAA";
  private static final String UPDATED_UNIT = "BBBBBBBBBB";

  private static final String DEFAULT_CODE = "AAAAAAAAAA";
  private static final String UPDATED_CODE = "BBBBBBBBBB";

  private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
  private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

  private static final Boolean DEFAULT_SHOW_IN_SHOP = false;
  private static final Boolean UPDATED_SHOW_IN_SHOP = true;

  @Autowired
  private ItemRepository itemRepository;

  @Autowired
  private ItemMapper itemMapper;

  @Autowired
  private ItemService itemService;

  @Autowired
  private ItemSearchRepository itemSearchRepository;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  @Autowired
  private TokenProvider tokenProvider;

  @Autowired
  private HttpServletRequest request;

  private MockMvc restItemMockMvc;

  private Item item;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final ItemResource itemResource = new ItemResource(itemService, request, tokenProvider);
    this.restItemMockMvc = MockMvcBuilders.standaloneSetup(itemResource).setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  /**
   * Create an entity for this test.
   *
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static Item createEntity(EntityManager em) {
    Item item = new Item().name(DEFAULT_NAME).pictureUrl(DEFAULT_PICTURE_URL).pricePerUnit(DEFAULT_PRICE_PER_UNIT).unit(DEFAULT_UNIT).code(DEFAULT_CODE)
        .description(DEFAULT_DESCRIPTION).showInShop(DEFAULT_SHOW_IN_SHOP);
    // Add required entity
    Company company = CompanyResourceIntTest.createEntity(em);
    em.persist(company);
    em.flush();
    item.setCompanyId(company.getId());
    return item;
  }

  @Before
  public void initTest() {
    itemSearchRepository.deleteAll();
    item = createEntity(em);
  }

  @Test
  @Transactional
  public void createItem() throws Exception {
    int databaseSizeBeforeCreate = itemRepository.findAll().size();

    // Create the Item
    ItemDTO itemDTO = itemMapper.toDto(item);
    restItemMockMvc.perform(post("/api/items").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(itemDTO))).andExpect(status().isCreated());

    // Validate the Item in the database
    List<Item> itemList = itemRepository.findAll();
    assertThat(itemList).hasSize(databaseSizeBeforeCreate + 1);
    Item testItem = itemList.get(itemList.size() - 1);
    assertThat(testItem.getName()).isEqualTo(DEFAULT_NAME);
    assertThat(testItem.getPictureUrl()).isEqualTo(DEFAULT_PICTURE_URL);
    assertThat(testItem.getPricePerUnit()).isEqualTo(DEFAULT_PRICE_PER_UNIT);
    assertThat(testItem.getUnit()).isEqualTo(DEFAULT_UNIT);
    assertThat(testItem.getCode()).isEqualTo(DEFAULT_CODE);
    assertThat(testItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    assertThat(testItem.isShowInShop()).isEqualTo(DEFAULT_SHOW_IN_SHOP);

    // Validate the Item in Elasticsearch
    Item itemEs = itemSearchRepository.findOne(testItem.getId());
    assertThat(itemEs).isEqualToComparingFieldByField(testItem);
  }

  @Test
  @Transactional
  public void createItemWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = itemRepository.findAll().size();

    // Create the Item with an existing ID
    item.setId(1L);
    ItemDTO itemDTO = itemMapper.toDto(item);

    // An entity with an existing ID cannot be created, so this API call must fail
    restItemMockMvc.perform(post("/api/items").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(itemDTO))).andExpect(status().isBadRequest());

    // Validate the Item in the database
    List<Item> itemList = itemRepository.findAll();
    assertThat(itemList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkNameIsRequired() throws Exception {
    int databaseSizeBeforeTest = itemRepository.findAll().size();
    // set the field null
    item.setName(null);

    // Create the Item, which fails.
    ItemDTO itemDTO = itemMapper.toDto(item);

    restItemMockMvc.perform(post("/api/items").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(itemDTO))).andExpect(status().isBadRequest());

    List<Item> itemList = itemRepository.findAll();
    assertThat(itemList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkUnitIsRequired() throws Exception {
    int databaseSizeBeforeTest = itemRepository.findAll().size();
    // set the field null
    item.setUnit(null);

    // Create the Item, which fails.
    ItemDTO itemDTO = itemMapper.toDto(item);

    restItemMockMvc.perform(post("/api/items").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(itemDTO))).andExpect(status().isBadRequest());

    List<Item> itemList = itemRepository.findAll();
    assertThat(itemList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkShowInShopIsRequired() throws Exception {
    int databaseSizeBeforeTest = itemRepository.findAll().size();
    // set the field null
    item.setShowInShop(null);

    // Create the Item, which fails.
    ItemDTO itemDTO = itemMapper.toDto(item);

    restItemMockMvc.perform(post("/api/items").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(itemDTO))).andExpect(status().isBadRequest());

    List<Item> itemList = itemRepository.findAll();
    assertThat(itemList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllItems() throws Exception {
    // Initialize the database
    itemRepository.saveAndFlush(item);

    // Get all the itemList
    restItemMockMvc.perform(get("/api/items?sort=id,desc")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(item.getId().intValue()))).andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
        .andExpect(jsonPath("$.[*].pictureUrl").value(hasItem(DEFAULT_PICTURE_URL.toString())))
        .andExpect(jsonPath("$.[*].pricePerUnit").value(hasItem(DEFAULT_PRICE_PER_UNIT.doubleValue()))).andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT.toString())))
        .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString()))).andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
        .andExpect(jsonPath("$.[*].showInShop").value(hasItem(DEFAULT_SHOW_IN_SHOP.booleanValue())));
  }

  @Test
  @Transactional
  public void getItem() throws Exception {
    // Initialize the database
    itemRepository.saveAndFlush(item);

    // Get the item
    restItemMockMvc.perform(get("/api/items/{id}", item.getId())).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(item.getId().intValue())).andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
        .andExpect(jsonPath("$.pictureUrl").value(DEFAULT_PICTURE_URL.toString())).andExpect(jsonPath("$.pricePerUnit").value(DEFAULT_PRICE_PER_UNIT.doubleValue()))
        .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT.toString())).andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
        .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString())).andExpect(jsonPath("$.showInShop").value(DEFAULT_SHOW_IN_SHOP.booleanValue()));
  }

  @Test
  @Transactional
  public void getNonExistingItem() throws Exception {
    // Get the item
    restItemMockMvc.perform(get("/api/items/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateItem() throws Exception {
    // Initialize the database
    itemRepository.saveAndFlush(item);
    itemSearchRepository.save(item);
    int databaseSizeBeforeUpdate = itemRepository.findAll().size();

    // Update the item
    Item updatedItem = itemRepository.findOne(item.getId());
    updatedItem.name(UPDATED_NAME).pictureUrl(UPDATED_PICTURE_URL).pricePerUnit(UPDATED_PRICE_PER_UNIT).unit(UPDATED_UNIT).code(UPDATED_CODE).description(UPDATED_DESCRIPTION)
        .showInShop(UPDATED_SHOW_IN_SHOP);
    ItemDTO itemDTO = itemMapper.toDto(updatedItem);

    restItemMockMvc.perform(put("/api/items").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(itemDTO))).andExpect(status().isOk());

    // Validate the Item in the database
    List<Item> itemList = itemRepository.findAll();
    assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    Item testItem = itemList.get(itemList.size() - 1);
    assertThat(testItem.getName()).isEqualTo(UPDATED_NAME);
    assertThat(testItem.getPictureUrl()).isEqualTo(UPDATED_PICTURE_URL);
    assertThat(testItem.getPricePerUnit()).isEqualTo(UPDATED_PRICE_PER_UNIT);
    assertThat(testItem.getUnit()).isEqualTo(UPDATED_UNIT);
    assertThat(testItem.getCode()).isEqualTo(UPDATED_CODE);
    assertThat(testItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    assertThat(testItem.isShowInShop()).isEqualTo(UPDATED_SHOW_IN_SHOP);

    // Validate the Item in Elasticsearch
    Item itemEs = itemSearchRepository.findOne(testItem.getId());
    assertThat(itemEs).isEqualToComparingFieldByField(testItem);
  }

  @Test
  @Transactional
  public void updateNonExistingItem() throws Exception {
    int databaseSizeBeforeUpdate = itemRepository.findAll().size();

    // Create the Item
    ItemDTO itemDTO = itemMapper.toDto(item);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restItemMockMvc.perform(put("/api/items").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(itemDTO))).andExpect(status().isCreated());

    // Validate the Item in the database
    List<Item> itemList = itemRepository.findAll();
    assertThat(itemList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteItem() throws Exception {
    // Initialize the database
    itemRepository.saveAndFlush(item);
    itemSearchRepository.save(item);
    int databaseSizeBeforeDelete = itemRepository.findAll().size();

    // Get the item
    restItemMockMvc.perform(delete("/api/items/{id}", item.getId()).accept(TestUtil.APPLICATION_JSON_UTF8)).andExpect(status().isOk());

    // Validate Elasticsearch is empty
    boolean itemExistsInEs = itemSearchRepository.exists(item.getId());
    assertThat(itemExistsInEs).isFalse();

    // Validate the database is empty
    List<Item> itemList = itemRepository.findAll();
    assertThat(itemList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void searchItem() throws Exception {
    // Initialize the database
    itemRepository.saveAndFlush(item);
    itemSearchRepository.save(item);

    // Search the item
    restItemMockMvc.perform(get("/api/_search/items?query=id:" + item.getId())).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(item.getId().intValue()))).andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
        .andExpect(jsonPath("$.[*].pictureUrl").value(hasItem(DEFAULT_PICTURE_URL.toString())))
        .andExpect(jsonPath("$.[*].pricePerUnit").value(hasItem(DEFAULT_PRICE_PER_UNIT.doubleValue()))).andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT.toString())))
        .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString()))).andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
        .andExpect(jsonPath("$.[*].showInShop").value(hasItem(DEFAULT_SHOW_IN_SHOP.booleanValue())));
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Item.class);
    Item item1 = new Item();
    item1.setId(1L);
    Item item2 = new Item();
    item2.setId(item1.getId());
    assertThat(item1).isEqualTo(item2);
    item2.setId(2L);
    assertThat(item1).isNotEqualTo(item2);
    item1.setId(null);
    assertThat(item1).isNotEqualTo(item2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(ItemDTO.class);
    ItemDTO itemDTO1 = new ItemDTO();
    itemDTO1.setId(1L);
    ItemDTO itemDTO2 = new ItemDTO();
    assertThat(itemDTO1).isNotEqualTo(itemDTO2);
    itemDTO2.setId(itemDTO1.getId());
    assertThat(itemDTO1).isEqualTo(itemDTO2);
    itemDTO2.setId(2L);
    assertThat(itemDTO1).isNotEqualTo(itemDTO2);
    itemDTO1.setId(null);
    assertThat(itemDTO1).isNotEqualTo(itemDTO2);
  }

  @Test
  @Transactional
  public void testEntityFromId() {
    assertThat(itemMapper.fromId(42L).getId()).isEqualTo(42);
    assertThat(itemMapper.fromId(null)).isNull();
  }

}
