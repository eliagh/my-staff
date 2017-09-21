package com.mycompany.mystaff.security.social;

import java.util.Optional;

import javax.servlet.http.Cookie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;

import com.mycompany.mystaff.domain.User;
import com.mycompany.mystaff.repository.UserRepository;
import com.mycompany.mystaff.security.jwt.TokenProvider;

import io.github.jhipster.config.JHipsterProperties;

public class CustomSignInAdapter implements SignInAdapter {

  private final Logger log = LoggerFactory.getLogger(CustomSignInAdapter.class);

  private final UserDetailsService userDetailsService;

  private final JHipsterProperties jHipsterProperties;

  private final TokenProvider tokenProvider;

  private final UserRepository userRepository;

  public CustomSignInAdapter(UserDetailsService userDetailsService, JHipsterProperties jHipsterProperties, TokenProvider tokenProvider, UserRepository userRepository) {
    this.userDetailsService = userDetailsService;
    this.jHipsterProperties = jHipsterProperties;
    this.tokenProvider = tokenProvider;
    this.userRepository = userRepository;
  }

  @Override
  public String signIn(String userId, Connection<?> connection, NativeWebRequest request) {
    try {
      UserDetails loginUser = userDetailsService.loadUserByUsername(userId);
      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());

      Optional<User> user = userRepository.findOneByLogin(loginUser.getUsername());

      SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      String jwt = tokenProvider.createToken(authenticationToken, false, user.get().getCompanyId());
      ServletWebRequest servletWebRequest = (ServletWebRequest) request;
      servletWebRequest.getResponse().addCookie(getSocialAuthenticationCookie(jwt));
    } catch (AuthenticationException ae) {
      log.error("Social authentication error");
      log.trace("Authentication exception trace: {}", ae);
    }
    return jHipsterProperties.getSocial().getRedirectAfterSignIn();
  }

  private Cookie getSocialAuthenticationCookie(String token) {
    Cookie socialAuthCookie = new Cookie("social-authentication", token);
    socialAuthCookie.setPath("/");
    socialAuthCookie.setMaxAge(10);
    return socialAuthCookie;
  }

}
