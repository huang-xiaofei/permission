package com.szmtjk.authentication.db.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.szmtjk.authentication.db.entity.ActionResourcesDBQuery;
import com.szmtjk.authentication.db.entity.ActionResourcesDO;
import com.szmtjk.business.db.dao.base.BaseDAO;
import com.xxx.boot.jdbc.annotation.Dao;

@Dao
public interface ActionResourcesDAO extends BaseDAO<ActionResourcesDO, ActionResourcesDBQuery> {
	/**
	 * 查询用户操作权限
	 * 
	 * @param userId
	 * @return
	 */
	List<ActionResourcesDO> queryByUserId(Long userId);

	/**
	 * 根据系统 查询用户操作权限
	 * 
	 * @param userId
	 * @return
	 */
	List<ActionResourcesDO> queryByUserIdAndSystemId(@Param(value = "userId") Long userId,
			@Param(value = "systemId") int systemId);

	/**
	 * 加载权限树
	 * 
	 * @return
	 */
	List<Map<String, Object>> queryTreeResourcesInfo();

	/**
	 * 修改父节点的节点类型
	 * 
	 * @param map
	 * @return
	 */
	int modifyParentLeft(@Param("pojo") Map<String, Object> map);

	int qureyParentByIdInfoCnt(@Param("id") Integer id);
}
