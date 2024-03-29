package com.szmtjk.business.converter.base;

import com.szmtjk.business.bean.base.BaseDBQueryPage;
import com.szmtjk.business.bean.base.BaseQueryPage;
import com.szmtjk.business.util.PrimitiveUtil;

/**
 * Created by Jadic on 2017/12/31.
 */
public abstract class QueryConditionConverter<S extends BaseQueryPage, T extends BaseDBQueryPage> {

	public abstract T toDOCondition(S s);

	protected void paginationConvert(S src, T target) {
		Integer pageNumber = src.getPage();
		if (src.getQueryParamFlag() == 1) {
			pageNumber = src.getPageIndex() + 1;
		}
		int page = PrimitiveUtil.getPrimitive(pageNumber, 1);
		int pageSize = PrimitiveUtil.getPrimitive(src.getPageSize(), 10);
		target.setLimitNum(pageSize);
		target.setStartRow((page - 1) * pageSize);
		target.setSystemId(src.getSystemId());
	}
}
