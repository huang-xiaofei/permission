package com.szmtjk.business.db.config;

import com.xxx.boot.jdbc.annotation.Dao;
import com.xxx.boot.jdbc.annotation.MobMapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Jadic on 2017/12/31.
 */
@Configuration
@MobMapperScan(basePackages = {
        "com.szmtjk"
}, annotationClass = Dao.class)
public class DBConfig {

}
