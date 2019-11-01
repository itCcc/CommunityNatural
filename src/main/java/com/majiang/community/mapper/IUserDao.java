package com.majiang.community.mapper;

import com.majiang.community.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Mapper
@Repository
public interface IUserDao {

    @Select("select * from user")
    Collection<User> findAll();

    @Select("select * from user where" +
            " id=#{id}")
    User findById(Long id);

    @Select("select * from user where token=#{token}")
    List<User> findByToken(String token);

    @Insert("insert into user(account_id,account_name,token,gmt_create,gmt_modified,avatar_url) " +
            "value(#{account_id},#{account_name},#{token},#{gmt_create},#{gmt_modified},#{avatar_url})")
    void save(User user);

    @Update("update user set account_id=#{account_id} ,account_name=#{account_name},token=#{token},gmt_create=#{gmt_create}, gmt_modified=#{gmt_modified},avatar_url=#{avatar_url} where id=#{id}")
    void update(User user);

    @Select("select * from user where account_id=#{account_id}")
    User findByAccount_id(@Param("account_id") String account_id);

}
