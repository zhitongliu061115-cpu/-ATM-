package ATM.Client.ClientUI;

import ATM.Client.ATMService;
import ATM.Tools.QRCode;
import ATM.Tools.Click_BGM;
import ATM.Server.ServerUI.ConsolePanel;
import ATM.Tools.Speak_Jacob;
import com.google.zxing.WriterException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TransferPanel extends JPanel {
    private ATMService atmService;
    private MenuPanel menuFrame;
    private BufferedImage transferImage;
    MainFrame mainFrame;


    public TransferPanel(ATMService atmService, MainFrame mainFrame) {
        this.atmService = atmService;
        this.mainFrame = mainFrame;


        try {
            transferImage = ImageIO.read(getClass().getResourceAsStream("/ATM/resources/transfer.jpg"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "无法加载背景图片，请检查资源文件！", "错误", JOptionPane.ERROR_MESSAGE);
        }

        showTransfer();
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


    //转账界面
    private void showTransfer() {

        setLayout(new BorderLayout());

        JPanel backgroundPanel = createBackgroundPanel(transferImage);
        backgroundPanel.setLayout(null);
        JPanel transferPanel = new JPanel();
        transferPanel.setOpaque(false);
        transferPanel.setBounds(269, 409, 500, 200);

        transferPanel.setLayout(new BoxLayout(transferPanel, BoxLayout.Y_AXIS));

        //输入转入账号
        JPanel toAccountPanel = new JPanel();
        toAccountPanel.setOpaque(false);
        toAccountPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel toAccountLabel = new JLabel("转入账号：");
        toAccountLabel.setForeground(Color.WHITE);
        toAccountLabel.setFont(new Font("微软雅黑", Font.BOLD, 25));

        JTextField toAccountField = new JTextField(10);
        toAccountField.setFont(new Font("微软雅黑", Font.BOLD, 20));

        toAccountPanel.add(toAccountLabel);
        toAccountPanel.add(toAccountField);

        //输入转账金额
        JPanel moneyPanel = new JPanel();
        moneyPanel.setOpaque(false);
        moneyPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel moneyLabel = new JLabel("转账金额：");
        moneyLabel.setForeground(Color.WHITE);
        moneyLabel.setFont(new Font("微软雅黑", Font.BOLD, 25));

        JTextField moneyField = new JTextField(10);
        moneyField.setFont(new Font("微软雅黑", Font.BOLD, 20));

        moneyPanel.add(moneyLabel);
        moneyPanel.add(moneyField);

        transferPanel.add(toAccountPanel);
        transferPanel.add(moneyPanel);

        backgroundPanel.add(transferPanel);
        add(backgroundPanel);


        backgroundPanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                Point point = e.getPoint();
                int x = point.x;
                int y = point.y;

                if (x >= 893 && x <= 1150 && y >= 200 && y <= 350) {
                    Click_BGM.playClick();//点击音效
                    if (moneyField.getText().isEmpty() || toAccountField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(TransferPanel.this, "请输入完整信息", "提示", JOptionPane.ERROR_MESSAGE);
                        return;
                    } else if (Double.parseDouble(moneyField.getText()) <= 0) {
                        JOptionPane.showMessageDialog(TransferPanel.this, "取款金额应为正数", "提示", JOptionPane.ERROR_MESSAGE);
                        return;
                    } else if (toAccountField.getText().equals(mainFrame.getCurrentAccount())) {
                        JOptionPane.showMessageDialog(TransferPanel.this, "转入账户不能为本账户", "提示", JOptionPane.ERROR_MESSAGE);

                    }else{
                        double money = Double.parseDouble(moneyField.getText());
                        String toAccount = toAccountField.getText();
                        String result = atmService.transfer(mainFrame.getCurrentAccount(), toAccount, money);

                        if (result.equals("转账成功")) {
                            Speak_Jacob.speak("向账户：" + toAccount + "转账成功");
                            JOptionPane.showMessageDialog(TransferPanel.this, "转账成功，金额：" + money + "元", "提示", JOptionPane.INFORMATION_MESSAGE);
                            moneyField.setText("");
                            toAccountField.setText("");


                            String timestamp = ConsolePanel.getCurrentTime();
                            String content = String.format(
                                    "=== 银行转账凭证 ===\n" +
                                            "转出账号：%s\n" +
                                            "转入账号：%s\n" +
                                            "金额：%.2f 元\n" +
                                            "时间：%s\n\n" +
                                            "凭证仅供保存，切勿外泄。",
                                    mainFrame.getCurrentAccount(), toAccount, money, timestamp);

                            try {
                                // 调用工具类生成二维码 BufferedImage
                                BufferedImage qrImage = QRCode.createQRCodeImage(content, 250, 250);

                                // 弹出新窗口，展示二维码
                                showQRCode(qrImage, content);

                            } catch (WriterException ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(TransferPanel.this, "二维码生成失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                            }

                        } else {
                            JOptionPane.showMessageDialog(
                                    TransferPanel.this, result, "提示", JOptionPane.ERROR_MESSAGE);
                        }

                    }


                } else if (x >= 900 && x <= 1150 && y >= 410 && y <= 540) {
                    Click_BGM.playClick();//点击音效
                    mainFrame.showCard(MainFrame.MENU, mainFrame.getCurrentAccount());
                }
            }
        });


    }

    //生成二维码窗口
    private void showQRCode(BufferedImage qrImage, String content) {

        JDialog dialog = new JDialog(mainFrame, "交易二维码", true);
        dialog.setSize(300, 400);
        dialog.setLocationRelativeTo(TransferPanel.this);
        dialog.setResizable(false);
        dialog.setLayout(new BorderLayout());


        //提示文字
        JLabel tipsLabel = new JLabel("扫码可保存转账明细");
        tipsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        tipsLabel.setFont(new Font("微软雅黑", Font.PLAIN, 20));

        // 将 BufferedImage 放到 JLabel
        JLabel qrLabel = new JLabel(new ImageIcon(qrImage));
        qrLabel.setHorizontalAlignment(SwingConstants.CENTER);

        //添加按键区域

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton okButton = new JButton("确定");
        buttonPanel.add(okButton);

        dialog.add(tipsLabel, BorderLayout.NORTH);
        dialog.add(qrLabel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        buttonPanel.add(okButton);

        dialog.add(tipsLabel, BorderLayout.NORTH);
        dialog.add(qrLabel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        okButton.addActionListener(e -> {
            dialog.dispose(); // 先关闭二维码对话框

            // 2 秒后关闭 TransferFrame，返回主菜单
            new javax.swing.Timer(2000, evt -> {
                mainFrame.showCard(MainFrame.MENU, mainFrame.getCurrentAccount());
            }) {{
                setRepeats(false);
                start();
            }};
        });


        dialog.setVisible(true);


    }

}
