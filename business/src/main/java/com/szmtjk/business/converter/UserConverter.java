package com.szmtjk.business.converter;

import com.szmtjk.business.converter.base.ModelConverter;
import com.szmtjk.business.db.entity.UserDO;
import com.szmtjk.business.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * auto generated by code helper on 2019-01-05.
 */
@Component
public class UserConverter extends ModelConverter<UserDO, User> {

    @Override
    public UserDO toDataObject(User src) {
        UserDO dst = new UserDO();
        if (src != null) {
            BeanUtils.copyProperties(src, dst);
        }
        return dst;
    }

    @Override
    public User toModel(UserDO src) {
        User dst = new User();
        if (src != null) {
            BeanUtils.copyProperties(src, dst);
        }
        return dst;
    }

}