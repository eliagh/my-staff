package com.mycompany.mystaff.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.mystaff.config.Constants;
import com.mycompany.mystaff.domain.User;
import com.mycompany.mystaff.repository.UserRepository;
import com.mycompany.mystaff.repository.search.UserSearchRepository;
import com.mycompany.mystaff.security.AuthoritiesConstants;
import com.mycompany.mystaff.security.jwt.JWTConfigurer;
import com.mycompany.mystaff.security.jwt.TokenProvider;
import com.mycompany.mystaff.service.MailService;
import com.mycompany.mystaff.service.UserService;
import com.mycompany.mystaff.service.dto.UserDTO;
import com.mycompany.mystaff.service.util.ResolveTokenUtil;
import com.mycompany.mystaff.web.rest.util.HeaderUtil;
import com.mycompany.mystaff.web.rest.util.PaginationUtil;
import com.mycompany.mystaff.web.rest.vm.ManagedUserVM;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing users.
 * <p>
 * This class accesses the User entity, and needs to fetch its collection of authorities.
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and
 * Authority, and send everything to the client side: there would be no View Model and DTO, a lot
 * less code, and an outer-join which would be good for performance.
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all the
 * time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li>Not having an outer join causes n+1 requests to the database. This is not a real issue as we
 * have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer
 * join (which will get lots of data from the database, for each HTTP call).</li>
 * <li>As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this case.
 */
@RestController
@RequestMapping("/api")
public class UserResource {

  private final Logger log = LoggerFactory.getLogger(UserResource.class);

  private static final String ENTITY_NAME = "userManagement";

  private final HttpServletRequest request;

  private final UserRepository userRepository;

  private final MailService mailService;

  private final UserService userService;

  private final UserSearchRepository userSearchRepository;

  private final TokenProvider tokenProvider;

  public UserResource(UserRepository userRepository, MailService mailService, UserService userService, UserSearchRepository userSearchRepository, HttpServletRequest request,
      TokenProvider tokenProvider) {
    this.userRepository = userRepository;
    this.mailService = mailService;
    this.userService = userService;
    this.userSearchRepository = userSearchRepository;
    this.request = request;
    this.tokenProvider = tokenProvider;
  }

