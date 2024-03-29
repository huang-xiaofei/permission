package com.szmtjk.business.model;

import com.szmtjk.business.bean.base.BaseQueryPage;

/**
 * Created by xiaohu on 2018/8/25.
 */
public class OperationLogQuery extends BaseQueryPage {

    private Long userId;
    private String key;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
