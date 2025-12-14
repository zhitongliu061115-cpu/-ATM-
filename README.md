# 百川银行 ATM 系统（Java Socket + Swing）

---

## 项目简介

本项目是一个基于 **Java** 实现的 ATM 模拟系统，采用 **C/S 架构**：客户端提供 ATM 图形化操作界面，服务器端负责业务处理与管理员管理后台。客户端与服务器通过 **Socket** 通信，账户数据以文本文件 `data.txt` 持久化存储；服务器端使用 **线程池** 支持多客户端并发请求。:contentReference[oaicite:1]{index=1}

---

## 功能特性

### 客户端（ATM 用户端）
- 登录：账号/密码 + **图形验证码**
- 注册：账号/密码/邮箱 + **邮箱验证码**
- 业务功能：
  - 查询余额
  - 存款（可生成 **PDF 回单**）
  - 取款（可生成 **PDF 回单**）
  - 转账（生成 **二维码凭证**）
  - 修改密码（邮件通知）
- 体验增强：
  - 背景音乐、按钮点击音效
  - 文字转语音（个性化提示语）:contentReference[oaicite:2]{index=2}

### 服务器端（管理后台）
- 连接管理：在线客户端列表、连接/断开日志
- 账户管理：查询/刷新/添加账户、修改余额、重置密码（邮件通知）
- 控制台日志：系统运行日志、操作日志、异常日志、日志清空
- 数据统计可视化（JFreeChart）：
  - 总账户数/总资产/平均余额
  - 余额分布、占比等图表 :contentReference[oaicite:3]{index=3}

---

## 技术栈

- GUI：Java Swing / AWT
- 网络：Java Socket
- 存储：Java IO（`data.txt`）
- 并发：线程池（ExecutorService）
- 可视化：JFreeChart
- PDF：Apache PDFBox
- 二维码：Google ZXing
- 邮件：JavaMail（`javax.mail` 1.6.2 + activation 1.1.1）
- 语音：Jacob + Windows SAPI
- 日志：自定义 Logger :contentReference[oaicite:4]{index=4}

---

## 系统架构

- **Client**：只负责界面与交互，将操作封装为文本命令发送给服务器（如 `Login 账号 密码`），等待服务器返回结果。:contentReference[oaicite:5]{index=5}  
- **Server**：`ServerSocket` 监听端口，接收连接后由线程池分发 `clientHandler` 处理请求；核心业务逻辑集中在 `Service` 中，读写 `data.txt` 并保证线程安全（关键方法 `synchronized`）。:contentReference[oaicite:6]{index=6}

---

