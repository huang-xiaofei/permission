package com.szmtjk.web.controller;

import org.springframework.beans.factory.annotation.Autowired;

import com.szmtjk.business.model.UserThirdPartyDetail;
import com.szmtjk.business.model.UserThirdPartyDetailQuery;
import com.szmtjk.business.service.UserThirdPartyDetailService;
import com.szmtjk.business.service.base.BaseService;
import com.szmtjk.web.controller.base.BaseCRUDController;

/**
 * 三方信息
 */
//@RestController
//@RequestMapping("/userThirdPartyDetail")
public class UserThirdPartyDetailController
		extends BaseCRUDController<UserThirdPartyDetail, UserThirdPartyDetailQuery> {

	@Autowired
	private UserThirdPartyDetailService userThirdPartyDetailService;

	@Override
	protected BaseService<UserThirdPartyDetail, UserThirdPartyDetailQuery> getBaseService() {
		return userThirdPartyDetailService;
	}
}
