package com.tangwh.mapper;

import com.tangwh.entity.Comment;
import org.apache.ibatis.annotations.Update;

public interface CommentExtMapper {

 //  @Update("UPDATE `comment` SET `comment_count`=#{commentCount} WHERE `id` =#{id}")
    int inCommentCount(Comment comment);//评论数
}