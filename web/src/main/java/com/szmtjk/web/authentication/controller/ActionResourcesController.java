package com.szmtjk.web.authentication.controller;

import com.szmtjk.authentication.model.ActionResources;
import com.szmtjk.authentication.model.ActionResourcesQuery;
import com.szmtjk.authentication.service.ActionResourcesService;
import com.szmtjk.business.service.base.BaseService;
import com.xxx.common.bean.JsonRet;
import com.szmtjk.web.config.JsonParam;
import com.szmtjk.web.controller.base.BaseCRUDController;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequestMapping("/action")
@RestController
public class ActionResourcesController extends BaseCRUDController<ActionResources, ActionResourcesQuery> {
    @Autowired
    private ActionResourcesService actionResourcesService;

    @Override
    protected BaseService<ActionResources, ActionResourcesQuery> getBaseService() {
        return this.actionResourcesService;
    }

    @RequestMapping(value = "/getTree", method = RequestMethod.GET)
    public Object getTree(@JsonParam ActionResourcesQuery actionResourcesQuery) {
        JsonRet<Object> ret = super.getList(actionResourcesQuery);
        List<ActionResources> argFlys = null;

        if (ret.isSuccess()) {
            Object retData = ret.getData();
            if (retData instanceof Map) {
                Map<String, Object> paramMap = (Map<String, Object>)retData;
                Object listData = paramMap.get("list");
                if (listData instanceof List) {
                    argFlys = (List<ActionResources>)listData;
                }
            } else if (retData instanceof List) {
                argFlys = (List<ActionResources>)ret.getData();
            }
        }
        if (argFlys == null) {
            argFlys = new ArrayList<>();
        }
        ActionResources mActionResources = new ActionResources();
        mActionResources.setId(0L);
        mActionResources.setName("根权限节点");
        argFlys.add(mActionResources);
        return argFlys;
    }

    /**
     * 加载所有权限
     *
     * @return
     */
    @RequestMapping(value = "/loadResources", method = RequestMethod.POST)
    public List<Map<String, Object>> queryTreeResourcesInfo() {
        List<Map<String, Object>> mArgFlys = actionResourcesService.queryTreeResourcesInfo();
        Map<String, Object> mRootMap = new HashedMap();
        mRootMap.put("id", 0);
        mRootMap.put("name", "所有权限");
        mArgFlys.add(mRootMap);
        return mArgFlys;
    }
}
