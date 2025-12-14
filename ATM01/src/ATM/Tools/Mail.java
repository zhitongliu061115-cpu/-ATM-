package ATM.Tools;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


public class Mail {

    public static String USERNAME = "2546696700@qq.com"; //	邮箱发送账号
    public static String PASSWORD = "yuloueajuxrudiih"; //邮箱平台的授权码
    public static String HOST = "smtp.qq.com"; // SMTP服务器地址
    public static String PORT = "465"; //SMTP服务器端口
    public static Session session = null;


    public static void sendEmail(String toEmail, String subject, String content) throws MessagingException {
        Properties props = new Properties();
        // 必填：SMTP 服务器
        props.put("mail.smtp.host", HOST);
        // 需要身份验证
        props.put("mail.smtp.auth", "true");
        // 使用 SSL 安全传输
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.port", String.valueOf(PORT));

        // 创建 Session ，自动使用 props 中的 SMTP 和 SSL 配置
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // QQ 邮箱用户名：完整邮箱地址
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });


//邮件内容
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(USERNAME));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
        message.setSubject(subject);
        message.setText(content);

        // 发送
        Transport.send(message);
        System.out.println("邮件已发送到：" + toEmail);
    }

    }
