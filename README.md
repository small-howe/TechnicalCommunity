
## 项目部署
### 依赖
1. git 需要把项目拉下来
2. jdk  编译
3. maven 构建项目
4. mysql
## 步骤
####1.更新源 安装git 创建文件 克隆项目  进入项目下载maven(查看版本) 编译打包
 - yum update
 - yum install git
 - mkdir App
 - cd App/ 
 - git clone https://github.com/small-howe/TechnicalCommunity.git
 - yum install maven
 - maven -v
 - mvn compile package
 - java -jar -Dspring.profiles.active=production target/communtiy-0.0.1-SNAPSHOT.jar
 - ps -aux | grep java// 查看进程 
 

 




## 资料
[Spring 文档](https://spring.io/guides)    
[Spring Web](https://spring.io/guides/gs/serving-web-content/)   
[es](https://elasticsearch.cn/explore)    
[Github deploy key](https://developer.github.com/v3/guides/managing-deploy-keys/#deploy-keys)    
[Bootstrap](https://v3.bootcss.com/getting-started/)    
[Github OAuth](https://developer.github.com/apps/building-oauth-apps/creating-an-oauth-app/)    
[Spring](https://docs.spring.io/spring-boot/docs/2.0.0.RC1/reference/htmlsingle/#boot-features-embedded-database-support)    
[菜鸟教程](https://www.runoob.com/mysql/mysql-insert-query.html)    
[Thymeleaf](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#setting-attribute-values)    
[Spring Dev Tool](https://docs.spring.io/spring-boot/docs/2.0.0.RC1/reference/htmlsingle/#using-boot-devtools)  
[Spring MVC](https://docs.spring.io/spring/docs/5.0.3.RELEASE/spring-framework-reference/web.html#mvc-handlermapping-interceptor)  
[Markdown 插件](http://editor.md.ipandao.com/)   
[ucloud SDK] (https://console.ucloud.cn/ufile/ufile/detail?id=small-howe)
[UFfile SDK](https://github.com/ucloud/ufile-sdk-java)  
[Count(*) VS Count(1)](https://mp.weixin.qq.com/s/Rwpke4BHu7Fz7KOpE2d3Lw)  

## 工具
[Git](https://git-scm.com/download)   
[Visual Paradigm](https://www.visual-paradigm.com)    
[Flyway](https://flywaydb.org/getstarted/firststeps/maven)  
[Lombok](https://www.projectlombok.org)    
[ctotree](https://www.octotree.io/)   
[Table of content sidebar](https://chrome.google.com/webstore/detail/table-of-contents-sidebar/ohohkfheangmbedkgechjkmbepeikkej)    
[One Tab](https://chrome.google.com/webstore/detail/chphlpgkkbolifaimnlloiipkdnihall)    
[Live Reload](https://chrome.google.com/webstore/detail/livereload/jnihajbhpnppcggbcgedagnkighmdlei/related)  
[Postman](https://chrome.google.com/webstore/detail/coohjcphdfgbiolnekdpbcijmhambjff)

## 脚本语法
```sql
CREATE TABLE USER
(
    ID int AUTO_INCREMENT PRIMARY KEY NOT NULL,
    ACCOUNT_ID VARCHAR(100),
    NAME VARCHAR(50),
    TOKEN VARCHAR(36),
    GMT_CREATE BIGINT,
    GMT_MODIFIED BIGINT
);
```
```bash
mvn flyway:migrate
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
```

