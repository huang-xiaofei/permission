package com.szmtjk.business.db.entity;

import com.szmtjk.business.bean.base.BaseDBQueryPage;

/**
 * auto generated by code helper on 2019-03-04.
 */
public class FileImportStatusDBQuery extends BaseDBQueryPage {

    /**
     * 导入业务类型  1：预约文件   2：体检报告
     */
    private Integer bizType;
    /**
     * 文件名
     */
    private String fileName;

    public Integer getBizType() {
        return bizType;
    }

    public void setBizType(Integer bizType) {
        this.bizType = bizType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
