🏦 百川银行 ATM 系统 (Baichuan Bank ATM System)

基于 Java Swing 与 Socket 通信的 C/S 架构银行自动取款机模拟系统。

📖 项目简介

本项目主要目标是模拟真实 ATM 自动取款机的操作流程，实现用户与银行系统之间的交互。系统采用 C/S (客户端/服务器) 架构，双方通过 Socket 进行通信。

服务器端：使用多线程处理并发请求，管理账户数据（文件存储），并提供可视化的监控和管理界面。

客户端：提供友好的图形化用户界面，支持存款、取款、转账等核心业务，并集成了语音交互、多媒体反馈等创新功能。

✨ 核心功能
🖥️ 客户端 (Client)

🔐 安全登录/注册：支持账号密码登录（带图形验证码），新用户注册需通过 邮箱验证码 验证身份。

💰 核心业务：

余额查询：实时查看账户余额。

存款/取款：支持现金存取，并在交易成功后生成 PDF 电子凭条。

转账：支持向他人转账，交易成功后自动生成 交易详情二维码 供保存。

🛡️ 安全中心：支持修改密码，操作成功后发送 邮件通知。

🔊 交互体验：

语音播报：集成文字转语音 (TTS)，在登录、交易成功时进行个性化语音提示。

音效反馈：背景音乐 (BGM) 及按钮点击音效。

⚙️ 服务器端 (Server)

📡 连接管理：实时监控客户端连接列表（IP、账号、状态），支持多线程并发处理（线程池）。

👥 账户管理：管理员可进行账户查询、添加、余额修改、密码重置等操作。

📝 日志系统：全方位的日志记录（系统运行日志、客户端操作日志、异常信息）。

📊 数据可视化：基于 JFreeChart 展示银行资金分布、账户余额区间占比等统计图表。

🛠️ 技术栈
类别	技术/库	说明
编程语言	Java SE	核心逻辑实现
GUI 框架	Java Swing	界面设计 (CardLayout, JTabbedPane)
网络通信	Java Socket	C/S 架构通信，自定义文本协议
并发处理	ExecutorService	线程池管理客户端连接
数据存储	IO Stream	本地文件存储 (data.txt)
报表图表	JFreeChart	服务器端数据可视化
PDF 生成	Apache PDFBox	生成交易凭条
二维码	ZXing	生成转账凭证二维码
邮件服务	JavaMail	发送验证码及通知邮件
语音合成	Jacob (参考)	文字转语音 (TTS)
📂 项目结构
ATM-System/
├── src/
│   ├── ATM/
│   │   ├── Client/          # 客户端核心代码
│   │   │   ├── ATMClient.java   # 客户端入口
│   │   │   ├── ATMService.java  # 网络通信层
│   │   │   └── ClientUI/        # 界面实现 (MainFrame, Panels)
│   │   ├── Server/          # 服务器端核心代码
│   │   │   ├── ATMServer.java   # 服务器入口
│   │   │   ├── service.java     # 业务逻辑层
│   │   │   ├── clientHandler.java # 客户端请求处理线程
│   │   │   └── ServerUI/        # 服务器管理界面
│   │   ├── Account.java     # 账户实体类
│   │   └── Tools/           # 工具类包
│   │       ├── Mail.java        # 邮件发送
│   │       ├── QRCode.java      # 二维码生成
│   │       ├── Logger.java      # 日志记录
│   │       └── ...
├── data.txt                 # 账户数据存储文件
├── lib/                     # 依赖的 jar 包 (PDFBox, JFreeChart, ZXing 等)
└── README.md

🚀 快速开始
环境要求

JDK 1.8 或更高版本

IntelliJ IDEA / Eclipse

运行步骤

克隆仓库

git clone https://github.com/YourUsername/Baichuan-ATM-System.git


导入依赖
请确保 lib 目录下的所有 .jar 文件已添加到项目的 Classpath 中。主要依赖包括：

jfreechart.jar & jcommon.jar

pdfbox.jar & fontbox.jar

core.jar & javase.jar (ZXing)

javax.mail.jar & activation.jar

jacob.jar (如使用语音功能需配置 DLL)

启动服务器
运行 ATM.Server.ATMServer 类。

服务器启动后将监听指定端口，并加载 data.txt 中的账户数据。

启动客户端
运行 ATM.Client.ATMClient 类。

启动后即可看到登录界面，体验背景音乐与语音提示。

📸 系统截图

(此处建议上传你的结题报告中的截图并替换以下链接)

客户端
登录界面	主菜单

	
取款凭条 (PDF)	转账二维码

	
服务器端
监控面板	数据统计图表

	
💡 创新与难点解决

UI 交互优化：放弃了传统的多窗口跳转，采用 CardLayout 布局管理器，实现了单窗口内平滑的界面切换，解决了频繁创建 JFrame 导致的卡顿和闪烁问题。

邮件兼容性：在开发邮件功能时，遇到 jakarta.mail 版本兼容性问题，通过回退至 javax.mail-1.6.2 并手动管理依赖，成功解决了发送失败的异常。

数据安全：所有关键操作（修改密码、转账）均通过服务器端验证，且数据文件写入采用 synchronized 同步锁，确保高并发下的数据一致性。



免责声明：本项目为课程实训作业，仅用于学习和演示，不涉及真实货币交易。
