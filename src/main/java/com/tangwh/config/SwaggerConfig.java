package com.tangwh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

/**
 * Swagger 配置
 * @Configuration 加入至配置
 */
@Configuration
@EnableSwagger2 //开启Swagger2
public class SwaggerConfig {
  // 组1
    @Bean
    public Docket docket1(){
        return new Docket(DocumentationType.SWAGGER_2).groupName("A");
    }
    // 组2
    @Bean
    public Docket docket2(){
        return new Docket(DocumentationType.SWAGGER_2).groupName("B");
    }
    // 组3
    @Bean
    public Docket docket3(){
        return new Docket(DocumentationType.SWAGGER_2).groupName("C");
    }




    // 配置了Swagger的Bean实例
    @Bean
    public Docket docket(Environment environment){

       // 问题:我只希望我的Swagger在生产环境使用,在发布的时候不使用
        // 设置要显示的Swagger的 环境
        Profiles profiles = Profiles.of("dev");
        //1.获取项目环境 通过environment.acceptsProfiles 判断是否处在自己设定的环境当中
        boolean flag = environment.acceptsProfiles(profiles);


        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("唐维豪") // 分组
                //enable(false)是否启动Swagger  默认(true)
                .enable(flag)
                .select()
                // RequestHandlerSelectors配置要扫描的接口方式
                //basePackage :指定要扫描的包
                //any():扫描全部
                //none():都不扫描
                //withMethodAnnotation():扫描方法上的注解
                //withClassAnnotation()):扫描类上的注解 参数:是一个注解的反射对象
                .apis(RequestHandlerSelectors.basePackage("com.tangwh.controller"))
                // paths():过滤什么路径
            //  .paths(PathSelectors.ant("/tangwh/**"))
                .build();
    }


    //配置Wagger 信息 = apiInfo();

    public ApiInfo apiInfo(){
        Contact contact = new Contact("唐维豪", "urn:tos", "2678691035@qq.com");
        return new ApiInfo(
                "Howe Swagger API 文档",  //标题  (重点写)
                "再小的帆也能远航", // 描述 (重点写)
                "1.0",         // 版本
                "urn:tos",
                contact,              // 作者信息
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList());
    }
}
