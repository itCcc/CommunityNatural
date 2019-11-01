package com.majiang.community.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.majiang.community.dto.*;
import com.majiang.community.enums.CommentTypeEnum;
import com.majiang.community.exception.CustomizeErrorCode;
import com.majiang.community.exception.CustomizeException;
import com.majiang.community.mapper.ICommentDao;
import com.majiang.community.mapper.IQuestionDao;
import com.majiang.community.mapper.IUserDao;
import com.majiang.community.model.Comment;
import com.majiang.community.model.Question;
import com.majiang.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.undo.CannotUndoException;
import java.beans.Beans;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private IUserDao iUserDao;

    @Autowired
    private IQuestionDao iQuestionDao;

    @Autowired
    private ICommentDao iCommentDao;


    public PaginationDTO list(String search, Integer page, Integer size) {
        Integer totalPage;
        Integer totalCount;
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        if (StringUtils.isNotBlank(search)) {
            String[] tags = search.split("");
            search = Arrays.stream(tags).collect(Collectors.joining("|"));
            questionQueryDTO.setSearch(search);
            totalCount = iQuestionDao.searchCount(search);
            if (totalCount == 0) {
                return null;
            }
        } else {
            totalCount = iQuestionDao.count();
        }
        PaginationDTO paginationDTO=new PaginationDTO();
        totalPage=(totalCount%size==0)?totalCount/size:totalCount/size+1;
        if (page<1){
            page=1;
        }
        if (page>totalPage){
            page=totalPage;
        }
        Integer offset=size*(page-1);
        paginationDTO.setPagination(totalPage,page);
        questionQueryDTO.setPage(offset);
        questionQueryDTO.setSize(size);
        List<Question> questionList;
        if (StringUtils.isNotBlank(questionQueryDTO.getSearch())) {
            questionList = iQuestionDao.searchFindAll(questionQueryDTO);
        } else {
            questionList = iQuestionDao.findAll(offset, size);
        }

        List<QuestionDTO> questionDTOList=new ArrayList<>();
        for (Question question : questionList) {
            User user=iUserDao.findById(question.getCreator());
            QuestionDTO questionDTO=new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);

        return paginationDTO;
    }

    public PaginationDTO listUser(Long userId, Integer page, Integer size) {
        PaginationDTO paginationDTO=new PaginationDTO();
        Integer totalPage;
        Integer totalCount = iQuestionDao.countByUserId(userId);
        totalPage=(totalCount%size==0)?totalCount/size:totalCount/size+1;
        if (page<1){
            page=1;
        }
        if (page>totalPage){
            page=totalPage;
        }
        paginationDTO.setPagination(totalPage,page);
        Integer offset=size*(page-1);
        List<Question>  questionList =iQuestionDao.listByUserId(userId,offset,size);
        List<QuestionDTO> questionDTOList=new ArrayList<>();
        for (Question question : questionList) {
            User user=iUserDao.findById(question.getCreator());
            QuestionDTO questionDTO=new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);

        return paginationDTO;
    }

    public QuestionDTO finById(Long id) {
        Question question = iQuestionDao.finById(id);
        if (question==null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO=new QuestionDTO();
        User user=iUserDao.findById(question.getCreator());
        questionDTO.setUser(user);
        BeanUtils.copyProperties(question,questionDTO);
        return questionDTO;
    }

        public void createOrUpdate(Question question) {
        if (question.getId()==null){
            question.setGmt_create(System.currentTimeMillis());
            question.setGmt_modified(question.getGmt_create());
            iQuestionDao.save(question);
        }else{
            question.setGmt_modified(System.currentTimeMillis());
                iQuestionDao.update(question);
        }
    }

    public void incView(Long id) {
        Question question = iQuestionDao.finById(id);
        if (question==null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }else {
            iQuestionDao.setViewCount(question.getId(),question.getView_count());
            Question questionGetViewCont = iQuestionDao.finById(id);
            questionGetViewCont.getView_count();
        }


    }

    public Question findByPrimaryKey(Long parent_id) {
        Question question = iQuestionDao.findByPrimaryKey(parent_id);
        if (question==null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        return question;
    }

    public void setComment_count(Long id, Long comment_count,Long jdbcType) {
        iQuestionDao.setComment_count(id,comment_count,jdbcType);
    }

    public List<CommentDTO> findByQuestionId(Long id,Integer type) {
        List<Comment> comments = iCommentDao.findByQuestionId(id,type);

        if (comments.size()==0){
            return new ArrayList<>();
        }

        //获取去重评论人
        Set<Long> commentators=comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
       List<Long> userIds=new ArrayList<>();
       userIds.addAll(commentators);

        //获取评论人并转换为Map
        List<User> users=new ArrayList<>();
        for (Long userId : userIds) {
            User user = iUserDao.findById(userId);
            users.add(user);
        }
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        //转换comment为commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOS;
    }



    public List<QuestionDTO> selectRelated(QuestionDTO questionDTO) {
        if (StringUtils.isBlank(questionDTO.getTag())){
            return new ArrayList<>();
        }
        String[] tags = StringUtils.split(questionDTO.getTag(), ',');
        String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        List<QuestionDTO> relatedQuestionDTO = iQuestionDao.selectRelated(questionDTO.getId(), regexpTag);

        return relatedQuestionDTO;
    }
}
