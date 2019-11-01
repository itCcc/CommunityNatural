package com.majiang.community.mapper;

import com.majiang.community.dto.QuestionDTO;
import com.majiang.community.dto.QuestionQueryDTO;
import com.majiang.community.model.Comment;
import com.majiang.community.model.Question;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collection;
import java.util.List;

@Mapper
@Repository
public interface IQuestionDao {

    @Select("select * from question where(title regexp #{search}) order by gmt_create desc Limit #{page},#{size}")
    List<Question> searchFindAll(QuestionQueryDTO questionQueryDTO);

    @Select("select * from question order by gmt_create desc Limit #{offset},#{size}")
    List<Question> findAll(@Param("offset") Integer offset,@Param("size") Integer size);

    @Select("select count(1) from question ")
    Integer count();

    @Insert("insert into question(title,description,gmt_create,gmt_modified,creator,tag) " +
            "value(#{title},#{description},#{gmt_create},#{gmt_modified},#{creator},#{tag})")
    void save(Question question);

    @Select("select * from question where creator= #{userId} order by gmt_create desc limit #{offset},#{size}")
    List<Question> listByUserId(@Param("userId") Long userId,@Param("offset") Integer offset,@Param("size") Integer size);

    @Select("select count(1) from question where creator = #{userId} ")
    Integer countByUserId(@Param("userId") Long userId);

    @Select("select * from question where id=#{id}")
    Question finById(@Param("id") Long id);

    @Update("update question set title=#{title},description=#{description},gmt_modified=#{gmt_modified},tag=#{tag} where id=#{id}")
    void update(Question question);

    @Update("update question set view_count=#{view_count}+1 where id=#{id}")
    void setViewCount(Long id, Long view_count);

    @Update("update question set comment_count=(#{comment_count} + #{jdbcType})  where id=#{id}")
    void setComment_count(Long id, Long comment_count,Long jdbcType);

    @Select("select * from question where id=#{parent_id}")
    Question findByPrimaryKey(Long parent_id);

    @Select("select * from question where id!=#{id} and tag regexp #{regexpTag}")
    List<QuestionDTO> selectRelated(Long id,String regexpTag);

    @Select("select count(*) from question where( title regexp #{search})")
    Integer searchCount(String search);
}
