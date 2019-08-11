package com.szmtjk.web.authentication.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.szmtjk.authentication.model.DataDictionary;
import com.szmtjk.authentication.model.DataDictionaryQuery;
import com.szmtjk.authentication.service.DataDictionaryService;
import com.szmtjk.business.service.base.BaseService;
import com.szmtjk.web.config.JsonParam;
import com.szmtjk.web.controller.base.BaseCRUDController;
import com.xxx.common.bean.JsonRet;

@RequestMapping("/dataDictionary")
@RestController
public class DataDictionaryController extends BaseCRUDController<DataDictionary, DataDictionaryQuery> {
	@Autowired
	private DataDictionaryService dataDictionaryService;

	@RequestMapping(value = "/getTree", method = RequestMethod.GET)
	public Object getTree(@JsonParam DataDictionaryQuery dataDictionaryQuery) {
		JsonRet<Object> ret = super.getList(dataDictionaryQuery);
		if (ret.isSuccess()) {
			return ret.getData();
		}
		return null;
	}

	@Override
	protected BaseService<DataDictionary, DataDictionaryQuery> getBaseService() {
		return this.dataDictionaryService;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getTreeSp", method = RequestMethod.GET)
	public Object getTreeSp(@JsonParam DataDictionaryQuery dataDictionaryQuery) {
		System.out.println("getTreeSp DataDictionaryQuery："+JSON.toJSONString(dataDictionaryQuery));
		JsonRet<Object> ret = super.getList(dataDictionaryQuery);
		System.out.println("getTreeSp ret："+JSON.toJSONString(ret));
		Map<String, Object> params = (Map<String, Object>) ret.getData();
		List<DataDictionary> lists = (List<DataDictionary>) params.get("list");
		System.out.println("getTreeSp："+JSON.toJSONString(lists));
		List<DataDictionary> rootList = new ArrayList<DataDictionary>();
		//用于筛选parentId相同的节点
		Map<Long, List<DataDictionary>> parentIdMaper = new HashMap<Long, List<DataDictionary>>();
		for (DataDictionary dictionary : lists) {
			long parentId = dictionary.getParentId();
			if (dictionary.getParentId() == 0L) {
				rootList.add(dictionary);//parentId节点为0的代表根节点
			}
			
			for (DataDictionary dataDictionaryDO2 : lists) {
				if (parentId == dataDictionaryDO2.getParentId()) {//筛选出parentId相同的节点到list中
					if (parentIdMaper.get(parentId) == null) {
						parentIdMaper.put(parentId, new ArrayList<DataDictionary>());
					}
					List<DataDictionary> dataDictionaries = parentIdMaper.get(parentId);
					if (!dataDictionaries.contains(dataDictionaryDO2)) {
						dataDictionaries.add(dataDictionaryDO2);
					}
				}
			}
		}
		Map<String, List< Map<String, List<DataDictionary>>>> result = new HashMap<String, List< Map<String, List<DataDictionary>>>>();
		for (DataDictionary root : rootList) {
			List<DataDictionary> sonList = parentIdMaper.get(root.getId());
			if (sonList == null) {//没子节点直接跳过
				continue;
			}
			//存取所有子节点
			List< Map<String, List<DataDictionary>>> sonResult = new ArrayList<Map<String, List<DataDictionary>>>();
			for (DataDictionary son : sonList) {
				List<DataDictionary> grandSonList = parentIdMaper.get(son.getId());//获取所有孙子节点
				Map<String, List<DataDictionary>> grandSonResult = new HashMap<String, List<DataDictionary>>();
				grandSonResult.put(son.getCode(), grandSonList);
				sonResult.add(grandSonResult);
			}
			result.put(root.getCode(), sonResult);
		}

		System.out.println(JSON.toJSON(result));
		if (ret.isSuccess()) {
			ret.setData(result);
			return JSON.toJSON(ret);
		}
		return null;
	}

}
