package com.majiang.community.service;

import com.majiang.community.enums.CommentTypeEnum;
import com.majiang.community.enums.NotificationStatusEnum;
import com.majiang.community.enums.NotificationTypeEnum;
import com.majiang.community.exception.CustomizeErrorCode;
import com.majiang.community.exception.CustomizeException;
import com.majiang.community.mapper.ICommentDao;
import com.majiang.community.model.Comment;
import com.majiang.community.model.Notification;
import com.majiang.community.model.Question;
import com.majiang.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CommentServer {

    @Autowired
    private ICommentDao iCommentDao;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private NotificationServer notificationServer;

    @Transactional
    public void save(Comment comment, User commentator) {
        if (comment.getParent_id()==null || comment.getParent_id()==0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType()==null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if (comment.getType()==CommentTypeEnum.COMMENT.getType()){
            //回复评论
            Comment dbComment=iCommentDao.findByPrimaryKey(comment.getParent_id());
            if (dbComment==null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            //查询问题
            Question question = questionService.findByPrimaryKey(dbComment.getParent_id());
            iCommentDao.save(comment);
            //初始化评论数
            if (comment.getComment_count()==null){
                Integer jdbcInitialize=0;
                iCommentDao.initializeComment_count(comment.getId(),jdbcInitialize);
            }
            Integer jdbcType=1;
            iCommentDao.setComment_count(dbComment.getId(),dbComment.getComment_count(),jdbcType);
            //创建通知
            createNotification(comment, dbComment.getCommentator(),commentator.getAccount_name(),
                    question.getTitle(),NotificationTypeEnum.REPLY_COMMENT,question.getId());
        }else {
            //回复问题
            Question question = questionService.findByPrimaryKey(comment.getParent_id());
            iCommentDao.save(comment);
            Long jdbcType=1L;
            questionService.setComment_count(question.getId(),question.getComment_count(),jdbcType);
            //创建通知
            createNotification(comment,question.getCreator(),commentator.getAccount_name(),
                    question.getTitle(),NotificationTypeEnum.REPLY_QUESTION,question.getId());
        }
    }

    private void createNotification(Comment comment, Long receiver,String notifierName,String outTitle,NotificationTypeEnum notificationTypeEnum,Long outer_id) {
        if (receiver==comment.getCommentator()){
            return;
        }
        Notification notification = new Notification();
        notification.setMgt_create(System.currentTimeMillis());
        notification.setOuter_id(outer_id);
        notification.setNotifier(comment.getCommentator());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setType(notificationTypeEnum.getType());
        notification.setReceiver(receiver);
        notification.setNotifier_name(notifierName);
        notification.setOuter_title(outTitle);
        notificationServer.save(notification);
    }


}
