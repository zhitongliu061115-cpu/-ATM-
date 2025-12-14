package ATM.Client.ClientUI;

import ATM.Client.ATMService;
import ATM.Tools.Click_BGM;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ChangePasswordPanel extends JPanel {
    private ATMService atmService;
    private MenuPanel menuFrame;
    private BufferedImage changePasswordImage;

    private MainFrame mainFrame;

    public ChangePasswordPanel(ATMService atmService,MainFrame mainFrame) {
        this.atmService = atmService;
        this.mainFrame = mainFrame;


        try {
            changePasswordImage = ImageIO.read(getClass().getResourceAsStream("/ATM/resources/changePassword.jpg"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "无法加载背景图片", "错误", JOptionPane.ERROR_MESSAGE);
        }

        showChangePassword();
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




    //更改密码界面
    private void showChangePassword() {
        setLayout(new BorderLayout());

        JPanel backgroundPanel = createBackgroundPanel(changePasswordImage);
        backgroundPanel.setLayout(null);
        JPanel changePasswordPanel = new JPanel();
        changePasswordPanel.setOpaque(false);
        changePasswordPanel.setLayout(new BoxLayout(changePasswordPanel, BoxLayout.Y_AXIS));
        changePasswordPanel.setBounds(269, 450, 800, 200);

        //输入新密码
        JPanel newPasswordPanel = new JPanel();
        newPasswordPanel.setOpaque(false);
        newPasswordPanel.setLayout(new FlowLayout(FlowLayout.LEFT,10,5));
        JLabel newPasswordLabel =new JLabel("新密码：   ");
        newPasswordLabel.setForeground(Color.WHITE);
        newPasswordLabel.setFont(new Font("微软雅黑", Font.BOLD, 25));

        JPasswordField newPasswordField = new JPasswordField(10);

        newPasswordField.setFont(new Font("微软雅黑", Font.BOLD, 25));
        newPasswordField.setEchoChar('●');

        newPasswordPanel.add(newPasswordLabel);
        newPasswordPanel.add(newPasswordField);

        //确认密码
        JPanel verifyPanel = new JPanel();
        verifyPanel.setOpaque(false);
        verifyPanel.setLayout(new FlowLayout(FlowLayout.LEFT,10,5));
        JLabel verifyLabel=new JLabel("确认密码：");
        verifyLabel.setForeground(Color.WHITE);
        verifyLabel.setFont(new Font("微软雅黑", Font.BOLD, 25));

        JPasswordField verifyField = new JPasswordField(10);
        verifyField.setEchoChar('●');

        verifyField.setFont(new Font("微软雅黑", Font.BOLD, 25));
        verifyPanel.add(verifyLabel);
        verifyPanel.add(verifyField);

        changePasswordPanel.add(newPasswordPanel);
        changePasswordPanel.add(verifyPanel);
        backgroundPanel.add(changePasswordPanel);

       add(backgroundPanel);



        backgroundPanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                Point point = e.getPoint();
                int x = point.x;
                int y = point.y;

                if (x >= 893 && x <= 1150 && y >= 200 && y <= 350) {
                    Click_BGM.playClick();//点击音效
                    String newPassword = newPasswordField.getText();
                    String verifyPassword = verifyField.getText();
                    if (newPasswordField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(ChangePasswordPanel.this, "请输入新密码", "提示", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (verifyPassword.isEmpty()) {
                        JOptionPane.showMessageDialog(ChangePasswordPanel.this, "请输入验证密码", "提示", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if(newPassword.equals(verifyPassword)) {
                        String result= atmService.changePassword(mainFrame.getCurrentAccount(), newPassword);

                        JOptionPane.showMessageDialog(ChangePasswordPanel.this, "修改密码成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                        newPasswordField.setText("");
                        verifyField.setText("");


                        Timer timer = new Timer(2000, ae -> {
                            mainFrame.showCard(MainFrame.MENU, mainFrame.getCurrentAccount());
                        });
                        timer.setRepeats(false);
                        timer.start();

                        return;
                    }else{
                        JOptionPane.showMessageDialog(ChangePasswordPanel.this,"两次输入密码不一致，请重试","提示",JOptionPane.ERROR_MESSAGE);
                        newPasswordField.setText("");
                        verifyField.setText("");

                    }


                } else if (x >= 900 && x <= 1150 && y >= 410 && y <= 540) {
                    Click_BGM.playClick();//点击音效
                    mainFrame.showCard(MainFrame.MENU, mainFrame.getCurrentAccount());
                }
            }
        });

    }
}
