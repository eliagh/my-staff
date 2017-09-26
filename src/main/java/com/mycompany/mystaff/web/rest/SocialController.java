package com.mycompany.mystaff.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.support.URIBuilder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.view.RedirectView;

import com.mycompany.mystaff.domain.Company;
import com.mycompany.mystaff.service.CompanyService;
import com.mycompany.mystaff.service.SocialService;

@RestController
@RequestMapping("/social")
public class SocialController {

  private final Logger log = LoggerFactory.getLogger(SocialController.class);

  private final SocialService socialService;

  private final CompanyService companyService;

  private final ProviderSignInUtils providerSignInUtils;

  public SocialController(SocialService socialService, ProviderSignInUtils providerSignInUtils, CompanyService companyService) {
    this.socialService = socialService;
    this.providerSignInUtils = providerSignInUtils;
    this.companyService = companyService;
  }

  @GetMapping("/signup")
  public RedirectView signUp(WebRequest webRequest, @CookieValue(name = "NG_TRANSLATE_LANG_KEY", required = false, defaultValue = "\"en\"") String langKey) {
    try {
      String userLangKey = langKey.replace("\"", "");
      Connection<?> connection = providerSignInUtils.getConnectionFromSession(webRequest);

      Company company = companyService.create(userLangKey);
      socialService.createSocialUser(connection, userLangKey, company.getId());
      return new RedirectView(URIBuilder.fromUri("/#/social-register/" + connection.getKey().getProviderId()).queryParam("success", "true").build().toString(), true);
    } catch (Exception e) {
      log.error("Exception creating social user: ", e);
      return new RedirectView(URIBuilder.fromUri("/#/social-register/no-provider").queryParam("success", "false").build().toString(), true);
    }
  }

}
