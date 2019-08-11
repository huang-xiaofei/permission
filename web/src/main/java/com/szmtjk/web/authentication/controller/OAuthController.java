package com.szmtjk.web.authentication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.szmtjk.authentication.model.OAuth;
import com.szmtjk.authentication.model.OAuthQuery;
import com.szmtjk.authentication.service.OAuthService;
import com.szmtjk.business.service.base.BaseService;
import com.szmtjk.web.controller.base.BaseCRUDController;

//@RequestMapping("/oAuth")
//@RestController
@Deprecated
public class OAuthController extends BaseCRUDController<OAuth, OAuthQuery> {
	@Autowired
	@Qualifier(value = "oauthServiceImpl")
	private OAuthService oAuthService;

	@Override
	protected BaseService<OAuth, OAuthQuery> getBaseService() {
		return this.oAuthService;
	}
}
