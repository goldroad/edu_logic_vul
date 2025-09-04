# 在线教育系统

## 项目简介

这是一个基于Spring Boot + MySQL开发的在线教育系统，包含学员端和教师/管理员端功能。

## 技术栈

- **后端**: Spring Boot 2.7.14, Spring Data JPA, MySQL 8.0
- **前端**: Thymeleaf, HTML5, CSS3, JavaScript
- **数据库**: MySQL 8.0
- **构建工具**: Maven
- **Java版本**: JDK 8+

## 快速开始

### 1. 环境准备

- JDK 8 或更高版本
- Maven 3.6+
- MySQL 8.0
- Git

### 2. 数据库配置

创建数据库：
```sql
CREATE DATABASE edu_logic_vul CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. 克隆项目

```bash
git clone https://github.com/goldroad/edu_logic_vul.git
cd edu_logic_vul
```

### 4. 配置数据库连接

编辑 `src/main/resources/application.yml`，确认数据库连接信息：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/edu_logic_vul?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root
    password: root
```

### 5. 运行项目

```bash
mvn spring-boot:run
```

或者：
```bash
mvn clean package
java -jar target/edu-logic-vul-1.0.0.jar
```

### 6. 访问系统

打开浏览器访问：http://localhost:8080/edu

## 功能特性

### 用户管理
- 用户注册和登录
- 多角色支持（学生、教师、管理员）
- 个人资料管理
- 密码重置功能

### 课程管理
- 课程创建和发布
- 课程分类和搜索
- 课程详情展示
- 学习进度跟踪

### 订单系统
- 课程购买
- 订单管理
- 支付处理
- 订单历史查询

### 优惠券系统
- 优惠券创建
- 优惠券领取
- 折扣计算
- 使用记录

### 文件管理
- 文件上传下载
- 文件信息查询
- 目录浏览
- 配置文件管理

## 测试账号

| 角色 | 用户名 | 密码 | 说明 |
|------|--------|------|------|
| 管理员 | admin | admin123 | 系统管理员 |
| 教师 | teacher | teacher123 | 课程教师 |
| 学生 | student | student123 | 普通学生 |
| 弱口令用户 | weakuser | 123456 | 弱口令测试 |
| 测试用户 | user1-user5 | 123456 | 批量测试账号 |

## API 接口文档

### 认证接口
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/register` - 用户注册
- `POST /api/auth/forgot-password` - 忘记密码
- `GET /api/auth/captcha` - 获取验证码
- `POST /api/auth/logout` - 用户登出

### 用户接口
- `GET /api/user/{id}` - 获取用户信息
- `GET /api/user/list` - 获取用户列表
- `PUT /api/user/{id}` - 修改用户信息
- `DELETE /api/user/{id}` - 删除用户
- `GET /api/user/system/info` - 获取系统信息

### 课程接口
- `GET /api/course/published` - 获取已发布课程
- `GET /api/course/{id}` - 获取课程详情
- `POST /api/course/create` - 创建课程
- `GET /api/course/my` - 获取我的课程
- `GET /api/course/all` - 获取所有课程

### 订单接口
- `POST /api/order/create` - 创建订单
- `POST /api/order/{orderNo}/pay` - 支付订单
- `GET /api/order/my` - 获取我的订单
- `GET /api/order/{orderNo}` - 获取订单详情
- `GET /api/order/all` - 获取所有订单

### 优惠券接口
- `GET /api/coupon/available` - 获取可用优惠券
- `POST /api/coupon/{id}/receive` - 领取优惠券
- `GET /api/coupon/my` - 获取我的优惠券
- `POST /api/coupon/create` - 创建优惠券

### 文件接口
- `GET /api/file/read` - 读取文件
- `GET /api/file/info` - 获取文件信息
- `GET /api/file/list` - 列出目录内容
- `GET /api/file/config` - 读取配置文件

### 系统接口
- `POST /api/system/reset-data` - 重置数据到初始状态

## 数据恢复功能

系统提供了数据恢复功能，可以将所有数据重置到初始状态：

1. 在登录页面点击"恢复数据到初始状态"按钮
2. 确认操作后，系统将清空所有用户数据、订单、优惠券等信息
3. 重新初始化默认的测试数据

## 项目结构

```
src/
├── main/
│   ├── java/
│   │   └── com/edu/
│   │       ├── config/          # 配置类
│   │       ├── controller/      # 控制器
│   │       ├── entity/          # 实体类
│   │       ├── repository/      # 数据访问层
│   │       └── service/         # 业务逻辑层
│   └── resources/
│       ├── templates/           # 页面模板
│       ├── init-data.sql        # 初始化数据脚本
│       └── application.yml      # 配置文件
```

## 开发指南

### 添加新功能
1. 在相应的包中创建实体类、Repository、Service和Controller
2. 在templates目录下创建对应的页面模板
3. 更新数据库初始化脚本（如需要）

### 数据库变更
1. 修改实体类
2. 更新init-data.sql文件
3. 使用数据恢复功能重置数据

## 注意事项

1. 本系统用于学习和测试目的
2. 请在安全的测试环境中运行
3. 定期备份重要数据
4. 遵守相关法律法规

## 贡献

欢迎提交Issue和Pull Request来改进这个项目。

## 许可证

MIT License

## 联系方式

如有问题，请提交Issue或联系项目维护者。