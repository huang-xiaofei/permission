package com.szmtjk.business.util;

import java.util.Base64;
import java.util.List;

import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import com.szmtjk.authentication.authenticator.Authenticator;
import com.szmtjk.authentication.model.LocalAuth;
import com.szmtjk.authentication.util.DigestUtil;
import com.szmtjk.business.model.User;

public class TokenUtil {

	private static final long TOKEN_EXPIRE_TIME = 30 * 24 * 3600 * 1000L;// token有效期30天

	public static String encodeUserToken(User user) {

		long expire = System.currentTimeMillis() + TOKEN_EXPIRE_TIME;
		String md5 = DigestUtil.md5(String.valueOf(user.getId()), user.getUserName(), String.valueOf(expire));
		String token = user.getId() + ":" + md5 + ":" + expire + ":" + Authenticator.USER_TYPE_WE_CHAT;
		token = Base64.getUrlEncoder().withoutPadding().encodeToString(token.getBytes());
		return token;
	}

	public static String encodeUserToken(LocalAuth user) {
		long expire = System.currentTimeMillis() + TOKEN_EXPIRE_TIME;
		String md5 = DigestUtil.md5(String.valueOf(user.getId()), user.getLoginName(), user.getPasswd(),
				String.valueOf(expire));
		String token = user.getId() + ":" + md5 + ":" + expire + ":" + Authenticator.USER_TYPE_WEB_USER + ":"
				+ user.getUserId();
		System.out.println("生成的token：：：" + token);
		token = Base64.getUrlEncoder().withoutPadding().encodeToString(token.getBytes());
		return token;
	}

	public static void main(String[] args) {
		List<String> l = decodeUserToken("NDQ6ZTk0ZjBiNWM5Mzk1ODMxNDZiMWZjOTcwMmZkMWIzYTQ6MTU2NTk0MzYzMDU1ODoy");
		System.out.println(l);
	}

	public static List<String> decodeUserToken(String token) {
		if (!StringUtils.isEmpty(token)) {
			String tokenDecoded = new String(Base64.getUrlDecoder().decode(token.getBytes()));
			String[] tokenMembers = tokenDecoded.split(":");
			return Lists.newArrayList(tokenMembers);
		}
		return null;
	}
}
