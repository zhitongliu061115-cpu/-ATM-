package ATM.Client.ClientUI;

import ATM.Client.ATMService;
import ATM.Tools.Click_BGM;
import ATM.Tools.Speak_Jacob;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class WithdrawPanel extends JPanel {
    private ATMService atmService;
    private MenuPanel menuFrame;
    private BufferedImage withdrawImage;

    private MainFrame mainFrame;



    public WithdrawPanel(ATMService atmService, MainFrame mainFrame) {
        this.atmService = atmService;
        this.mainFrame = mainFrame;


        try {
            withdrawImage = ImageIO.read(getClass().getResourceAsStream("/ATM/resources/withdraw.jpg"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "无法加载背景图片，请检查资源文件！", "错误", JOptionPane.ERROR_MESSAGE);
        }

        showWithdraw();
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

    //取款界面
    private void showWithdraw() {

        setLayout(new BorderLayout());

        JPanel backgroundPanel = createBackgroundPanel(withdrawImage);
        backgroundPanel.setLayout(null);

        JPanel withdrawPanel = new JPanel();
        withdrawPanel.setOpaque(false);
        withdrawPanel.setBounds(250, 440, 600, 60);
        withdrawPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel money = new JLabel("取款金额：");
        money.setFont(new Font("微软雅黑", Font.BOLD, 25));
        money.setForeground(Color.WHITE);

        JTextField moneyField = new JTextField(10);
        moneyField.setFont(new Font("微软雅黑", Font.BOLD, 25));

        withdrawPanel.add(money);
        withdrawPanel.add(moneyField);

        backgroundPanel.add(withdrawPanel);
        add(backgroundPanel);

//鼠标点击监听器
        backgroundPanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Point point = e.getPoint();
                int x = point.x;
                int y = point.y;

                if (x >= 900 && x <= 1150 && y >= 210 && y <= 335) {
                    Click_BGM.playClick();//点击音效
                    if (moneyField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(WithdrawPanel.this, "请输入取款金额", "提示", JOptionPane.ERROR_MESSAGE);

                        return;

                    }else if (Double.parseDouble(moneyField.getText()) < 0) {
                        JOptionPane.showMessageDialog(WithdrawPanel.this, "取款金额需为正数", "提示", JOptionPane.ERROR_MESSAGE);
                    //打印回单

                    }else{
                        String input = moneyField.getText();
                        double money = Double.parseDouble(input);
                    String result = atmService.withdraw(mainFrame.getCurrentAccount(), money);
                    if (result.contains("成功")) {
                        Speak_Jacob.speak("取款成功！");
                        JOptionPane.showMessageDialog(WithdrawPanel.this, result, "提示", JOptionPane.INFORMATION_MESSAGE);
                        mainFrame.showPrintReceiptDialog("取款", mainFrame.getCurrentAccount(), null, money);

                        moneyField.setText("");

                    } else {
                        JOptionPane.showMessageDialog(WithdrawPanel.this, result, "提示", JOptionPane.ERROR_MESSAGE);
                    }


                } }else if (x >= 910 && x <= 1150 && y >= 405 && y <= 540) {
                    Click_BGM.playClick();//点击音效
                    mainFrame.showCard(MainFrame.MENU, mainFrame.getCurrentAccount());
                }
            }
        });


    }



}
