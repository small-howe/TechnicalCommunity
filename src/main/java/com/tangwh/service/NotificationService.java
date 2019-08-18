package com.tangwh.service;

import com.tangwh.dto.NotificationDTO;
import com.tangwh.dto.PageinationDTO;
import com.tangwh.entity.Notification;
import com.tangwh.entity.NotificationExample;
import com.tangwh.entity.User;
import com.tangwh.enums.NotificationEnum;
import com.tangwh.enums.NotificationStatusEnum;
import com.tangwh.exception.CustomizeErrorCode;
import com.tangwh.exception.CustomizeException;
import com.tangwh.mapper.NotificationMapper;
import com.tangwh.mapper.UserMapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private UserMapper userMapper;

    public PageinationDTO list(Integer userId, Integer page, Integer size) {
        /**  0  5   第一页   5*(i-1)
         *   5  5   第二页    5*()
         *  10  5   第三页
         * */
        PageinationDTO<NotificationDTO> pageinationDTO = new PageinationDTO<>();
        // 查询数据库总数
        Integer totalPage;

        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andRecevierEqualTo((long) userId);
        Integer count = (int) notificationMapper.countByExample(notificationExample);

        if (count % size == 0) {
            // 显示要展示多少页码  count 是数据总条数
            totalPage = count / size;
        } else {
            totalPage = (count / size) + 1;
        }
        if (page < 1) {
            page = 1;
        }
        pageinationDTO.setPagination(totalPage, page);
        if (page > totalPage) {
            page = totalPage;
        }
        // offset =size*(page-1) (imlit offfset,5)
        Integer offset = size * (page - 1);

        NotificationExample example = new NotificationExample();
        example.createCriteria()
                .andRecevierEqualTo((long) userId);
        example.setOrderByClause("gmt_create DESC");
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));
        if (notifications.size() == 0) {
            return pageinationDTO;
        }
        // java8新语法
        Set<Long> disUserIds = notifications.stream().map(notifiy -> notifiy.getNotifier()).collect(Collectors.toSet());

        List<Long> usedIdsa = new ArrayList<>(disUserIds);
        // 转换操作
        List<Integer> usedIds = new ArrayList<>();
        for (Long usedId : usedIdsa) {
            int intValue = usedId.intValue();
            usedIds.add(intValue);
        }


        List<NotificationDTO> NotificationDTOS = new ArrayList<>();
        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();

            BeanUtils.copyProperties(notification, notificationDTO);
            int type = notification.getType();
            notificationDTO.setTypeName(NotificationEnum.nameOfType(notification.getType()));
            NotificationDTOS.add(notificationDTO);
        }


//        不用写
//        UserExample userExample = new UserExample();
//        userExample.createCriteria()
//                .andIdIn(usedIds);
//        List<User> users = userMapper.selectByExample(userExample);
//        Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(u -> u.getId(), u -> u));


        pageinationDTO.setData(NotificationDTOS);
        return pageinationDTO;
    }

    /**
     * 未读取通知的个数
     *
     * @param usedId
     * @return
     */
    public Long unreadCount(Integer usedId) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andRecevierEqualTo((long) usedId)
        .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(notificationExample);

    }


    /**
     * 读取 状态
     *
     * @param id
     * @param user
     * @return
     */
    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if (notification == null) {
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if (notification.getRecevier() != user.getId().longValue()) {
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }
notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);

        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
        int type = notification.getType();
        notificationDTO.setTypeName(NotificationEnum.nameOfType(notification.getType()));

        return notificationDTO;
    }
}



