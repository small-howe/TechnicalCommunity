package com.tangwh.service;

import com.tangwh.dto.PageinationDTO;
import com.tangwh.dto.QuesstionDTO;
import com.tangwh.dto.QuestionQueryDTO;
import com.tangwh.entity.Question;
import com.tangwh.entity.QuestionExample;
import com.tangwh.entity.User;
import com.tangwh.exception.CustomizeErrorCode;
import com.tangwh.exception.CustomizeException;
import com.tangwh.mapper.QuestionExtMapper;
import com.tangwh.mapper.QuestionMapper;
import com.tangwh.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuesstionServcie {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    QuestionExtMapper questionExtMapper;
    // 两张表相结合
    public PageinationDTO getqusetionList(String search,Integer page, Integer size) {


        // 阿帕奇 下面的判断是否为空
        if(StringUtils.isNotBlank(search)){
            // 先把tag 用逗号隔开
            String[] tags = StringUtils.split(search, " ");

            search= Arrays.stream(tags).collect(Collectors.joining("|"));
        }





        /**  0  5   第一页   5*(i-1)
         *   5  5   第二页    5*()
         *  10  5   第三页
         * */
        PageinationDTO pageinationDTO = new PageinationDTO();
        // 查询数据库总数

        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();

        questionQueryDTO.setSearch(search);
        Integer count = questionExtMapper.countBySearch(questionQueryDTO);
        Integer totalPage;
        if (count % size == 0) {
            // 显示要展示多少页码  count 是数据总条数
            totalPage = count / size;
        } else {
            totalPage = (count / size) + 1;
        }
        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        pageinationDTO.setPagination(totalPage, page);
        // offset =size*(page-1) (imlit offfset,5)
        Integer offset = size * (page - 1);
        questionQueryDTO.setSize(size);
        questionQueryDTO.setPage(offset);

        List<Question> questionsList = questionExtMapper.selectBySearch(questionQueryDTO);

        List<QuesstionDTO> quesstionDTOList = new ArrayList<>();
        for (Question question : questionsList) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            // 最古老的方式
            //   question.setId(user.getId());
            //SpringBoot 提供最新方式
            // 把question 对象拷贝给quesstionDto
            QuesstionDTO quesstionDTO = new QuesstionDTO();
            BeanUtils.copyProperties(question, quesstionDTO);
            quesstionDTO.setUser(user);
            quesstionDTOList.add(quesstionDTO);
        }
        pageinationDTO.setData(quesstionDTOList);
        return pageinationDTO;
    }

    // 根据用户ID查询 自己所提出来的问题
    public PageinationDTO getqusetionList(Integer userId, Integer page, Integer size) {

            /**  0  5   第一页   5*(i-1)
             *   5  5   第二页    5*()
             *  10  5   第三页
             * */
            PageinationDTO pageinationDTO = new PageinationDTO();
            // 查询数据库总数
            Integer totalPage;
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria()
                    .andCreatorEqualTo(userId);
            Integer count = (int)questionMapper.countByExample(questionExample);

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

            List<Question> questions = questionMapper.ListById(userId,offset, size);
            List<QuesstionDTO> quesstionDTOList = new ArrayList<>();
            for (Question question : questions) {
                User user = userMapper.selectByPrimaryKey(question.getCreator());
                // 最古老的方式
                //   question.setId(user.getId());
                //SpringBoot 提供最新方式
                // 把question 对象拷贝给quesstionDto
                QuesstionDTO quesstionDTO = new QuesstionDTO();
                BeanUtils.copyProperties(question, quesstionDTO);
                quesstionDTO.setUser(user);
                quesstionDTOList.add(quesstionDTO);
            }
            pageinationDTO.setData(quesstionDTOList);
            return pageinationDTO;
        }


    /**
     *根据ID查询 用户所有信息
     * @param id
     * @return
     */
    public QuesstionDTO ById(Integer id) {

      Question question =  questionMapper.getById(id);
        if(question==null){
            throw  new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuesstionDTO quesstionDTO = new QuesstionDTO();
        BeanUtils.copyProperties(question, quesstionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        quesstionDTO.setUser(user);
        return quesstionDTO;
    }

    // 更新问题  和发布一起 判断 发布还是更新
    public void createOrUpdate(Question quesstion) {
        if(quesstion.getId()==null){
             //创建
            // 创建时间
            quesstion.setCreate(System.currentTimeMillis());
            // 更新的时间
            quesstion.setGmtModified(quesstion.getCreate());

            questionMapper.save(quesstion);
        }else {

            // 修改时间
            quesstion.setGmtModified(System.currentTimeMillis());
            // 更新

            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(quesstion.getTitle());
            updateQuestion.setDescription(quesstion.getDescription());
            updateQuestion.setTag(quesstion.getTag());


            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo(quesstion.getId());
            int update = questionMapper.updateByExampleSelective(updateQuestion, example);
            if(update!=1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    /**
     * 累加阅读数
     * @param id
     */
    public void incView(Integer id) {

        Question question = new Question();
        question.setId(id);
        question.setVieweCount(1);
        questionExtMapper.inCview(question);
    }


    public List<QuesstionDTO> selectRelated(QuesstionDTO queryDTO) {
        // 阿帕奇 下面的判断是否为空
        if(StringUtils.isBlank(queryDTO.getTag())){

            return new ArrayList<>();
        }

        // 先把tag 用逗号隔开
        String[] tags = StringUtils.split(queryDTO.getTag(), ",");
        String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(queryDTO.getId());
        question.setTag(regexpTag);

        List<Question> questions = questionExtMapper.selectRelated(question);
        // questions转化成QuesstionDTO
        List<QuesstionDTO> quesstionDTOS = questions.stream().map(q -> {
            QuesstionDTO quesstionDTO = new QuesstionDTO();
            BeanUtils.copyProperties(q, quesstionDTO);
            return quesstionDTO;
        }).collect(Collectors.toList());
        return quesstionDTOS;
    }
}
