package com.tangwh.mapper;

import com.tangwh.dto.QuestionQueryDTO;
import com.tangwh.entity.Question;
import com.tangwh.entity.QuestionExample;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface QuestionExtMapper {
    int inCview(Question record);// 阅读数
    //  @Update("UPDATE `question` SET `comment_count` = #{commentCount} WHERE `id` = #{id}")
    int inCommentCount(Question record);//评论数
  //  @Select("select * from QUESTION where id !=#{id} and tag REGEXP #{tag}")
  List<Question> selectRelated(Question question);

    Integer countBySearch(QuestionQueryDTO questionQueryDTO);

    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);
}