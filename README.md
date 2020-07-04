## 今日校园自动打卡(仅支持天院学子)
### 说明

- 部署方式一 (推荐,免费白嫖)
    * 1.注册腾讯云
    * 2.创建云函数链接 https://console.cloud.tencent.com/scf
    * 3.点击左侧【函数服务】-->【新建】-->【函数名称随便起】--> 【运行环境选择python3.6】-->【创建方式选择模板函数，再选择下方的helloword】-->【下一步】-->【复制下面python代码并修改学号密码邮箱】--> 【点击高级设置，把超时时间设置10s】--> 【点击完成】--> 【点击左侧触发管理】--> 【点击创建触发器】 --> 【定时任务名称随便起】-->【触发周期选择自定义触发周期】 --> 【Cron表达式填写 0 0 6 * * * *】
         ```python
       # -*- coding: utf8 -*-
       import requests
       import json
       def main_handler(event, context):
           #==================必填项========================
           # 学号
           username = '请输入学号'
           # 密码
           password = '请输入密码'
           # 个人邮箱
           email = '请输入邮箱@qq.com'
           #===============================================
       
           header = {"Content-Type":"application/json"}
           url = "https://v2.xiaoyang666.top/api/autoSubmit"
           params = {
               'username': username,
               'password': password,
               'email': email
           }
           res = requests.post(url, headers=header, data = json.dumps(params))
           print(res.text)
         ```
   
- 部署方式二 (需要服务器)
    * 1.进入项目根目录
    * 2.配置application.yml中【学号】、【密码】、【邮箱】(用于接收打卡情况通知)
    * 3.自定义打卡时间， 在task包中的AutoSubmitTask修改cron表达式。(默认每天早上6点自动打卡)
    * 4.mvn install
    * 5.复制target包中的CyDailyAutoSubmit.jar上传到服务器
    * 6.nohup java -jar CyDailyAutoSubmit.jar &  ##启动项目
    
- 项目基于 https://github.com/ZimoLoveShuang/auto-submit 开发优化

- 仅供学习交流,感谢网上相关资源