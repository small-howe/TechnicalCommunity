package com.tangwh.service;

import com.tangwh.dto.CommentDTO;
import com.tangwh.entity.*;
import com.tangwh.enums.CommentTypeEnum;
import com.tangwh.enums.NotificationEnum;
import com.tangwh.enums.NotificationStatusEnum;
import com.tangwh.exception.CustomizeErrorCode;
import com.tangwh.exception.CustomizeException;
import com.tangwh.mapper.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentExtMapper commentExtMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Transient
    public void insert(Comment comment, User commentator) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            //回复评论
            Comment dbcomment = commentMapper.selectByPrimaryKey(comment.getParentId());

            if(dbcomment==null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
           int dbnum = dbcomment.getParentId().intValue();
            // 查找问题是否存在  存在 拿到title
            Question question = questionMapper.getById(dbcomment.getParentId().intValue());
            if(question==null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }



            commentMapper.insert(comment);

            // 增加回复评论数
            Comment praentComment = new Comment();
            praentComment.setCommentCount(1);
            praentComment.setId(comment.getParentId());
             commentExtMapper.inCommentCount(praentComment);

            // 创建通知
            createNotify(comment,dbcomment.getCommentator(), commentator.getName(), question.getTitle(), NotificationEnum.REPLY_COMMENT,(long)question.getId());

        } else {
            // 回复问题  Long 转换成Int
            Long parentId = comment.getParentId();
            Integer intValue = parentId.intValue();
            Question question = questionMapper.getById(intValue);
        //  commentMapper.selectByPrimaryKey(comment.getParentId());
            if(question==null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
           // commentMapper.insert(comment);
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.inCommentCount(question);
            // 创建通知
            createNotify(comment,question.getCreator(),commentator.getName(),question.getTitle(),
                    NotificationEnum.REPLY_QUESTION, (long)question.getId());

        }
    }

    private void createNotify(Comment comment, Integer recevier, String notifierName, String outTitle,
                              NotificationEnum notificationType, Long outerid) {

        // 自己回复自己问题不需要不通知
        if(recevier == comment.getCommentator()){
            return;

        }
        // 回复通知问题
        Notification notification = new Notification();
        //回复时间
        notification.setGmtCreate(System.currentTimeMillis());
        //用type 来区分 通知类型
        notification.setType(notificationType.getType());
        //通知的类型，评论，点赞之类的  回复的id
        notification.setOuterid(outerid);
        //通知者  (评论人)
        notification.setNotifier((long)comment.getCommentator());
        //收通知的人
        Long recevicers = recevier.longValue();
        notification.setRecevier(recevicers);
        // 通知状态(已读和未读)
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        //发起通知人的姓名
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outTitle);
        notificationMapper.insert(notification);
    }



    public List<CommentDTO> listByTargetId(Integer id, CommentTypeEnum type) {
        CommentExample commentExample = new CommentExample();
//        Integer 转化成Long
        long idValue = id.longValue();
        commentExample.createCriteria()
                .andParentIdEqualTo(idValue)
        .andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments;
        comments = commentMapper.selectByExample(commentExample);
        if (comments.size()==0){
            return new ArrayList<>();
        }else {
//java8 新语法  通过map 遍历 返回结果集，map(当前对象 -> 希望返回什么对象) . 算结果集  获取所有评论的人comentators
////            获取去重读的评论人
            Set<Integer> comentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
            List<Integer> userIds =new ArrayList<>();
            userIds.addAll(comentators);

            // 获取评论人并转换为 Map
            UserExample userExample = new UserExample();
            userExample.createCriteria()
                    .andIdIn(userIds);
            List<User> users = userMapper.selectByExample(userExample);
            Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

// 转换 comment 为 commentDTO
            List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
                CommentDTO commentDTO = new CommentDTO();
                BeanUtils.copyProperties(comment, commentDTO);
                commentDTO.setUser(userMap.get(comment.getCommentator()));
                return commentDTO;
            }).collect(Collectors.toList());

            return commentDTOS;

        }

    }
}
