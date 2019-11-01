package com.majiang.community.mapper;

import com.majiang.community.dto.PaginationDTO;
import com.majiang.community.model.Notification;
import com.majiang.community.model.Question;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface INotificationDao {

    @Insert("insert into notification(notifier,receiver,outer_id,type,mgt_create,status,notifier_name,outer_title) " +
            "value(#{notifier},#{receiver},#{outer_id},#{type},#{mgt_create},#{status},#{notifier_name},#{outer_title})")
    void save(Notification notification);


    @Select("select count(*) from notification where receiver=#{userId}")
    Integer countByUserId(Long userId);

    @Select("select * from notification where receiver=#{userId} order by mgt_create desc limit #{offset},#{size}")
    List<Notification> listByUserId(@Param("userId") Long userId,@Param("offset") Integer offset,@Param("size") Integer size);

    @Select("select count(status) from notification where receiver=#{userId} and status=#{jdbcStatus}")
    Long countByStatus(Long userId,Integer jdbcStatus);

    @Select("select * from notification where id=#{id}")
    Notification selectByPrimary_id(Long id);

    @Update("update notification set status=#{jdbcStatus} where id=id")
    void setStatus(Long id,Integer jdbcStatus);


}
