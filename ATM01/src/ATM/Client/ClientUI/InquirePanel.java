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

public class InquirePanel extends JPanel {
    private ATMService atmService;
    private MenuPanel menuFrame;
    private BufferedImage inquireImage;
    private  JLabel showInquireLabel;

    private MainFrame mainFrame;

    public InquirePanel(ATMService atmService,MainFrame mainFrame) {
        this.atmService = atmService;

        this.mainFrame = mainFrame;

        try {
            inquireImage = ImageIO.read(getClass().getResourceAsStream("/ATM/resources/inquire.jpg"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "无法加载背景图片，请检查资源文件！", "错误", JOptionPane.ERROR_MESSAGE);
        }
        ;

        showInquire();

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

    //余额查询界面
    public void showInquire() {


        setLayout(new BorderLayout());


        JPanel backgroundPanel = createBackgroundPanel(inquireImage);
        backgroundPanel.setLayout(null);



      showInquireLabel = new JLabel("余额： 元");
        showInquireLabel.setFont(new Font("微软雅黑", Font.BOLD, 40));
        showInquireLabel.setForeground(Color.WHITE);
        showInquireLabel.setBounds(300, 440, 600, 60);


        backgroundPanel.add(showInquireLabel);
        add(backgroundPanel);


        //鼠标点击监听器
        backgroundPanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                Point point = e.getPoint();
                int x = point.x;
                int y = point.y;

                if (x >= 910 && x <= 1150 && y >= 190 && y <= 310) {
                    Click_BGM.playClick();//点击音效
                    mainFrame.showCard(MainFrame.WITHDRAW, mainFrame.getCurrentAccount());
                } else if (x >= 910 && x <= 1150 && y >= 370 && y <= 500) {
                    Click_BGM.playClick();//点击音效
                    mainFrame.showCard(MainFrame.TRANSFER, mainFrame.getCurrentAccount());
                } else if (x >= 910 && x <= 1150 && y >= 570 && y <= 690) {
                    Click_BGM.playClick();//点击音效
                    mainFrame.showCard(MainFrame.MENU, mainFrame.getCurrentAccount());
                }

            }

        });


    }

    public void refresh() {
        // 刷新余额显示
        if (mainFrame.getCurrentAccount() != null) {
            String balance = atmService.inquire(mainFrame.getCurrentAccount());
            if (balance == null || balance.isEmpty() || balance.equals("用户不存在")) {
                balance = "（无法获取）";
            }
            showInquireLabel.setText("余额：" + balance + " 元");
            System.out.println("余额已刷新: " + balance);
        } else {
            showInquireLabel.setText("余额：未登录 元");
        }
    }

}
