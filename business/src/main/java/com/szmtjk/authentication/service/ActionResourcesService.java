package com.szmtjk.authentication.service;

import java.util.List;
import java.util.Map;

import com.szmtjk.authentication.model.ActionResources;
import com.szmtjk.authentication.model.ActionResourcesQuery;
import com.szmtjk.business.service.base.BaseService;

public interface ActionResourcesService extends BaseService<ActionResources, ActionResourcesQuery> {
	/**
	 * 查询用户权限
	 * 
	 * @param userId
	 * @return
	 */
	List<ActionResources> queryByUserId(Long userId);

	/**
	 * 加载权限树
	 * 
	 * @return
	 */
	List<Map<String, Object>> queryTreeResourcesInfo();

	/**
	 * @param userId
	 * @param systemId
	 * @return
	 * @author lenovo
	 * @date 2019年7月20日 下午3:23:34
	 */
	List<ActionResources> queryByUserIdAndSystemId(Long userId, int systemId);
}
