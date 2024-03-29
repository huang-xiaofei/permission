package com.szmtjk.authentication.service.wechat;

import com.szmtjk.authentication.util.HttpClientUtil;
import com.szmtjk.business.bean.wechat.AppSecretConfig;
import com.szmtjk.business.bean.wechat.AccessTokenResponse;
import com.szmtjk.business.bean.wechat.OpenIdResponse;
import com.szmtjk.business.bean.wechat.TemplateMsgData;
import com.szmtjk.business.bean.wechat.UnionIdResponse;
import com.szmtjk.business.bean.wechat.ValueColorPair;
import com.szmtjk.business.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xiaohu on 2018/10/26.
 */
@Service
public class WeChatService {

    private static final Logger LOG = LoggerFactory.getLogger(WeChatService.class);

    @Autowired
    private WeChatAppSecretConfigs weChatAppSecretConfigs;

    private Map<String, String> tokenMap = new ConcurrentHashMap<>();
    private Map<Integer, AppSecretConfig> appSecretConfigMap = new ConcurrentHashMap<>();

    public String getAccessToken(String appId, String appSecret) {
        String tokenKey = appId + "_" + appSecret;
        String accessToken = tokenMap.get(tokenKey);
        if (StringUtils.isEmpty(accessToken)) {
            AccessTokenResponse checkedAccessToken = getCheckedAccessToken(appId, appSecret);
            if (checkedAccessToken != null && !StringUtils.isEmpty(checkedAccessToken.getAccessToken())) {
                accessToken = checkedAccessToken.getAccessToken();
                tokenMap.put(tokenKey, accessToken);
            }
        }
        return accessToken;
    }

    private AccessTokenResponse getCheckedAccessToken(String appId, String appSecret) {
        String baseUrl = "https://api.weixin.qq.com/cgi-bin/token";
        Map<String, String> params = new HashMap<>();
        params.put("appid", appId);
        params.put("secret", appSecret);
        params.put("grant_type", "client_credential");
        String response = HttpClientUtil.doGet(HttpClientUtil.buildUrl(baseUrl, params));
        return getWeChatResponse(response, AccessTokenResponse.class);
    }

    public void addAccessTokenKey(String appId, String appSecret) {
        String tokenKey = appId + "_" + appSecret;
        String accessToken = tokenMap.get(tokenKey);
        if (accessToken == null) {
            AccessTokenResponse checkedAccessToken = getCheckedAccessToken(appId, appSecret);
            if (checkedAccessToken != null && !StringUtils.isEmpty(checkedAccessToken.getAccessToken())) {
                accessToken = checkedAccessToken.getAccessToken();
                tokenMap.put(tokenKey, accessToken);
            }
        }
    }

    public AppSecretConfig getAppSecretConfig(int appType) {
        return appSecretConfigMap.get(appType);
    }


    public OpenIdResponse getOpenId(String appId, String appSecret, String code) {
        String baseUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";
        Map<String, String> params = new HashMap<>();
        params.put("appid", appId);
        params.put("secret", appSecret);
        params.put("code", code);
        params.put("grant_type", "authorization_code");
        String url = HttpClientUtil.buildUrl(baseUrl, params);
        String response = HttpClientUtil.doGet(url);
        return getWeChatResponse(response, OpenIdResponse.class);
    }

    public OpenIdResponse getMiniProgramOpenId(String appId, String appSecret, String code) {
        String baseUrl = "https://api.weixin.qq.com/sns/jscode2session";
        Map<String, String> params = new HashMap<>();
        params.put("appid", appId);
        params.put("secret", appSecret);
        params.put("js_code", code);
        params.put("grant_type", "authorization_code");
        String url = HttpClientUtil.buildUrl(baseUrl, params);
        String response = HttpClientUtil.doGet(url);
        return getWeChatResponse(response, OpenIdResponse.class);
    }

