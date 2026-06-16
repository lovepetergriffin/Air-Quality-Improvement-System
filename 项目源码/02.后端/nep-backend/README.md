# NEP环保公众监督系统后端

        本目录按照 `pojo -> mapper -> service -> controller` 分层组织，覆盖登录、反馈上报与派发、网格员任务、AQI查询、统计保存、反馈状态更新、监督员注册登录、省份城市列表、首页看板等模块。

        ## 运行

        1. 在 MySQL 中执行 `03.项目源码/03.数据库/nep.sql`。
        2. 按本机 MySQL 账号修改 `src/main/resources/application.yml`。
        3. 执行 `mvn spring-boot:run`。
        4. 接口文档入口：`http://localhost:8080/doc.html` 或 Swagger UI。
