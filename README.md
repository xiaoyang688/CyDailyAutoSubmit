## 今日校园自动打卡(仅支持天院学子)
### 说明

- 克隆项目

- 配置application.yml中【学号】、【密码】、【邮箱】(用于接收打卡情况通知)

- 自定义打卡时间， 在task包中的AutoSubmitTask修改cron表达式。(默认每天早上6点自动打卡)

- 部署安装
    * 1.进入项目根目录
    * 2.mvn install
    * 3.复制target包中的wisedu-unified-login-api-v1.0.jar上传到服务器
    * 4.nohup java -jar wisedu-unified-login-api-v1.0.jar &  ##启动项目
    
- 项目基于 https://github.com/ZimoLoveShuang/auto-submit 开发优化

- 代码未经整理,仅供学习交流,感谢网上相关资源