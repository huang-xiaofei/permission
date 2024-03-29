package com.szmtjk.web.authentication.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.szmtjk.authentication.model.UserRoles;
import com.szmtjk.authentication.model.UserRolesQuery;
import com.szmtjk.authentication.service.UserRolesService;
import com.szmtjk.business.service.base.BaseService;
import com.szmtjk.web.config.JsonParam;
import com.szmtjk.web.controller.base.BaseCRUDController;
import com.xxx.common.bean.JsonRet;

@RequestMapping("/userRoles")
@RestController
public class UserRolesController extends BaseCRUDController<UserRoles, UserRolesQuery> {

	@Autowired
	private UserRolesService userRolesService;

	/**
	 * 设置用户角色
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/addfly", method = RequestMethod.POST)
	public JsonRet<Boolean> addfly(@JsonParam UserRoles model) {
		return userRolesService.addfly(model);
	}

	/**
	 * 根据用户加载角色
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/loadRolesByUserId", method = RequestMethod.POST)
	public List<Map<String, Object>> loadRolesByUserId(@RequestParam Map<String, Object> map) {
		return userRolesService.queryRolesByUserIdInfo(map);
	}

	@Override
	protected BaseService<UserRoles, UserRolesQuery> getBaseService() {
		return this.userRolesService;
	}
}
