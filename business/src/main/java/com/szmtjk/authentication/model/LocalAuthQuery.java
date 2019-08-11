package com.szmtjk.authentication.model;

import com.szmtjk.business.bean.base.BaseQueryPage;

public class LocalAuthQuery extends BaseQueryPage {
	private String key;

	public LocalAuthQuery(){

	}

	public LocalAuthQuery(String key){
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
