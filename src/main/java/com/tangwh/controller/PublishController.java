package com.tangwh.controller;

import com.tangwh.cache.TagCache;
import com.tangwh.dto.QuesstionDTO;
import com.tangwh.entity.Question;
import com.tangwh.entity.User;
import com.tangwh.service.QuesstionServcie;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

// alt + fn+f12 get set 方法（全选属性  shift+向下）  和 创建类
//导包   alt+ 回车  取掉没用的代码
//提取变量  ctrl  + alt +v
// ctrl +e  切换临时的目录
//shift +f12 当前页面最大化
// 双击 shift  查找页面并进入
// ctrl +alt +L 格式化代码
//shift + Enter  换行
// atrl +alt +o  去掉没用的包
@Controller
public class PublishController {


    @Autowired
    private QuesstionServcie quesstionServcie;

    /** 根据用户id 不同 进入对自己问题的编辑页面也不同
     * /publish/{id}
     * @return
     */
    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Integer id,
                       Model model){
        //回显页面
        QuesstionDTO question = quesstionServcie.ById(id);
        model.addAttribute("title", question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag", question.getTag());

        model.addAttribute("id", question.getId());
        model.addAttribute("tags", TagCache.get());

        return "publish";
    }

    @GetMapping("/publish")
    public String publish(Model model) {
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    @PostMapping("/publish")
    //接受从前台 的值@RequestParam("name")
    public String dopulish(@RequestParam( value = "title" ,required = false) String title,
                           @RequestParam(value = "description",required = false) String description,
                           @RequestParam( value = "tag",required = false) String tag,
                           @RequestParam(value = "id",required = false)Integer id,
                           HttpServletRequest request,
                           Model model) {
       // 回显 在页面
        model.addAttribute("title", title);
        model.addAttribute("description",description);
        model.addAttribute("tag", tag);
        model.addAttribute("tags", TagCache.get());

        if (title == null || title.isEmpty()){
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }if (description == null || description.isEmpty()){
            model.addAttribute("error", "内容不能为空");
            return "publish";
        }if (tag == null || tag.isEmpty()){
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }
        String invalid = TagCache.filterInvalid(tag);
        if(StringUtils.isNoneBlank(invalid)){
            model.addAttribute("error", "输入非法标签"+invalid);
            return "publish";
        }


        User user = (User) request.getSession().getAttribute("user");

        // 未获得用户信息 报错
        if (user == null) {
            model.addAttribute("error", "用户未登录");
            return "publish";
        }
            // 把值 存入数据库中
            Question quesstion = new Question();
            // 标题
            quesstion.setTitle(title);
            // 内容
            quesstion.setDescription(description);
            // 标签
            quesstion.setTag(tag);
            // 用户id
            quesstion.setCreator(user.getId());
            //通过页面  hidden 传送过来的id
            quesstion.setId(id);
            // 判断有没有id 有进行更新  没有进行创建
            quesstionServcie.createOrUpdate(quesstion);



        return "redirect:/";
    }
}
