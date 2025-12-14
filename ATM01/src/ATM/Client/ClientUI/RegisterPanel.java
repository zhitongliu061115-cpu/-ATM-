package ATM.Client.ClientUI;

import ATM.Account;
import ATM.Client.ATMService;
import ATM.Tools.Click_BGM;
import ATM.Tools.Mail;
import ATM.Tools.Speak_Jacob;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class RegisterPanel extends JPanel {
    private ATMService atmService;
    private BufferedImage registerImage;
    private String verify;
    JButton verifyButton;

    private MainFrame mainFrame;

    public RegisterPanel(ATMService atmService, MainFrame mainFrame) {
        this.atmService = atmService;
        this.mainFrame = mainFrame;

        try {
            registerImage = ImageIO.read(getClass().getResourceAsStream("/ATM/resources/login.jpg"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "无法加载背景图片，请检查资源文件！", "错误", JOptionPane.ERROR_MESSAGE);
        }
        showRegisterWindow();
    }


    //创建背景
    public JPanel createBackgroundPanel(BufferedImage backgroundImage) {


        return new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

    }


    private void showRegisterWindow() {
        setLayout(new BorderLayout());


        //贴图背景
        JPanel backgroundPanel = createBackgroundPanel(registerImage);
        backgroundPanel.setLayout(null);

        JPanel registerPanel = new JPanel();
        registerPanel.setOpaque(false);

        registerPanel.setLayout(new BorderLayout());
        registerPanel.setBounds(575, 350, 400, 350);

        JPanel inputPanel = new JPanel();
        inputPanel.setOpaque(false);
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        inputPanel.setBounds(575, 350, 400, 350);

        //输入区域（账号、密码、验证码）
        JPanel inputAreaPanel = new JPanel();
        inputAreaPanel.setOpaque(false);
        inputAreaPanel.setLayout(new BoxLayout(inputAreaPanel, BoxLayout.Y_AXIS));

        //输入账号的部分
        JPanel accountPanel = new JPanel();
        accountPanel.setOpaque(false);
        accountPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel accountLabel = new JLabel("账号： ");
        accountLabel.setFont(new Font("微软雅黑", Font.BOLD, 25));
        accountLabel.setForeground(Color.WHITE);

        JTextField accountField = new JTextField(15);
        accountField.setFont(new Font("微软雅黑", Font.BOLD, 20));

        accountPanel.add(accountLabel);
        accountPanel.add(accountField);

        //输入密码的部分
        JPanel passwordPanel = new JPanel();
        passwordPanel.setOpaque(false);
        passwordPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel passwordLabel = new JLabel("密码： ");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setFont(new Font("微软雅黑", Font.BOLD, 25));

        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("微软雅黑", Font.BOLD, 20));
        passwordField.setEchoChar('●');

        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);


        //输入邮箱的部分
        JPanel emailPanel = new JPanel();
        emailPanel.setOpaque(false);
        emailPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel emailLabel = new JLabel("邮箱： ");
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setFont(new Font("微软雅黑", Font.BOLD, 25));

        JTextField emailField = new JTextField(15);
        emailField.setFont(new Font("微软雅黑", Font.BOLD, 20));


        emailPanel.add(emailLabel);
        emailPanel.add(emailField);

        //验证码区域
        JPanel verifyPanel = new JPanel();
        verifyPanel.setOpaque(false);
        verifyPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel verifyLabel = new JLabel("验证码：");
        verifyLabel.setForeground(Color.WHITE);
        verifyLabel.setFont(new Font("微软雅黑", Font.BOLD, 25));

        JTextField verifyField = new JTextField(8);
        verifyField.setFont(new Font("微软雅黑", Font.BOLD, 20));

        verifyButton = new JButton("发送验证码");
        verifyButton.setFont(new Font("微软雅黑", Font.BOLD, 15));

        verifyPanel.add(verifyLabel);
        verifyPanel.add(verifyField);
        verifyPanel.add(verifyButton);

//按键区域

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        //注册按键
        JButton registerButton = new JButton("注册");
        registerButton.setBackground(new Color(100, 149, 237, 220));
        registerButton.setFont(new Font("微软雅黑", Font.BOLD, 25));
        registerButton.setForeground(Color.WHITE);

        //返回按键
        JButton backButton = new JButton("返回 ");
        backButton.setBackground(new Color(100, 149, 237, 220));
        backButton.setFont(new Font("微软雅黑", Font.BOLD, 25));
        backButton.setForeground(Color.WHITE);

        buttonPanel.add(registerButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(backButton);


        inputAreaPanel.add(accountPanel);
        inputAreaPanel.add(passwordPanel);
        inputAreaPanel.add(emailPanel);
        inputAreaPanel.add(verifyPanel);

        registerPanel.add(inputAreaPanel, BorderLayout.CENTER);
        registerPanel.add(buttonPanel, BorderLayout.SOUTH);

        backgroundPanel.add(registerPanel);
        add(backgroundPanel);


        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Click_BGM.playClick();//点击音效

                if (verify == null || !verifyField.getText().equalsIgnoreCase(verify)) {
                    JOptionPane.showMessageDialog(RegisterPanel.this, "验证码错误！", "提示", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String newAccount = accountField.getText();
                String newPassword = passwordField.getText();
                String email = emailField.getText();
                String verify = verifyField.getText();
                double initBalance = 0;
                double balance = 0.0;


                if (newAccount.isEmpty() || newPassword.isEmpty() || verify.isEmpty()) {
                    JOptionPane.showMessageDialog(RegisterPanel.this, "请填写完整", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String result = atmService.register(newAccount, newPassword, initBalance, email);
                if ("添加账户成功".equals(result)) {

                    Speak_Jacob.speak("账户" + newAccount + "注册成功");
                    JOptionPane.showMessageDialog(RegisterPanel.this, "注册成功，请返回登录！", "提示", JOptionPane.INFORMATION_MESSAGE);
                    accountField.setText("");
                    passwordField.setText("");
                    emailField.setText("");
                    mainFrame.showCard(MainFrame.LOGIN, mainFrame.getCurrentAccount());
                } else {

                    JOptionPane.showMessageDialog(RegisterPanel.this, result, "注册失败", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Click_BGM.playClick();//点击音效
                mainFrame.showCard(MainFrame.LOGIN, mainFrame.getCurrentAccount());
            }
        });


        verifyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                Click_BGM.playClick();
                String email = emailField.getText().trim();
                if (email.isEmpty()) {
                    JOptionPane.showMessageDialog(RegisterPanel.this, "请先输入邮箱", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                // 生成验证码
                verify = generateCaptcha();
                String subject = "【百川银行】邮箱验证码";
                String body = String.format("尊敬的用户，\n\n您的验证码是：%s\n5 分钟内有效。", verify);
                new Thread(() -> {
                    try {
                        Mail.sendEmail(email, subject, body);
                    } catch (Exception ex) {
                        System.err.println("验证码邮件发送失败: " + ex.getMessage());
                    }
                }).start();
                JOptionPane.showMessageDialog(RegisterPanel.this, "验证码已发送至邮箱，请查收", "提示", JOptionPane.INFORMATION_MESSAGE);
                verifyButton.setText("重新发送");
            }
            });
                                       }
            //随机生成验证码
            private String generateCaptcha() {
                String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
                Random rnd = new Random();
                StringBuilder sb = new StringBuilder(4);
                for (int i = 0; i < 4; i++) {
                    sb.append(chars.charAt(rnd.nextInt(chars.length())));
                }
                return sb.toString();
            }



    }
