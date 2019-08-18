package com.tangwh.service;

import com.tangwh.entity.User;
import com.tangwh.entity.UserExample;
import com.tangwh.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 如果数据库能查到AccountID 说明该用户已经创建 只需要更新Token
     *
     * @param user
     */
    public void createUpdate(User user) {

        UserExample userExample = new UserExample();
        userExample.createCriteria()
            //    .andAccountIdEqualTo(user.getAccount_id());
         .andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);


        if (users.size()==0) {
            // 修改日志   当前时间
          //  user.setGmt_create(System.currentTimeMillis());
           // user.setGmt_modified(user.getGmt_create());
            user.setGmtCreate(System.currentTimeMillis());
           user.setGmtModified(user.getGmtCreate());
            // 如果数据库中不存在accountID 就做插入
            userMapper.insert(user);

        } else {
            User dbuser = users.get(0);
            User updateUser= new User();
            // 如果数据库中存在accountId 就做更新
            // 时间
        //    updateUser.setGmt_modified(System.currentTimeMillis());
            updateUser.setGmtModified(System.currentTimeMillis());
            // 头像更新
          //  updateUser.setAvatar_url(user.getAvatar_url());
            // 更新姓名
            updateUser.setName(user.getName());
            // 更新Token
            updateUser.setToken(user.getToken());

            UserExample example  = new UserExample();
            example.createCriteria()
                    .andIdEqualTo(dbuser.getId());
            userMapper.updateByExampleSelective(updateUser, example);


        }
    }
}
