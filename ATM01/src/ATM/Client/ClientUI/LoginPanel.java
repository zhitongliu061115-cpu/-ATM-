package ATM.Client.ClientUI;

import ATM.Client.ATMService;
import ATM.Tools.BGM;
import ATM.Tools.Click_BGM;
import ATM.Tools.Speak_Jacob;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class LoginPanel extends JPanel {
    private ATMService atmService;
    private BufferedImage loginImage;
    int attemptNum=0;
    private String verify;
    JButton verifyButton;
    private MainFrame mainFrame;


    public LoginPanel(ATMService atmService, MainFrame mainFrame) {
        this.atmService = atmService;
        this.mainFrame=mainFrame;

        BGM.init();

        try {
            loginImage = ImageIO.read(getClass().getResourceAsStream("/ATM/resources/login.jpg"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "无法加载背景图片，请检查资源文件！", "错误", JOptionPane.ERROR_MESSAGE);
        }
        showLoginWindow();
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

    public void showLoginWindow() {
    setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1200, 894));


//贴图背景
        JPanel backgroundPanel = createBackgroundPanel(loginImage);
        backgroundPanel.setLayout(null);

        //创建操作部分
        JPanel loginPanel = new JPanel();

        loginPanel.setOpaque(false);//设置透明

        loginPanel.setLayout(new BorderLayout());
        loginPanel.setBounds(575, 350, 400, 275);

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

        //验证码区域

         JPanel verifyPanel = new JPanel();
        verifyPanel.setOpaque(false);
        verifyPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel verifyLabel = new JLabel("验证码：");
        verifyLabel.setForeground(Color.WHITE);
        verifyLabel.setFont(new Font("微软雅黑", Font.BOLD, 25));

        JTextField verifyField = new JTextField(8);
        verifyField.setFont(new Font("微软雅黑", Font.BOLD, 20));

        verifyButton = new JButton();
        verifyButton.setFont(new Font("Comic Sans MS", Font.BOLD, 20));

        verifyPanel.add(verifyLabel);
        verifyPanel.add(verifyField);
        verifyPanel.add(verifyButton);


        //按键区域

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        //登录按键
        JButton loginButton = new JButton("登录");
        loginButton.setBackground(new Color(100, 149, 237, 220));
        loginButton.setFont(new Font("微软雅黑", Font.BOLD, 25));
        loginButton.setForeground(Color.WHITE);

        //注册按键
        JButton registerButton = new JButton("注册");
        registerButton.setBackground(new Color(100, 149, 237, 220));
        registerButton.setFont(new Font("微软雅黑", Font.BOLD, 25));
        registerButton.setForeground(Color.WHITE);

        buttonPanel.add(loginButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(registerButton);

        //组装面板
        inputAreaPanel.add(accountPanel);
        inputAreaPanel.add(Box.createVerticalStrut(10));
        inputAreaPanel.add(passwordPanel);
        inputAreaPanel.add(Box.createVerticalStrut(10));
        inputAreaPanel.add(verifyPanel);


        loginPanel.add(inputAreaPanel, BorderLayout.CENTER);
        loginPanel.add(buttonPanel, BorderLayout.SOUTH);

        backgroundPanel.add(loginPanel);

      add(backgroundPanel);
        iniVrify();

        loginButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Click_BGM.playClick();//点击音效
                //验证验证码
                if (!verifyField.getText().equalsIgnoreCase(verify)) {
                    JOptionPane.showMessageDialog(LoginPanel.this, "验证码错误！", "提示", JOptionPane.ERROR_MESSAGE);
                    iniVrify();
                    return;
                }

                String accountName = accountField.getText();
                String password = new String(passwordField.getPassword());

                if (accountName.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginPanel.this, "请输入完整账号密码！", "提示", JOptionPane.WARNING_MESSAGE);
                    return;

                }


                String result = atmService.login(accountName, password);
                if ("登录成功".equals(result)) {
                    Speak_Jacob.speak("账户"+accountName+",欢迎使用百川银行");
                    mainFrame.showCard(MainFrame.MENU, accountName);
                } else {
                    attemptNum++;
                    if (attemptNum==3) {
                        JOptionPane.showMessageDialog(LoginPanel.this, "失败次数过多，您的账户已被锁定\n请前往银行服务大厅或拨打：010-810-8088", "提示", JOptionPane.ERROR_MESSAGE);
                        mainFrame.showCard(MainFrame.EXIT, accountName);

                    }else{

                    JOptionPane.showMessageDialog(LoginPanel.this, "登录失败，请检查账号密码", "提示", JOptionPane.WARNING_MESSAGE);
                    return;}
                }

            }
        });

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Click_BGM.playClick();//点击音效
                mainFrame.showCard(MainFrame.REGISTER, null);
            }
        });

        verifyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Click_BGM.playClick();//点击音效
                iniVrify();
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

    // 初始化/刷新验证码
    private void iniVrify() {
        verify = generateCaptcha();
        verifyButton.setText(verify);
    }



}
