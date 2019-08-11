package com.szmtjk.authentication.authenticator.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.szmtjk.authentication.authenticator.AbsAuthenticator;
import com.szmtjk.authentication.util.UserSessionUtil;
import com.szmtjk.business.model.User;
import com.szmtjk.business.service.UserService;
import com.szmtjk.business.util.TokenUtil;
import com.xxx.common.bean.ErrCode;
import com.xxx.common.bean.JsonRet;

@Service
public class UserAuthenticator extends AbsAuthenticator {

	@Autowired
	private UserService userService;

	@Override
	public int getUserType() {
		return USER_TYPE_WE_CHAT;
	}

	@Override
	public JsonRet<Object> authenticate(String token) {
		JsonRet<Object> jsonRet = null;
		// token 为空直接返回错误
		if (StringUtils.isBlank(token)) {
			return JsonRet.getErrRet(ErrCode.AUTHTICATION_TOKEN_EMPTY);
		}

		List<String> tokenMembers = TokenUtil.decodeUserToken(token);
		if (tokenMembers == null || tokenMembers.size() != 5) {
			return JsonRet.getErrRet(ErrCode.AUTHTICATION_TOKEN_ERROR);
		}

		int userType = Integer.parseInt(tokenMembers.get(3));
		System.out.println(111);
		if (userType != getUserType()) {
			return JsonRet.getSuccessRet(true);
		}
		System.out.println(222);
		String userId = tokenMembers.get(0);
		if (StringUtils.isBlank(userId) || !StringUtils.isNumeric(userId)) {
			return JsonRet.getErrRet(ErrCode.AUTHTICATION_TOKEN_ERROR);
		}
		System.out.println(3333);
		String expireStr = tokenMembers.get(2);
		if (StringUtils.isBlank(expireStr) || !StringUtils.isNumeric(expireStr)) {
			return JsonRet.getErrRet(ErrCode.AUTHTICATION_TOKEN_ERROR);
		}
		System.out.println(444);
		long expire = Long.valueOf(expireStr);
		// 验证 token 是否过期
		if (0 < (System.currentTimeMillis() - expire)) {
			return JsonRet.getErrRet(ErrCode.AUTHTICATION_TOKEN_EXPIRE);
		}
		System.out.println(555);
		JsonRet<User> userRet = userService.getById(Long.parseLong(userId));
		if (!userRet.isSuccess() || userRet.getData() == null) {
			return JsonRet.getErrRet(ErrCode.AUTHTICATION_TOKEN_ERROR);
		}
		System.out.println(555);
		UserSessionUtil.setCurrentUser(userRet.getData());
		return JsonRet.getSuccessRet(true);
	}
}
