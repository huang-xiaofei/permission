package com.szmtjk.authentication.authenticator.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.szmtjk.authentication.authenticator.AbsAuthenticator;
import com.szmtjk.authentication.model.LocalAuth;
import com.szmtjk.authentication.model.UserProfile;
import com.szmtjk.authentication.security.Subject;
import com.szmtjk.authentication.security.User;
import com.szmtjk.authentication.service.LocalAuthService;
import com.szmtjk.authentication.service.UserProfileService;
import com.szmtjk.authentication.util.DigestUtil;
import com.szmtjk.authentication.util.SessionUtil;
import com.szmtjk.business.util.TokenUtil;
import com.xxx.common.bean.ErrCode;
import com.xxx.common.bean.JsonRet;

/**
 * @author tsingtao_tung Created At: 2018/1/22 上午3:00.
 */
@Service
public class LocalAuthenticator extends AbsAuthenticator {

	@Autowired
	private UserProfileService userProfileService;

	@Autowired
	private LocalAuthService localAuthService;

	@Override
	public int getUserType() {
		return USER_TYPE_WEB_USER;
	}

	@Override
	public JsonRet<Object> authenticate(String token) {
		JsonRet<Object> jsonRet = JsonRet.getErrRet(ErrCode.AUTHTICATION_TOKEN_ERROR.getCode(),
				ErrCode.AUTHTICATION_TOKEN_ERROR.getMsg());
		System.out.println("开始校验");
		List<String> tokenMembers = TokenUtil.decodeUserToken(token);
		if (tokenMembers == null || tokenMembers.size() != 5) {
			return JsonRet.getErrRet(ErrCode.AUTHTICATION_TOKEN_ERROR);
		}
		int userType = Integer.parseInt(tokenMembers.get(3));
		if (userType != getUserType()) {
			return JsonRet.getSuccessRet(true);
		}
		String authIdStr = tokenMembers.get(0);
		if (StringUtils.isBlank(authIdStr) || !StringUtils.isNumeric(authIdStr)) {
			return JsonRet.getErrRet(ErrCode.AUTHTICATION_TOKEN_ERROR);
		}
		String expireStr = tokenMembers.get(2);
		if (StringUtils.isBlank(expireStr) || !StringUtils.isNumeric(expireStr)) {
			return JsonRet.getErrRet(ErrCode.AUTHTICATION_TOKEN_ERROR);
		}
		long expire = Long.valueOf(expireStr);
		// 验证 token 是否过期
		if (0 < (System.currentTimeMillis() - expire)) {
			return JsonRet.getErrRet(ErrCode.AUTHTICATION_TOKEN_EXPIRE);
		}
		long authId = Long.valueOf(authIdStr);
		String userIdStr = tokenMembers.get(4);
		long userId = Long.valueOf(userIdStr);
		String md5 = tokenMembers.get(1);
		// 根据token的id获取本地权限信息
		System.out.println("准备获取本地信息。。。");
		LocalAuth localAuth = this.localAuthService.queryById(authId);
		if (null == localAuth) {
			System.out.println("null == localAuth");
			return jsonRet;
		}

		String loginName = localAuth.getLoginName();
		String localPasswd = localAuth.getPasswd();
		String realMd5 = DigestUtil.md5(String.valueOf(authId), loginName, localPasswd, String.valueOf(expire));
		if (!StringUtils.equals(md5, realMd5)) {
			return jsonRet;
		}
		System.out.println("开始执行bindUserToSession!!!:userid=" + userId);
		this.bindUserToSession(userId);

		return JsonRet.getSuccessRet(null);
	}

	// LocalAuth表的userId对应UserProfile表的主键id
	private void bindUserToSession(Long userProfileId) {
		JsonRet<UserProfile> profileJsonRet = this.userProfileService.getById(userProfileId);
		System.out.println("userProfileService=" + JSON.toJSONString(profileJsonRet));
		UserProfile profile = profileJsonRet.getData();
		User user = new User(profile);
		Subject subject = new Subject(user, true);
		SessionUtil.setSubject(subject);
	}
}
