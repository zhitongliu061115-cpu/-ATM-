package ATM.Server.ServerUI;

import ATM.Server.service;
import org.jfree.chart.ChartTheme;

import javax.swing.*;
import java.awt.*;
import java.rmi.ServerError;

public class MainFrame {
    private JFrame frame;
    private service service;
    private ConnectionPanel connectionPanel;
    private AccountPanel accountPanel;
    private ConsolePanel consolePanel;
    private ChartPanel chartPanel;

    public MainFrame(service service) {
        this.service = service;
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("ATM服务器管理系统");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        frame.setLocationRelativeTo(null);

        // 创建选项卡面板
        JTabbedPane tabbedPane = new JTabbedPane();

        // 创建各个面板
        connectionPanel = new ConnectionPanel(this);
        consolePanel = new ConsolePanel();
        accountPanel = new AccountPanel(service, this);
        chartPanel = new ChartPanel(service, this);

        // 添加选项卡
        tabbedPane.addTab("连接管理", connectionPanel);
        tabbedPane.addTab("账户管理", accountPanel);
        tabbedPane.addTab("控制台", consolePanel);
        tabbedPane.addTab("数据统计", chartPanel);


        // 设置布局
        frame.setLayout(new BorderLayout());
        frame.add(tabbedPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    // 连接管理相关方法
    public void addConnection(String clientNum, String userName, String ipAddress) {
        connectionPanel.addConnection(clientNum, userName, ipAddress);
        logMessage("新客户端连接: " + userName + " (" + ipAddress + ")");
    }

    public void removeConnection(String clientId) {
        connectionPanel.removeConnection(clientId);
    }

    // 控制台日志方法
    public void logMessage(String msg) {
        consolePanel.logMessage(msg);
    }

    // 账户管理相关方法
    public void refreshAccountData() {
        accountPanel.loadAccountData();
    }

    public JFrame getFrame() {
        return frame;
    }

    //刷新图表
    public void refreshChartData() {
        if (chartPanel != null) {
            chartPanel.refreshData();
        }
    }



}

