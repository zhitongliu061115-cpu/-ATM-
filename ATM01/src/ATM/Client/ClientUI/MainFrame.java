package ATM.Client.ClientUI;

import ATM.Client.ATMService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MainFrame extends JFrame {
    private ATMService atmService;
    private String currentAccount;

    private JPanel cardPanel;
    private CardLayout cardLayout;

    private LoginPanel loginPanel;
    private MenuPanel menuPanel;
    private InquirePanel inquirePanel;
    private DepositPanel depositPanel;
    private WithdrawPanel withdrawPanel;
    private TransferPanel transferPanel;
    private ChangePasswordPanel changePwdPanel;
    private RegisterPanel registerPanel;
    private ExitPanel exitPanel;


    public static final String LOGIN = "LOGIN";
    public static final String REGISTER = "REGISTER";
    public static final String MENU = "MENU";
    public static final String INQUIRE = "INQUIRE";
    public static final String DEPOSIT = "DEPOSIT";
    public static final String WITHDRAW = "WITHDRAW";
    public static final String TRANSFER = "TRANSFER";
    public static final String CHANGEPASSWORD = "CHANGEPASSWORD";
    public static final String EXIT = "EXIT";


    public MainFrame(ATMService atmService) {
        this.atmService = atmService;

        setTitle("百川银行 ATM 客户端");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 894);
        setLocationRelativeTo(null);
        setResizable(false);

        // 初始化 CardLayout
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        //先创建各个界面
        loginPanel = new LoginPanel(this.atmService, this);
        menuPanel = new MenuPanel(this.atmService, this);
        inquirePanel = new InquirePanel(this.atmService, this);
        depositPanel = new DepositPanel(this.atmService, this);
        withdrawPanel = new WithdrawPanel(this.atmService, this);
        transferPanel = new TransferPanel(this.atmService, this);
        changePwdPanel = new ChangePasswordPanel(this.atmService, this);
        registerPanel = new RegisterPanel(this.atmService, this);
        exitPanel = new ExitPanel(this.atmService, this);

//添加到cardPanel
        cardPanel.add(loginPanel, LOGIN);
        cardPanel.add(menuPanel, MENU);
        cardPanel.add(inquirePanel, INQUIRE);
        cardPanel.add(depositPanel, DEPOSIT);
        cardPanel.add(withdrawPanel, WITHDRAW);
        cardPanel.add(transferPanel, TRANSFER);
        cardPanel.add(changePwdPanel, CHANGEPASSWORD);
        cardPanel.add(registerPanel, REGISTER);
        cardPanel.add(exitPanel, EXIT);

//先显示登录界面
        cardLayout.show(cardPanel, LOGIN);

        getContentPane().add(cardPanel);
    }

    //展示要求的界面
    public void showCard(String cardName, String account) {

        if (account != null) {
            this.currentAccount = account;
        }
        //若为Inquire，调用方法查询余额
        if (INQUIRE.equals(cardName)) {
            inquirePanel.refresh();
        }

        //若为EXIT，倒计时退出
        if (EXIT.equals(cardName)) {
            // 先把 EXIT 卡片 show 出来
            cardLayout.show(cardPanel, EXIT);
            // 再启动 ExitPanel 的定时器
            exitPanel.exitCountdown();
            return;
        }

//其他直接显示Panel
        cardLayout.show(cardPanel, cardName);
    }

    // 获取当前登录账号
    public String getCurrentAccount() {
        return currentAccount;
    }

//创建PDF
    private void generatePDF(String transactionType, String accountNumber, String toAccountNumber, double amount, String dateTime){
        // 使用 try-with-resources 确保 PDDocument 在完成后总是关闭
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            String customReceiptPath = "C:\\Users\\asus\\Desktop\\files\\课程相关\\面向对象程序设计实训\\作业\\ATM\\ATM01\\Receipts";
            File receiptsDir = new File(customReceiptPath);

            // 检查目录是否存在，如果不存在则尝试创建
            if (!receiptsDir.exists()) {
                if (receiptsDir.mkdirs()) { // 创建不存在的父目
                } else {
                    customReceiptPath = "."; // 回退到当前目录
                    receiptsDir = new File(customReceiptPath); // 更新 File 对象
                    if (!receiptsDir.exists()) receiptsDir.mkdirs(); // 再次尝试创建当前目录（通常会成功）
                }
            }

            String safeAccountNumber = accountNumber != null ? accountNumber.replaceAll("[^a-zA-Z0-9]", "_") : "unknown_account";
            String fileName = customReceiptPath + File.separator + "Receipt_" + transactionType + "_" + safeAccountNumber + "_" + System.currentTimeMillis() + ".pdf";
            // 使用 try-with-resources 确保 PDPageContentStream 在完成后总是关闭
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) { // 确保这里的 PDPageContentStream 是正确的导入
                PDType0Font font;
                try {
                    InputStream fontStream = getClass().getResourceAsStream("/ATM/resources/fonts/simsun.ttf");
//
                    font = PDType0Font.load(document, fontStream);
                } catch (IOException e) {
                    System.err.println("错误：加载中文字体失败: " + e.getMessage());
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                            "加载中文字体失败\n错误详情: " + e.getMessage() + "\n请检查字体文件配置。",
                            "PDF字体错误", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                contentStream.beginText();
                contentStream.setFont(font, 18);
                contentStream.setLeading(20.5f);
                contentStream.newLineAtOffset(50, 750);

                contentStream.showText("百川银行交易凭条");
                contentStream.newLine();
                contentStream.newLine();

                contentStream.setFont(font, 12);
                contentStream.showText("交易类型: " + transactionType);
                contentStream.newLine();
                contentStream.showText("交易账户: " + accountNumber);
                contentStream.newLine();
                if (toAccountNumber != null && !toAccountNumber.isEmpty()) {
                    contentStream.showText("对方账户: " + toAccountNumber);
                    contentStream.newLine();
                }
                contentStream.showText(String.format("交易金额: %.2f 元", amount));
                contentStream.newLine();
                contentStream.showText("交易时间: " + dateTime);
                contentStream.newLine();
                contentStream.newLine();
                contentStream.showText("--------------------------------------------------");
                contentStream.newLine();
                contentStream.showText("感谢您的使用，祝您生活愉快！");

                contentStream.endText();

            } // PDPageContentStream 关闭

            document.save(fileName); // 保存文档
            JOptionPane.showMessageDialog(this, "凭条已生成并保存到: " + fileName, "回单生成成功", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            System.err.println("生成PDF凭条失败 " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "生成PDF凭条失败: " + e.getMessage(), "PDF生成错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void showPrintReceiptDialog(String transactionType, String fromAccountNumber, String toAccountNumber, double amount) {
        int response = JOptionPane.showConfirmDialog(this,
                "交易成功！是否打印凭条？",
                "打印回单",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            String dateTime  =getCurrentTime();

            generatePDF(transactionType, fromAccountNumber, toAccountNumber, amount, dateTime);
        }
    }

    // 获取当前时间的方法
    public  static String getCurrentTime() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        String time= String.format("%04d-%02d-%02d %02d:%02d:%02d", year, month, day, hour, minute, second);
        return time;
    }

}