  /**
   * POST /users : Creates a new user.
   * <p>
   * Creates a new user if the login and email are not already used, and sends an mail with an
   * activation link. The user needs to be activated on creation.
   *
   * @param managedUserVM the user to create
   * @return the ResponseEntity with status 201 (Created) and with body the new user, or with status
   *         400 (Bad Request) if the login or email is already in use
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/users")
  @Timed
  @Secured({ AuthoritiesConstants.COMPANY_ADMIN, AuthoritiesConstants.LOCATION_ADMIN })
  public ResponseEntity<User> createUser(@Valid @RequestBody ManagedUserVM managedUserVM) throws URISyntaxException {
    log.debug("REST request to save User : {}", managedUserVM);

    if (managedUserVM.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new user cannot already have an ID")).body(null);
      // Lowercase the user login before comparing with database
    } else if (userRepository.findOneByLogin(managedUserVM.getLogin().toLowerCase()).isPresent()) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "userexists", "Login already in use")).body(null);
    } else if (userRepository.findOneByEmail(managedUserVM.getEmail()).isPresent()) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "emailexists", "Email already in use")).body(null);
    } else {
      final Long companyId = tokenProvider.getCompanyId(ResolveTokenUtil.resolveToken(request.getHeader(JWTConfigurer.AUTHORIZATION_HEADER)));
      managedUserVM.setCompanyId(companyId);

      User newUser = userService.createUser(managedUserVM);
      mailService.sendCreationEmail(newUser);
      return ResponseEntity.created(new URI("/api/users/" + newUser.getLogin())).headers(HeaderUtil.createAlert("userManagement.created", newUser.getLogin())).body(newUser);
    }
  }

  /**
   * PUT /users : Updates an existing User.
   *
   * @param managedUserVM the user to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated user, or with status
   *         400 (Bad Request) if the login or email is already in use, or with status 500 (Internal
   *         Server Error) if the user couldn't be updated
   */
  @PutMapping("/users")
  @Timed
  @Secured({ AuthoritiesConstants.COMPANY_ADMIN, AuthoritiesConstants.LOCATION_ADMIN })
  public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody ManagedUserVM managedUserVM) {
    log.debug("REST request to update User : {}", managedUserVM);

    final Long companyId = tokenProvider.getCompanyId(ResolveTokenUtil.resolveToken(request.getHeader(JWTConfigurer.AUTHORIZATION_HEADER)));

    Optional<User> existingUser = userRepository.findOneByEmail(managedUserVM.getEmail());
    if (!companyId.equals(existingUser.get().getCompanyId())) {
      return ResponseEntity.notFound().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "emailexists", "User not found")).build();
    }

    if (existingUser.isPresent() && (!existingUser.get().getId().equals(managedUserVM.getId()))) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "emailexists", "Email already in use")).body(null);
    }
    existingUser = userRepository.findOneByLogin(managedUserVM.getLogin().toLowerCase());
    if (existingUser.isPresent() && (!existingUser.get().getId().equals(managedUserVM.getId()))) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "userexists", "Login already in use")).body(null);
    }
    Optional<UserDTO> updatedUser = userService.updateUser(managedUserVM);

    return ResponseUtil.wrapOrNotFound(updatedUser, HeaderUtil.createAlert("userManagement.updated", managedUserVM.getLogin()));
  }

  /**
   * GET /users : get all users.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and with body all users
   */
  @GetMapping("/users")
  @Timed
  public ResponseEntity<List<UserDTO>> getAllUsers(@ApiParam Pageable pageable) {
    final Long companyId = tokenProvider.getCompanyId(ResolveTokenUtil.resolveToken(request.getHeader(JWTConfigurer.AUTHORIZATION_HEADER)));

    final Page<UserDTO> page = userService.getAllManagedUsers(pageable, companyId);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

  /**
   * @return a string list of the all of the roles
   */
  @GetMapping("/users/authorities")
  @Timed
  @Secured({ AuthoritiesConstants.COMPANY_ADMIN, AuthoritiesConstants.LOCATION_ADMIN })
  public List<String> getAuthorities() {
    return userService.getAuthorities();
  }

  /**
   * GET /users/:login : get the "login" user.
   *
   * @param login the login of the user to find
   * @return the ResponseEntity with status 200 (OK) and with body the "login" user, or with status
   *         404 (Not Found)
   */
  @GetMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
  @Timed
  public ResponseEntity<UserDTO> getUser(@PathVariable String login) {
    log.debug("REST request to get User : {}", login);
    return ResponseUtil.wrapOrNotFound(userService.getUserWithAuthoritiesByLogin(login).map(UserDTO::new));
  }

  /**
   * DELETE /users/:login : delete the "login" User.
   *
   * @param login the login of the user to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
  @Timed
  @Secured({ AuthoritiesConstants.COMPANY_ADMIN, AuthoritiesConstants.LOCATION_ADMIN })
  public ResponseEntity<Void> deleteUser(@PathVariable String login) {
    log.debug("REST request to delete User: {}", login);
    userService.deleteUser(login);
    return ResponseEntity.ok().headers(HeaderUtil.createAlert("userManagement.deleted", login)).build();
  }

  /**
   * SEARCH /_search/users/:query : search for the User corresponding to the query.
   *
   * @param query the query to search
   * @return the result of the search
   */
  @GetMapping("/_search/users/{query}")
  @Timed
  public List<User> search(@PathVariable String query) {
    return StreamSupport.stream(userSearchRepository.search(queryStringQuery(query)).spliterator(), false).collect(Collectors.toList());
  }

}
