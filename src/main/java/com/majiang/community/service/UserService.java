package com.majiang.community.service;

import com.majiang.community.exception.CustomizeErrorCode;
import com.majiang.community.exception.CustomizeException;
import com.majiang.community.mapper.IUserDao;
import com.majiang.community.model.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.undo.CannotUndoException;

import static com.majiang.community.exception.CustomizeErrorCode.*;

@Service
public class UserService {

    @Autowired
   private IUserDao iUserDao;


    public void createOrUpdate(User user) {
        User byUser = iUserDao.findByAccount_id(user.getAccount_id());
        if (byUser==null){
            user.setGmt_create(System.currentTimeMillis());
            user.setGmt_modified(user.getGmt_create());
            //创建
            iUserDao.save(user);
        }else{
            byUser.setGmt_create(System.currentTimeMillis());
            byUser.setGmt_modified(byUser.getGmt_create());
            byUser.setAvatar_url(user.getAvatar_url());
            byUser.setAccount_name(user.getAccount_name());
            byUser.setToken(user.getToken());
            iUserDao.update(byUser);
        }
    }

    public User findById(Long id) {
        User user = iUserDao.findById(id);
        if (user == null) {
            throw new CustomizeException(NOT_USER);
        }
        return user;
    }
}
