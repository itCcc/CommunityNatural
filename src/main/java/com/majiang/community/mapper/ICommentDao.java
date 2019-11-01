package com.majiang.community.mapper;

import com.majiang.community.enums.CommentTypeEnum;
import com.majiang.community.model.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ICommentDao {


    @Insert("insert into comment(parent_id,type,commentator,gmt_create,gmt_modified,content,like_count,comment_count) " +
            "value(#{parent_id},#{type},#{commentator},#{gmt_create},#{gmt_modified},#{content},#{like_count},#{comment_count})")
    void save(Comment comment);

    @Select("select * from comment where id=#{parent_id}")
    Comment findByPrimaryKey(Long parent_id);

    @Select("select * from comment where parent_id=#{id} and type=#{type} order by gmt_create desc")
    List<Comment> findByQuestionId(Long id, Integer type);

    @Select("update comment set comment_count=(#{comment_count}+#{jdbcType}) where id=#{id}")
    void setComment_count(Long id, Integer comment_count,Integer jdbcType);

    @Update("update comment set comment_count=#{jdbcInitialize} where id=#{id}")
    void initializeComment_count(Long id, Integer jdbcInitialize);
}
