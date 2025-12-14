package ATM.Client.ClientUI;

import ATM.Client.ATMService;
import ATM.Tools.Speak_Jacob;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ExitPanel extends JPanel {
    private ATMService atmService;
    private BufferedImage exitImage;

    private MainFrame mainFrame;

    public ExitPanel(ATMService atmService, MainFrame mainFrame) {
        this.atmService = atmService;
        this.mainFrame = mainFrame;



        try {
            exitImage = ImageIO.read(getClass().getResourceAsStream("/ATM/resources/exit.jpg"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "无法加载背景图片，请检查资源文件！", "错误", JOptionPane.ERROR_MESSAGE);
        }
        showExit();
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


    //退出
    private void showExit() {

      setLayout(new BorderLayout());


        JPanel backgroundPanel = createBackgroundPanel(exitImage);
        backgroundPanel.setLayout(null);

        JLabel byeLabel =new JLabel("感谢使用百川银行ATM系统！");
        JLabel byeLabel2 =new JLabel("          祝您生活愉快！");


        byeLabel.setForeground(Color.WHITE);
        byeLabel.setFont(new Font("微软雅黑", Font.BOLD, 60));
        byeLabel.setBounds(230, 350, 1000, 100);
        byeLabel2.setForeground(Color.WHITE);
        byeLabel2.setFont(new Font("微软雅黑", Font.BOLD, 60));
        byeLabel2.setBounds(230, 450, 1000, 100);

        backgroundPanel.add(byeLabel);
        backgroundPanel.add(byeLabel2);
        add(backgroundPanel);
    }

    //倒计时退出方法
    public void exitCountdown(){
        Timer timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atmService.exit(mainFrame.getCurrentAccount());//服务器端退出
                System.exit(0); // 客户端直接关闭
            }
        });
        timer.setRepeats(false);
        timer.start();
        Speak_Jacob.speak("感谢使用，祝您生活愉快！");
    }
}
