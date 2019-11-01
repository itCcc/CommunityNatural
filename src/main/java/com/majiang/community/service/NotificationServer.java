package com.majiang.community.service;

import com.majiang.community.dto.NotificationDTO;
import com.majiang.community.dto.PaginationDTO;
import com.majiang.community.dto.QuestionDTO;
import com.majiang.community.enums.NotificationStatusEnum;
import com.majiang.community.enums.NotificationTypeEnum;
import com.majiang.community.exception.CustomizeErrorCode;
import com.majiang.community.exception.CustomizeException;
import com.majiang.community.mapper.INotificationDao;
import com.majiang.community.mapper.IQuestionDao;
import com.majiang.community.mapper.IUserDao;
import com.majiang.community.model.Notification;
import com.majiang.community.model.Question;
import com.majiang.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NotificationServer {

    @Autowired
    private INotificationDao iNotificationDao;

    public void save(Notification notification) {
         iNotificationDao.save(notification);
    }

    public PaginationDTO list(Long userId, Integer page, Integer size) {
        PaginationDTO<NotificationDTO> paginationDTO=new PaginationDTO<>();
        Integer totalPage;
        Integer totalCount = iNotificationDao.countByUserId(userId);
        totalPage=(totalCount%size==0)?totalCount/size:totalCount/size+1;
        if (page<1){
            page=1;
        }
        if (page>totalPage){
            page=totalPage;
        }
        paginationDTO.setPagination(totalPage,page);
        Integer offset=size*(page-1);

        List<Notification> notificationList = iNotificationDao.listByUserId(userId,offset, size);
        if (notificationList.size() == 0) {
            return null;
        }
        ArrayList<NotificationDTO> notificationDTOS = new ArrayList<>();
        for (Notification notification : notificationList) {
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTO.setNotifier_name(notification.getNotifier_name());
            notificationDTO.setNotifier_id(notification.getNotifier());
            notificationDTO.setOuter_title(notification.getOuter_title());
            BeanUtils.copyProperties(notification,notificationDTO);
            notificationDTOS.add(notificationDTO);
        }
        paginationDTO.setData(notificationDTOS);
        return paginationDTO;
    }

    public Long countByStatus(Long userId) {
        Integer jdbcStatus=0;
        Long unreadCount = iNotificationDao.countByStatus(userId,jdbcStatus);
        return unreadCount;
    }

    public NotificationDTO read(Long id, User user) {
        Notification notification= iNotificationDao.selectByPrimary_id(id);
        if (notification==null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_EMPTY);
        }
        if (!Objects.equals(notification.getReceiver(),user.getId())){
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_EMPTY);
        }
        //设置为已读
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        Integer jdbcStatus=1;
        iNotificationDao.setStatus(notification.getId(),jdbcStatus);
        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }
}
