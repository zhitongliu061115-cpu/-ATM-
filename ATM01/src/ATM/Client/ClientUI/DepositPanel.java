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

public class DepositPanel extends JPanel {
    private ATMService atmService;
    private BufferedImage depositImage;

    private MainFrame mainFrame;

    public DepositPanel(ATMService atmService,MainFrame mainFrame) {
        this.atmService = atmService;
        this.mainFrame = mainFrame;

        try {
            depositImage = ImageIO.read(getClass().getResourceAsStream("/ATM/resources/deposit.jpg"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "无法加载背景图片，请检查资源文件！", "错误", JOptionPane.ERROR_MESSAGE);
        }

        showDeposit();
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

    //存款界面
    private void showDeposit() {

       setLayout(new BorderLayout());

        JPanel backgroundPanel = createBackgroundPanel(depositImage);
        backgroundPanel.setLayout(null);

        JPanel depositPanel = new JPanel();
        depositPanel.setOpaque(false);
        depositPanel.setBounds(250, 440, 600, 60);
        depositPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel money = new JLabel("存款金额：");
        money.setFont(new Font("微软雅黑", Font.BOLD, 25));
        money.setForeground(Color.WHITE);

        JTextField moneyField = new JTextField(10);
        moneyField.setFont(new Font("微软雅黑", Font.BOLD, 25));

        depositPanel.add(money);
        depositPanel.add(moneyField);

        backgroundPanel.add(depositPanel);
        add(backgroundPanel);


        //鼠标点击监听器
        backgroundPanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Point point = e.getPoint();
                int x = point.x;
                int y = point.y;

                if (x >= 893 && x <= 1150 && y >= 200 && y <= 350) {
                    Click_BGM.playClick();//点击音效

                    if (moneyField.getText().isEmpty()||moneyField.getText()==null) {
                        JOptionPane.showMessageDialog(DepositPanel.this, "请输入存款金额", "提示", JOptionPane.ERROR_MESSAGE);
                        return;
                    } else if (Double.parseDouble(moneyField.getText()) <= 0) {
                        JOptionPane.showMessageDialog(DepositPanel.this, "取款金额应为正数", "提示", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    double money = Double.parseDouble(moneyField.getText());
                    String currentAccount = mainFrame.getCurrentAccount();
                    String result = atmService.deposit(currentAccount, money);
                    if (result != null && result.contains("成功")) {

                        Speak_Jacob.speak("存款成功！");
                        JOptionPane.showMessageDialog(DepositPanel.this, "存款成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                    moneyField.setText("");
                    //询问是否打印存单
                        mainFrame.showPrintReceiptDialog("存款", currentAccount, null, money);

                    }else {
                        JOptionPane.showMessageDialog(DepositPanel.this, result, "存款失败", JOptionPane.ERROR_MESSAGE);
                    }


                } else if (x >= 900 && x <= 1150 && y >= 410 && y <= 540) {
                    Click_BGM.playClick();//点击音效
                    mainFrame.showCard(MainFrame.MENU, mainFrame.getCurrentAccount());                }
            }
        });


    }




}
