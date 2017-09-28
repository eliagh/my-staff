package com.mycompany.mystaff.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

  public static final String ANONYMOUS = "ROLE_ANONYMOUS";

  public static final String ADMIN = "ROLE_ADMIN";

  public static final String USER = "ROLE_USER";

  public static final String COMPANY_ADMIN = "ROLE_COMPANY_ADMIN";

  public static final String LOCATION_ADMIN = "ROLE_LOCATION_ADMIN";

  public static final String STAFF = "ROLE_STAFF";

  private AuthoritiesConstants() {
  }

}
