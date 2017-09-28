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
import org.springframework.util.Base64Utils;

import com.mycompany.mystaff.MystaffApp;
import com.mycompany.mystaff.domain.Company;
import com.mycompany.mystaff.domain.File;
import com.mycompany.mystaff.domain.enumeration.FileType;
import com.mycompany.mystaff.repository.FileRepository;
import com.mycompany.mystaff.repository.search.FileSearchRepository;
import com.mycompany.mystaff.security.jwt.TokenProvider;
import com.mycompany.mystaff.service.FileService;
import com.mycompany.mystaff.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the FileResource REST controller.
 *
 * @see FileResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MystaffApp.class)
public class FileResourceIntTest {

  private static final String DEFAULT_NAME = "AAAAAAAAAA";
  private static final String UPDATED_NAME = "BBBBBBBBBB";

  private static final String DEFAULT_URL = "AAAAAAAAAA";
  private static final String UPDATED_URL = "BBBBBBBBBB";

  private static final FileType DEFAULT_FILE_TYPE = FileType.AVATAR;
  private static final FileType UPDATED_FILE_TYPE = FileType.PRODUCT_PICTURE;

  private static final byte[] DEFAULT_FILE = TestUtil.createByteArray(1, "0");
  private static final byte[] UPDATED_FILE = TestUtil.createByteArray(2, "1");
  private static final String DEFAULT_FILE_CONTENT_TYPE = "image/jpg";
  private static final String UPDATED_FILE_CONTENT_TYPE = "image/png";

  @Autowired
  private FileRepository fileRepository;

  @Autowired
  private FileService fileService;

  @Autowired
  private FileSearchRepository fileSearchRepository;

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

  private MockMvc restFileMockMvc;