    public UnionIdResponse getUnionId(String accessToken, String openId) {
        String baseUrl = "https://api.weixin.qq.com/sns/userinfo";
        Map<String, String> params = new HashMap<>();
        params.put("access_token", accessToken);
        params.put("openid", openId);
        params.put("lang", "zh_CN");
        String url = HttpClientUtil.buildUrl(baseUrl, params);
        String response = HttpClientUtil.doGet(url);
        System.out.println(response);
        if (!StringUtils.isEmpty(response)) {
            try {
                UnionIdResponse openIdResponse = JsonUtil.toObject(response, UnionIdResponse.class);
                return openIdResponse;
            } catch (Exception e) {
                LOG.error("deserialize json err, raw:{}", response, e);
            }
        }
        return getWeChatResponse(response, UnionIdResponse.class);
    }

    public String sendTestTemplateMsg(String toUser, String accessToken, String testFirst, String testRemark) {
        if (StringUtils.isEmpty(testFirst)) {
            testFirst = "这是测试内容";
        }
        if (StringUtils.isEmpty(testRemark)) {
            testRemark = "有问题请联系尽早联系调度员";
        }
        TemplateMsgData templateMsgData = new TemplateMsgData();
        templateMsgData.setToUser(toUser);
        templateMsgData.setTemplateId("rxjqJPbwa-pWUfE3sr4ppKpN7MBsTn1PWvjHFrxB_OE");
        Map<String, ValueColorPair> dataMap = new HashMap<>();
        dataMap.put("first", new ValueColorPair(testFirst, "#120FE9"));
        dataMap.put("keyword1", new ValueColorPair("1a2399ad", "#120FE9"));
        dataMap.put("keyword2", new ValueColorPair("小王", "#120FE9"));
        dataMap.put("keyword3", new ValueColorPair("10月23日 13:12", "#120FE9"));
        dataMap.put("keyword4", new ValueColorPair("南京->北京", "#120FE9"));
        dataMap.put("keyword5", new ValueColorPair("苏A 12345", "#120FE9"));
        dataMap.put("remark", new ValueColorPair(testRemark, "#120FE9"));
        templateMsgData.setData(dataMap);
        String baseUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken;
        return HttpClientUtil.post(baseUrl, JsonUtil.toJson(templateMsgData));
    }

    private <T> T getWeChatResponse(String response, Class<T> clazz) {
        if (!StringUtils.isEmpty(response)) {
            try {
                return JsonUtil.toObject(response, clazz);
            } catch (Exception e) {
                LOG.error("deserialize json err, raw:{}", response, e);
            }
        }
        return null;
    }

    @PostConstruct
    private void init() {
        if (CollectionUtils.isEmpty(weChatAppSecretConfigs.getAppSecretConfigList())) {
            return;
        }
        for (AppSecretConfig config : weChatAppSecretConfigs.getAppSecretConfigList()) {
            appSecretConfigMap.put(config.getAppType(), config);
            this.addAccessTokenKey(config.getAppId(), config.getAppSecret());
        }
    }

    @Scheduled(fixedRate = 3600 * 1000, initialDelay = 1800 * 000L)
    public void refresh() {
        tokenMap.keySet().forEach(this::refreshToken);
    }

    private void refreshToken(String tokenKey) {
        if (!StringUtils.isEmpty(tokenKey)) {
            String[] split = tokenKey.split("_");
            if (split.length == 2) {
                String appId = split[0];
                String appSecret = split[1];
                AccessTokenResponse checkedAccessToken = getCheckedAccessToken(appId, appSecret);
                if (checkedAccessToken != null && !StringUtils.isEmpty(checkedAccessToken.getAccessToken())) {
                    String accessToken = checkedAccessToken.getAccessToken();
                    tokenMap.put(tokenKey, accessToken);
                    LOG.info("refresh token, key:{}, new token:{}", tokenKey, accessToken);
                }
            }
        }

    }
}
