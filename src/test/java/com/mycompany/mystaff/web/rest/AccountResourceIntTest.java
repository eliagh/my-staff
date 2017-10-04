package com.mycompany.mystaff.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.mystaff.MystaffApp;
import com.mycompany.mystaff.config.Constants;
import com.mycompany.mystaff.domain.Authority;
import com.mycompany.mystaff.domain.Company;
import com.mycompany.mystaff.domain.User;
import com.mycompany.mystaff.repository.AuthorityRepository;
import com.mycompany.mystaff.repository.CompanyRepository;
import com.mycompany.mystaff.repository.UserRepository;
import com.mycompany.mystaff.security.AuthoritiesConstants;
import com.mycompany.mystaff.security.jwt.TokenProvider;
import com.mycompany.mystaff.service.CompanyService;
import com.mycompany.mystaff.service.MailService;
import com.mycompany.mystaff.service.UserService;
import com.mycompany.mystaff.service.dto.UserDTO;
import com.mycompany.mystaff.web.rest.vm.KeyAndPasswordVM;
import com.mycompany.mystaff.web.rest.vm.ManagedUserVM;

/**
 * Test class for the AccountResource REST controller.
 *
 * @see AccountResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MystaffApp.class)
public class AccountResourceIntTest {

  private String JWT = "";

  private static final Long DEFAULT_COMPANY_ID = 1L;
  private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
  private static final String DEFAULT_COMPANY_THEMA = "BBBBBBBBBB";

  @Autowired
  private TokenProvider tokenProvider;

  @Autowired
  private CompanyRepository companyRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AuthorityRepository authorityRepository;

  @Autowired
  private UserService userService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @SuppressWarnings("rawtypes")
  @Autowired
  private HttpMessageConverter[] httpMessageConverters;

  @Mock
  private UserService mockUserService;

  @Mock
  private MailService mockMailService;

  @Mock
  private CompanyService mockCompanyService;

  private MockMvc restUserMockMvc;

  private MockMvc restMvc;

  @Before
  public void setup() {
    Authentication authentication = TestUtil.createAuthentication();
    this.JWT = tokenProvider.createToken(authentication, false, DEFAULT_COMPANY_ID);
	    
    MockitoAnnotations.initMocks(this);
    doNothing().when(mockMailService).sendActivationEmail(anyObject());

    AccountResource accountResource = new AccountResource(userRepository, userService, mockMailService, mockCompanyService);

    AccountResource accountUserMockResource = new AccountResource(userRepository, mockUserService, mockMailService, mockCompanyService);

    this.restMvc = MockMvcBuilders.standaloneSetup(accountResource).setMessageConverters(httpMessageConverters).build();
    this.restUserMockMvc = MockMvcBuilders.standaloneSetup(accountUserMockResource).build();
  }

  @Test
  public void testNonAuthenticatedUser() throws Exception {
    restUserMockMvc.perform(get("/api/authenticate").header("Authorization", "Bearer " + JWT).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().string(""));
  }

  @Test
  public void testAuthenticatedUser() throws Exception {
    restUserMockMvc.perform(get("/api/authenticate").with(request -> {
      request.setRemoteUser("test");
      return request;
    }).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().string("test"));
  }

  @Test
  public void testGetExistingAccount() throws Exception {
    Set<Authority> authorities = new HashSet<>();
    Authority authority = new Authority();
    authority.setName(AuthoritiesConstants.ADMIN);
    authorities.add(authority);

    User user = new User();
    user.setLogin("test");
    user.setFirstName("john");
    user.setLastName("doe");
    user.setEmail("john.doe@jhipster.com");
    user.setImageUrl("http://placehold.it/50x50");
    user.setLangKey("en");
    user.setAuthorities(authorities);
    user.setCompanyId(DEFAULT_COMPANY_ID);
    when(mockUserService.getUserWithAuthorities()).thenReturn(user);

    restUserMockMvc.perform(get("/api/account").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andExpect(jsonPath("$.login").value("test")).andExpect(jsonPath("$.firstName").value("john"))
        .andExpect(jsonPath("$.lastName").value("doe")).andExpect(jsonPath("$.email").value("john.doe@jhipster.com"))
        .andExpect(jsonPath("$.imageUrl").value("http://placehold.it/50x50")).andExpect(jsonPath("$.langKey").value("en"))
        .andExpect(jsonPath("$.authorities").value(AuthoritiesConstants.ADMIN));
  }

  @Test
  public void testGetUnknownAccount() throws Exception {
    when(mockUserService.getUserWithAuthorities()).thenReturn(null);

    restUserMockMvc.perform(get("/api/account").header("Authorization", "Bearer " + JWT).accept(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError());
  }

  @Test
  @Transactional
  public void testRegisterValid() throws Exception {
    Company company = new Company();
    company.setName(DEFAULT_COMPANY_NAME);
    company.setThema(DEFAULT_COMPANY_THEMA);
    company = companyRepository.save(company);
    when(mockCompanyService.create(Constants.DEFAULT_LANGUAGE)).thenReturn(company);

    ManagedUserVM validUser = new ManagedUserVM( //
        null,                   // id
        "joe",                  // login
        "password",             // password
        "Joe",                  // firstName
        "Shmoe",                // lastName
        "joe@example.com",      // email
        true,                   // activated
        "http://placehold.it/50x50", // imageUrl
        Constants.DEFAULT_LANGUAGE,// langKey
        null,                   // createdBy
        null,                   // createdDate
        null,                   // lastModifiedBy
        null,                   // lastModifiedDate
        new HashSet<>(Collections.singletonList(AuthoritiesConstants.USER)) //
    );

    restMvc.perform(post("/api/register").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(validUser))).andExpect(status().isCreated());

    Optional<User> user = userRepository.findOneByLogin("joe");
    assertThat(user.isPresent()).isTrue();
  }

  @Test
  @Transactional
  public void testRegisterInvalidLogin() throws Exception {
    ManagedUserVM invalidUser = new ManagedUserVM( //
        null,                   // id
        "funky-log!n",          // login <-- invalid
        "password",             // password
        "Funky",                // firstName
        "One",                  // lastName
        "funky@example.com",    // email
        true,                   // activated
        "http://placehold.it/50x50", // imageUrl
        Constants.DEFAULT_LANGUAGE,// langKey
        null,                   // createdBy
        null,                   // createdDate
        null,                   // lastModifiedBy
        null,                   // lastModifiedDate
        new HashSet<>(Collections.singletonList(AuthoritiesConstants.USER)) //
    );

    restUserMockMvc.perform(post("/api/register").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(invalidUser)))
        .andExpect(status().isBadRequest());

    Optional<User> user = userRepository.findOneByEmailIgnoreCase("funky@example.com");
    assertThat(user.isPresent()).isFalse();
  }

  @Test
  @Transactional
  public void testRegisterInvalidEmail() throws Exception {
    ManagedUserVM invalidUser = new ManagedUserVM( //
        null,               // id
        "bob",              // login
        "password",         // password
        "Bob",              // firstName
        "Green",            // lastName
        "invalid",          // email <-- invalid
        true,               // activated
        "http://placehold.it/50x50", // imageUrl
        Constants.DEFAULT_LANGUAGE,// langKey
        null,                   // createdBy
        null,                   // createdDate
        null,                   // lastModifiedBy
        null,                   // lastModifiedDate
        new HashSet<>(Collections.singletonList(AuthoritiesConstants.USER)) //
    );

    restUserMockMvc.perform(post("/api/register").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(invalidUser)))
        .andExpect(status().isBadRequest());

    Optional<User> user = userRepository.findOneByLogin("bob");
    assertThat(user.isPresent()).isFalse();
  }

  @Test
  @Transactional
  public void testRegisterInvalidPassword() throws Exception {
    ManagedUserVM invalidUser = new ManagedUserVM( //
        null,               // id
        "bob",              // login
        "123",              // password with only 3 digits
        "Bob",              // firstName
        "Green",            // lastName
        "bob@example.com",  // email
        true,               // activated
        "http://placehold.it/50x50", // imageUrl
        Constants.DEFAULT_LANGUAGE,// langKey
        null,                   // createdBy
        null,                   // createdDate
        null,                   // lastModifiedBy
        null,                   // lastModifiedDate
        new HashSet<>(Collections.singletonList(AuthoritiesConstants.USER)) //
    );

    restUserMockMvc.perform(post("/api/register").header("Authorization", "Bearer " + JWT).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(invalidUser)))
        .andExpect(status().isBadRequest());

    Optional<User> user = userRepository.findOneByLogin("bob");
    assertThat(user.isPresent()).isFalse();
  }

  @Test
  @Transactional
  public void testRegisterNullPassword() throws Exception {
    ManagedUserVM invalidUser = new ManagedUserVM( //
        null,               // id
        "bob",              // login
        null,               // invalid null password
        "Bob",              // firstName
        "Green",            // lastName
        "bob@example.com",  // email
        true,               // activated
        "http://placehold.it/50x50", // imageUrl
        Constants.DEFAULT_LANGUAGE,// langKey
        null,                   // createdBy
        null,                   // createdDate
        null,                   // lastModifiedBy
        null,                   // lastModifiedDate
        new HashSet<>(Collections.singletonList(AuthoritiesConstants.USER)) //
    );

    restUserMockMvc.perform(post("/api/register").header("Authorization", "Bearer " + JWT).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(invalidUser)))
        .andExpect(status().isBadRequest());

    Optional<User> user = userRepository.findOneByLogin("bob");
    assertThat(user.isPresent()).isFalse();
  }

  @Test
  @Transactional
  public void testRegisterDuplicateLogin() throws Exception {
    Company company = new Company();
    company.setName(DEFAULT_COMPANY_NAME);
    company.setThema(DEFAULT_COMPANY_THEMA);
    company = companyRepository.save(company);
    when(mockCompanyService.create(Constants.DEFAULT_LANGUAGE)).thenReturn(company);

    // Good
    ManagedUserVM validUser = new ManagedUserVM( //
        null,                   // id
        "alice",                // login
        "password",             // password
        "Alice",                // firstName
        "Something",            // lastName
        "alice@example.com",    // email
        true,                   // activated
        "http://placehold.it/50x50", // imageUrl
        Constants.DEFAULT_LANGUAGE,// langKey
        null,                   // createdBy
        null,                   // createdDate
        null,                   // lastModifiedBy
        null,                   // lastModifiedDate
        new HashSet<>(Collections.singletonList(AuthoritiesConstants.USER)) //
    );

    // Duplicate login, different email
    ManagedUserVM duplicatedUser = new ManagedUserVM(validUser.getId(), validUser.getLogin(), validUser.getPassword(), validUser.getFirstName(), validUser.getLastName(),
        "alicejr@example.com", true, validUser.getImageUrl(), validUser.getLangKey(), validUser.getCreatedBy(), validUser.getCreatedDate(), validUser.getLastModifiedBy(),
        validUser.getLastModifiedDate(), validUser.getAuthorities());

    // Good user
    restMvc.perform(post("/api/register").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(validUser))).andExpect(status().isCreated());

    // Duplicate login
    restMvc.perform(post("/api/register").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(duplicatedUser)))
        .andExpect(status().is4xxClientError());

    Optional<User> userDup = userRepository.findOneByEmailIgnoreCase("alicejr@example.com");
    assertThat(userDup.isPresent()).isFalse();
  }

  @Test
  @Transactional
  public void testRegisterDuplicateEmail() throws Exception {
    Company company = new Company();
    company.setName(DEFAULT_COMPANY_NAME);
    company.setThema(DEFAULT_COMPANY_THEMA);
    company = companyRepository.save(company);
    when(mockCompanyService.create(Constants.DEFAULT_LANGUAGE)).thenReturn(company);

    // Good
    ManagedUserVM validUser = new ManagedUserVM( //
        null,                   // id
        "john",                 // login
        "password",             // password
        "John",                 // firstName
        "Doe",                  // lastName
        "john@example.com",     // email
        true,                   // activated
        "http://placehold.it/50x50", // imageUrl
        Constants.DEFAULT_LANGUAGE,// langKey
        null,                   // createdBy
        null,                   // createdDate
        null,                   // lastModifiedBy
        null,                   // lastModifiedDate
        new HashSet<>(Collections.singletonList(AuthoritiesConstants.USER)) //
    );

    // Duplicate email, different login
    ManagedUserVM duplicatedUser = new ManagedUserVM(validUser.getId(), "johnjr", validUser.getPassword(), validUser.getLogin(), validUser.getLastName(), validUser.getEmail(),
        true, validUser.getImageUrl(), validUser.getLangKey(), validUser.getCreatedBy(), validUser.getCreatedDate(), validUser.getLastModifiedBy(), validUser.getLastModifiedDate(),
        validUser.getAuthorities());

    // Good user
    restMvc.perform(post("/api/register").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(validUser))).andExpect(status().isCreated());

    // Duplicate email
    restMvc.perform(post("/api/register").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(duplicatedUser)))
        .andExpect(status().is4xxClientError());
    // Duplicate email - with uppercase email address

    final ManagedUserVM userWithUpperCaseEmail = new ManagedUserVM(validUser.getId(), "johnjr", validUser.getPassword(), validUser.getLogin(), validUser.getLastName(),
        validUser.getEmail().toUpperCase(), true, validUser.getImageUrl(), validUser.getLangKey(), validUser.getCreatedBy(), validUser.getCreatedDate(),
        validUser.getLastModifiedBy(), validUser.getLastModifiedDate(), validUser.getAuthorities());

    restMvc.perform(post("/api/register").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(userWithUpperCaseEmail)))
        .andExpect(status().is4xxClientError());

    Optional<User> userDup = userRepository.findOneByLogin("johnjr");
    assertThat(userDup.isPresent()).isFalse();
  }

  @Test
  @Transactional
  public void testRegisterAdminIsIgnored() throws Exception {
    Company company = new Company();
    company.setName(DEFAULT_COMPANY_NAME);
    company.setThema(DEFAULT_COMPANY_THEMA);
    company = companyRepository.save(company);
    when(mockCompanyService.create(Constants.DEFAULT_LANGUAGE)).thenReturn(company);

    ManagedUserVM validUser = new ManagedUserVM( //
        null,                   // id
        "badguy",               // login
        "password",             // password
        "Bad",                  // firstName
        "Guy",                  // lastName
        "badguy@example.com",   // email
        true,                   // activated
        "http://placehold.it/50x50", // imageUrl
        Constants.DEFAULT_LANGUAGE,// langKey
        null,                   // createdBy
        null,                   // createdDate
        null,                   // lastModifiedBy
        null,                   // lastModifiedDate
        new HashSet<>(Collections.singletonList(AuthoritiesConstants.ADMIN)) //
    );

    restMvc.perform(post("/api/register").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(validUser))).andExpect(status().isCreated());

    Optional<User> userDup = userRepository.findOneByLogin("badguy");
    assertThat(userDup.isPresent()).isTrue();
    assertThat(userDup.get().getAuthorities()).hasSize(4).containsAll(authorityRepository.retrieveUserRegistrationRoles(AuthoritiesConstants.USER, AuthoritiesConstants.COMPANY_ADMIN, AuthoritiesConstants.LOCATION_ADMIN, AuthoritiesConstants.STAFF));
  }

  @Test
  @Transactional
  public void testActivateAccount() throws Exception {
    Company company = new Company();
    company.setName(DEFAULT_COMPANY_NAME);
    company.setThema(DEFAULT_COMPANY_THEMA);
    company = companyRepository.save(company);

    final String activationKey = "some activation key";
    User user = new User();
    user.setLogin("activate-account");
    user.setEmail("activate-account@example.com");
    user.setCompanyId(company.getId());
    user.setPassword(RandomStringUtils.random(60));
    user.setActivated(false);
    user.setActivationKey(activationKey);

    userRepository.saveAndFlush(user);

    restMvc.perform(get("/api/activate?key={activationKey}", activationKey)).andExpect(status().isOk());

    user = userRepository.findOneByLogin(user.getLogin()).orElse(null);
    assertThat(user.getActivated()).isTrue();
  }

  @Test
  @Transactional
  public void testActivateAccountWithWrongKey() throws Exception {
    restMvc.perform(get("/api/activate?key=wrongActivationKey")).andExpect(status().isInternalServerError());
  }

  @Test
  @Transactional
  @WithMockUser("save-account")
  public void testSaveAccount() throws Exception {
    Company company = new Company();
    company.setName(DEFAULT_COMPANY_NAME);
    company.setThema(DEFAULT_COMPANY_THEMA);
    company = companyRepository.save(company);

    User user = new User();
    user.setLogin("save-account");
    user.setEmail("save-account@example.com");
    user.setCompanyId(company.getId());
    user.setPassword(RandomStringUtils.random(60));
    user.setActivated(true);

    userRepository.saveAndFlush(user);

    UserDTO userDTO = new UserDTO( //
        null,                   // id
        "not-used",          // login
        "firstname",                // firstName
        "lastname",                  // lastName
        "save-account@example.com",    // email
        false,                   // activated
        "http://placehold.it/50x50", // imageUrl
        Constants.DEFAULT_LANGUAGE,// langKey
        null,                   // createdBy
        null,                   // createdDate
        null,                   // lastModifiedBy
        null,                   // lastModifiedDate
        new HashSet<>(Collections.singletonList(AuthoritiesConstants.ADMIN)) //
    );

    restMvc.perform(post("/api/account").header("Authorization", "Bearer " + JWT).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(userDTO))).andExpect(status().isOk());

    User updatedUser = userRepository.findOneByLogin(user.getLogin()).orElse(null);
    assertThat(updatedUser.getFirstName()).isEqualTo(userDTO.getFirstName());
    assertThat(updatedUser.getLastName()).isEqualTo(userDTO.getLastName());
    assertThat(updatedUser.getEmail()).isEqualTo(userDTO.getEmail());
    assertThat(updatedUser.getLangKey()).isEqualTo(userDTO.getLangKey());
    assertThat(updatedUser.getPassword()).isEqualTo(user.getPassword());
    assertThat(updatedUser.getImageUrl()).isEqualTo(userDTO.getImageUrl());
    assertThat(updatedUser.getActivated()).isEqualTo(true);
    assertThat(updatedUser.getAuthorities()).isEmpty();
  }

  @Test
  @Transactional
  @WithMockUser("save-invalid-email")
  public void testSaveInvalidEmail() throws Exception {
    Company company = new Company(DEFAULT_COMPANY_ID);
    company.setName(DEFAULT_COMPANY_NAME);
    company.setThema(DEFAULT_COMPANY_THEMA);
    companyRepository.save(company);

    User user = new User();
    user.setLogin("save-invalid-email");
    user.setEmail("save-invalid-email@example.com");
    user.setPassword(RandomStringUtils.random(60));
    user.setActivated(true);
    user.setCompanyId(DEFAULT_COMPANY_ID);

    userRepository.saveAndFlush(user);

    UserDTO userDTO = new UserDTO( //
        null,                   // id
        "not-used",          // login
        "firstname",                // firstName
        "lastname",                  // lastName
        "invalid email",    // email
        false,                   // activated
        "http://placehold.it/50x50", // imageUrl
        Constants.DEFAULT_LANGUAGE,// langKey
        null,                   // createdBy
        null,                   // createdDate
        null,                   // lastModifiedBy
        null,                   // lastModifiedDate
        new HashSet<>(Collections.singletonList(AuthoritiesConstants.ADMIN)) //
    );

    restMvc.perform(post("/api/account").header("Authorization", "Bearer " + JWT).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(userDTO))).andExpect(status().isBadRequest());

    assertThat(userRepository.findOneByEmailIgnoreCase("invalid email")).isNotPresent();
  }

  @Test
  @Transactional
  @WithMockUser("save-existing-email")
  public void testSaveExistingEmail() throws Exception {
    Company company = new Company();
    company.setName(DEFAULT_COMPANY_NAME);
    company.setThema(DEFAULT_COMPANY_THEMA);
    company = companyRepository.save(company);

    User user = new User();
    user.setLogin("save-existing-email");
    user.setEmail("save-existing-email@example.com");
    user.setCompanyId(company.getId());
    user.setPassword(RandomStringUtils.random(60));
    user.setActivated(true);

    userRepository.saveAndFlush(user);

    Company anotherCompany = new Company();
    anotherCompany.setName(DEFAULT_COMPANY_NAME);
    anotherCompany.setThema(DEFAULT_COMPANY_THEMA);
    anotherCompany = companyRepository.save(anotherCompany);

    User anotherUser = new User();
    anotherUser.setLogin("save-existing-email2");
    anotherUser.setEmail("save-existing-email2@example.com");
    anotherUser.setCompanyId(anotherCompany.getId());
    anotherUser.setPassword(RandomStringUtils.random(60));
    anotherUser.setActivated(true);

    userRepository.saveAndFlush(anotherUser);

    UserDTO userDTO = new UserDTO( //
        null,                   // id
        "not-used",          // login
        "firstname",                // firstName
        "lastname",                  // lastName
        "save-existing-email2@example.com",    // email
        false,                   // activated
        "http://placehold.it/50x50", // imageUrl
        Constants.DEFAULT_LANGUAGE,// langKey
        null,                   // createdBy
        null,                   // createdDate
        null,                   // lastModifiedBy
        null,                   // lastModifiedDate
        new HashSet<>(Collections.singletonList(AuthoritiesConstants.ADMIN)) //
    );

    restMvc.perform(post("/api/account").header("Authorization", "Bearer " + JWT).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(userDTO))).andExpect(status().isBadRequest());

    User updatedUser = userRepository.findOneByLogin("save-existing-email").orElse(null);
    assertThat(updatedUser.getEmail()).isEqualTo("save-existing-email@example.com");
  }

  @Test
  @Transactional
  @WithMockUser("save-existing-email-and-login")
  public void testSaveExistingEmailAndLogin() throws Exception {
    Company company = new Company();
    company.setName(DEFAULT_COMPANY_NAME);
    company.setThema(DEFAULT_COMPANY_THEMA);
    company = companyRepository.save(company);

    User user = new User();
    user.setLogin("save-existing-email-and-login");
    user.setEmail("save-existing-email-and-login@example.com");
    user.setCompanyId(company.getId());
    user.setPassword(RandomStringUtils.random(60));
    user.setActivated(true);

    userRepository.saveAndFlush(user);

    UserDTO userDTO = new UserDTO( //
        null,                   // id
        "not-used",          // login
        "firstname",                // firstName
        "lastname",                  // lastName
        "save-existing-email-and-login@example.com",    // email
        false,                   // activated
        "http://placehold.it/50x50", // imageUrl
        Constants.DEFAULT_LANGUAGE,// langKey
        null,                   // createdBy
        null,                   // createdDate
        null,                   // lastModifiedBy
        null,                   // lastModifiedDate
        new HashSet<>(Collections.singletonList(AuthoritiesConstants.ADMIN)) //
    );

    restMvc.perform(post("/api/account").header("Authorization", "Bearer " + JWT).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(userDTO))).andExpect(status().isOk());

    User updatedUser = userRepository.findOneByLogin("save-existing-email-and-login").orElse(null);
    assertThat(updatedUser.getEmail()).isEqualTo("save-existing-email-and-login@example.com");
  }

  @Test
  @Transactional
  @WithMockUser("change-password")
  public void testChangePassword() throws Exception {
    Company company = new Company();
    company.setName(DEFAULT_COMPANY_NAME);
    company.setThema(DEFAULT_COMPANY_THEMA);
    company = companyRepository.save(company);

    User user = new User();
    user.setCompanyId(company.getId());
    user.setPassword(RandomStringUtils.random(60));
    user.setLogin("change-password");
    user.setEmail("change-password@example.com");
    userRepository.saveAndFlush(user);

    restMvc.perform(post("/api/account/change-password").content("new password")).andExpect(status().isOk());

    User updatedUser = userRepository.findOneByLogin("change-password").orElse(null);
    assertThat(passwordEncoder.matches("new password", updatedUser.getPassword())).isTrue();
  }

  @Test
  @Transactional
  @WithMockUser("change-password-too-small")
  public void testChangePasswordTooSmall() throws Exception {
    Company company = new Company();
    company.setName(DEFAULT_COMPANY_NAME);
    company.setThema(DEFAULT_COMPANY_THEMA);
    company = companyRepository.save(company);

    User user = new User();
    user.setCompanyId(company.getId());
    user.setPassword(RandomStringUtils.random(60));
    user.setLogin("change-password-too-small");
    user.setEmail("change-password-too-small@example.com");
    userRepository.saveAndFlush(user);

    restMvc.perform(post("/api/account/change-password").content("new")).andExpect(status().isBadRequest());

    User updatedUser = userRepository.findOneByLogin("change-password-too-small").orElse(null);
    assertThat(updatedUser.getPassword()).isEqualTo(user.getPassword());
  }

  @Test
  @Transactional
  @WithMockUser("change-password-too-long")
  public void testChangePasswordTooLong() throws Exception {
    Company company = new Company();
    company.setName(DEFAULT_COMPANY_NAME);
    company.setThema(DEFAULT_COMPANY_THEMA);
    company = companyRepository.save(company);

    User user = new User();
    user.setPassword(RandomStringUtils.random(60));
    user.setLogin("change-password-too-long");
    user.setEmail("change-password-too-long@example.com");
    user.setCompanyId(company.getId());
    userRepository.saveAndFlush(user);

    restMvc.perform(post("/api/account/change-password").content(RandomStringUtils.random(101))).andExpect(status().isBadRequest());

    User updatedUser = userRepository.findOneByLogin("change-password-too-long").orElse(null);
    assertThat(updatedUser.getPassword()).isEqualTo(user.getPassword());
  }

  @Test
  @Transactional
  @WithMockUser("change-password-empty")
  public void testChangePasswordEmpty() throws Exception {
    Company company = new Company();
    company.setName(DEFAULT_COMPANY_NAME);
    company.setThema(DEFAULT_COMPANY_THEMA);
    company = companyRepository.save(company);

    User user = new User();
    user.setCompanyId(company.getId());
    user.setPassword(RandomStringUtils.random(60));
    user.setLogin("change-password-empty");
    user.setEmail("change-password-empty@example.com");
    userRepository.saveAndFlush(user);

    restMvc.perform(post("/api/account/change-password").content(RandomStringUtils.random(0))).andExpect(status().isBadRequest());

    User updatedUser = userRepository.findOneByLogin("change-password-empty").orElse(null);
    assertThat(updatedUser.getPassword()).isEqualTo(user.getPassword());
  }

  @Test
  @Transactional
  public void testRequestPasswordReset() throws Exception {
    Company company = new Company();
    company.setName(DEFAULT_COMPANY_NAME);
    company.setThema(DEFAULT_COMPANY_THEMA);
    company = companyRepository.save(company);

    User user = new User();
    user.setPassword(RandomStringUtils.random(60));
    user.setActivated(true);
    user.setLogin("password-reset");
    user.setEmail("password-reset@example.com");
    user.setCompanyId(company.getId());
    userRepository.saveAndFlush(user);

    restMvc.perform(post("/api/account/reset-password/init").content("password-reset@example.com")).andExpect(status().isOk());
  }

  @Test
  public void testRequestPasswordResetWrongEmail() throws Exception {
    restMvc.perform(post("/api/account/reset-password/init").content("password-reset-wrong-email@example.com")).andExpect(status().isBadRequest());
  }

  @Test
  @Transactional
  public void testFinishPasswordReset() throws Exception {
    Company company = new Company();
    company.setName(DEFAULT_COMPANY_NAME);
    company.setThema(DEFAULT_COMPANY_THEMA);
    company = companyRepository.save(company);

    User user = new User();
    user.setPassword(RandomStringUtils.random(60));
    user.setLogin("finish-password-reset");
    user.setEmail("finish-password-reset@example.com");
    user.setCompanyId(company.getId());
    user.setResetDate(Instant.now().plusSeconds(60));
    user.setResetKey("reset key");
    userRepository.saveAndFlush(user);

    KeyAndPasswordVM keyAndPassword = new KeyAndPasswordVM();
    keyAndPassword.setKey(user.getResetKey());
    keyAndPassword.setNewPassword("new password");

    restMvc.perform(post("/api/account/reset-password/finish").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(keyAndPassword)))
        .andExpect(status().isOk());

    User updatedUser = userRepository.findOneByLogin(user.getLogin()).orElse(null);
    assertThat(passwordEncoder.matches(keyAndPassword.getNewPassword(), updatedUser.getPassword())).isTrue();
  }

  @Test
  @Transactional
  public void testFinishPasswordResetTooSmall() throws Exception {
    Company company = new Company();
    company.setName(DEFAULT_COMPANY_NAME);
    company.setThema(DEFAULT_COMPANY_THEMA);
    company = companyRepository.save(company);

    User user = new User();
    user.setPassword(RandomStringUtils.random(60));
    user.setLogin("finish-password-reset-too-small");
    user.setEmail("finish-password-reset-too-small@example.com");
    user.setResetDate(Instant.now().plusSeconds(60));
    user.setResetKey("reset key too small");
    user.setCompanyId(company.getId());
    userRepository.saveAndFlush(user);

    KeyAndPasswordVM keyAndPassword = new KeyAndPasswordVM();
    keyAndPassword.setKey(user.getResetKey());
    keyAndPassword.setNewPassword("foo");

    restMvc.perform(post("/api/account/reset-password/finish").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(keyAndPassword)))
        .andExpect(status().isBadRequest());

    User updatedUser = userRepository.findOneByLogin(user.getLogin()).orElse(null);
    assertThat(passwordEncoder.matches(keyAndPassword.getNewPassword(), updatedUser.getPassword())).isFalse();
  }

  @Test
  @Transactional
  public void testFinishPasswordResetWrongKey() throws Exception {
    KeyAndPasswordVM keyAndPassword = new KeyAndPasswordVM();
    keyAndPassword.setKey("wrong reset key");
    keyAndPassword.setNewPassword("new password");

    restMvc.perform(post("/api/account/reset-password/finish").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(keyAndPassword)))
        .andExpect(status().isInternalServerError());
  }

}