  private File file;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final FileResource fileResource = new FileResource(fileService, request, tokenProvider);
    this.restFileMockMvc = MockMvcBuilders.standaloneSetup(fileResource).setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  /**
   * Create an entity for this test.
   *
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static File createEntity(EntityManager em) {
    File file = new File().name(DEFAULT_NAME).url(DEFAULT_URL).fileType(DEFAULT_FILE_TYPE).file(DEFAULT_FILE).fileContentType(DEFAULT_FILE_CONTENT_TYPE);
    // Add required entity
    Company company = CompanyResourceIntTest.createEntity(em);
    em.persist(company);
    em.flush();
    file.setCompany(company);
    return file;
  }

  @Before
  public void initTest() {
    fileSearchRepository.deleteAll();
    file = createEntity(em);
  }

  @Test
  @Transactional
  public void createFile() throws Exception {
    int databaseSizeBeforeCreate = fileRepository.findAll().size();

    // Create the File
    restFileMockMvc.perform(post("/api/files").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(file))).andExpect(status().isCreated());

    // Validate the File in the database
    List<File> fileList = fileRepository.findAll();
    assertThat(fileList).hasSize(databaseSizeBeforeCreate + 1);
    File testFile = fileList.get(fileList.size() - 1);
    assertThat(testFile.getName()).isEqualTo(DEFAULT_NAME);
    assertThat(testFile.getUrl()).isEqualTo(DEFAULT_URL);
    assertThat(testFile.getFileType()).isEqualTo(DEFAULT_FILE_TYPE);
    assertThat(testFile.getFile()).isEqualTo(DEFAULT_FILE);
    assertThat(testFile.getFileContentType()).isEqualTo(DEFAULT_FILE_CONTENT_TYPE);

    // Validate the File in Elasticsearch
    File fileEs = fileSearchRepository.findOne(testFile.getId());
    assertThat(fileEs).isEqualToComparingFieldByField(testFile);
  }

  @Test
  @Transactional
  public void createFileWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = fileRepository.findAll().size();

    // Create the File with an existing ID
    file.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restFileMockMvc.perform(post("/api/files").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(file))).andExpect(status().isBadRequest());

    // Validate the File in the database
    List<File> fileList = fileRepository.findAll();
    assertThat(fileList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllFiles() throws Exception {
    // Initialize the database
    fileRepository.saveAndFlush(file);

    // Get all the fileList
    restFileMockMvc.perform(get("/api/files?sort=id,desc")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(file.getId().intValue()))).andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
        .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString()))).andExpect(jsonPath("$.[*].fileType").value(hasItem(DEFAULT_FILE_TYPE.toString())))
        .andExpect(jsonPath("$.[*].fileContentType").value(hasItem(DEFAULT_FILE_CONTENT_TYPE)))
        .andExpect(jsonPath("$.[*].file").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE))));
  }

  @Test
  @Transactional
  public void getFile() throws Exception {
    // Initialize the database
    fileRepository.saveAndFlush(file);

    // Get the file
    restFileMockMvc.perform(get("/api/files/{id}", file.getId())).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(file.getId().intValue())).andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
        .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString())).andExpect(jsonPath("$.fileType").value(DEFAULT_FILE_TYPE.toString()))
        .andExpect(jsonPath("$.fileContentType").value(DEFAULT_FILE_CONTENT_TYPE)).andExpect(jsonPath("$.file").value(Base64Utils.encodeToString(DEFAULT_FILE)));
  }

  @Test
  @Transactional
  public void getNonExistingFile() throws Exception {
    // Get the file
    restFileMockMvc.perform(get("/api/files/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateFile() throws Exception {
    // Initialize the database
    fileService.save(file);

    int databaseSizeBeforeUpdate = fileRepository.findAll().size();

    // Update the file
    File updatedFile = fileRepository.findOne(file.getId());
    updatedFile.name(UPDATED_NAME).url(UPDATED_URL).fileType(UPDATED_FILE_TYPE).file(UPDATED_FILE).fileContentType(UPDATED_FILE_CONTENT_TYPE);

    restFileMockMvc.perform(put("/api/files").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(updatedFile))).andExpect(status().isOk());

    // Validate the File in the database
    List<File> fileList = fileRepository.findAll();
    assertThat(fileList).hasSize(databaseSizeBeforeUpdate);
    File testFile = fileList.get(fileList.size() - 1);
    assertThat(testFile.getName()).isEqualTo(UPDATED_NAME);
    assertThat(testFile.getUrl()).isEqualTo(UPDATED_URL);
    assertThat(testFile.getFileType()).isEqualTo(UPDATED_FILE_TYPE);
    assertThat(testFile.getFile()).isEqualTo(UPDATED_FILE);
    assertThat(testFile.getFileContentType()).isEqualTo(UPDATED_FILE_CONTENT_TYPE);

    // Validate the File in Elasticsearch
    File fileEs = fileSearchRepository.findOne(testFile.getId());
    assertThat(fileEs).isEqualToComparingFieldByField(testFile);
  }

  @Test
  @Transactional
  public void updateNonExistingFile() throws Exception {
    int databaseSizeBeforeUpdate = fileRepository.findAll().size();

    // Create the File

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restFileMockMvc.perform(put("/api/files").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(file))).andExpect(status().isCreated());

    // Validate the File in the database
    List<File> fileList = fileRepository.findAll();
    assertThat(fileList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteFile() throws Exception {
    // Initialize the database
    fileService.save(file);

    int databaseSizeBeforeDelete = fileRepository.findAll().size();

    // Get the file
    restFileMockMvc.perform(delete("/api/files/{id}", file.getId()).accept(TestUtil.APPLICATION_JSON_UTF8)).andExpect(status().isOk());

    // Validate Elasticsearch is empty
    boolean fileExistsInEs = fileSearchRepository.exists(file.getId());
    assertThat(fileExistsInEs).isFalse();

    // Validate the database is empty
    List<File> fileList = fileRepository.findAll();
    assertThat(fileList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void searchFile() throws Exception {
    // Initialize the database
    fileService.save(file);

    // Search the file
    restFileMockMvc.perform(get("/api/_search/files?query=id:" + file.getId())).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(file.getId().intValue()))).andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
        .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString()))).andExpect(jsonPath("$.[*].fileType").value(hasItem(DEFAULT_FILE_TYPE.toString())))
        .andExpect(jsonPath("$.[*].fileContentType").value(hasItem(DEFAULT_FILE_CONTENT_TYPE)))
        .andExpect(jsonPath("$.[*].file").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE))));
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(File.class);
    File file1 = new File();
    file1.setId(1L);
    File file2 = new File();
    file2.setId(file1.getId());
    assertThat(file1).isEqualTo(file2);
    file2.setId(2L);
    assertThat(file1).isNotEqualTo(file2);
    file1.setId(null);
    assertThat(file1).isNotEqualTo(file2);
  }

}
