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

public class MenuPanel extends JPanel {
    private ATMService atmService;
    private BufferedImage menuImage;

    private MainFrame mainFrame;

    public MenuPanel(ATMService atmService, MainFrame mainFrame) {
        this.atmService = atmService;
        this.mainFrame = mainFrame;


        try {
            menuImage = ImageIO.read(getClass().getResourceAsStream("/ATM/resources/menu.jpg"));

        } catch (IOException e) {
            System.out.println("主菜单页面加载失败" + e.getMessage());
            e.printStackTrace();
        }
showMenu();
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


    public void showMenu() {

     setLayout(new BorderLayout());

        JPanel backgroundPanel = createBackgroundPanel(menuImage);
        backgroundPanel.setLayout(null);

        backgroundPanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Point point = e.getPoint();
                menuClick(point);
            }
        });
        add(backgroundPanel);

    }


    //主菜单界面相关方法
    //鼠标点击
    public void menuClick(Point point) {
        int x = point.x;
        int y = point.y;

        System.out.println("(" + x + "," + y + ")");


        //点击到不同区域，跳转到不同功能函数
        if (x >= 46 && x <= 285 && y >= 203 && y <= 324) {
            Click_BGM.playClick();//点击音效
           mainFrame.showCard(MainFrame.INQUIRE,mainFrame.getCurrentAccount());
           System.out.println("Inquire");
        } else if (x >= 895 && x <= 1131 && y >= 195 && y <= 315) {
            Click_BGM.playClick();//点击音效
            mainFrame.showCard(MainFrame.DEPOSIT, mainFrame.getCurrentAccount());
            System.out.println("Withdraw");
        } else if (x >= 49 && x <= 284 && y >= 400 && y <= 520) {
            Click_BGM.playClick();//点击音效
            mainFrame.showCard(MainFrame.TRANSFER, mainFrame.getCurrentAccount());
            System.out.println("Transfer");
        } else if (x >= 895 && x <= 1131 && y >= 380 && y <= 520) {
            Click_BGM.playClick();//点击音效
            mainFrame.showCard(MainFrame.WITHDRAW, mainFrame.getCurrentAccount());
            System.out.println("Deposit");
        } else if (x >= 43 && x <= 284 && y >= 590 && y <= 710) {
            Click_BGM.playClick();//点击音效
            mainFrame.showCard(MainFrame.CHANGEPASSWORD, mainFrame.getCurrentAccount());
            System.out.println("ChangePassword");
        } else if (x >= 895 && x <= 1130 && y >= 587 && y <= 710) {
            Click_BGM.playClick();//点击音效
            mainFrame.showCard(MainFrame.EXIT, mainFrame.getCurrentAccount());

            System.out.println("Quit");
        }


    }
}