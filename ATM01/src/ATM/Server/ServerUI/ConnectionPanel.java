package ATM.Server.ServerUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

public class ConnectionPanel extends JPanel {

    private MainFrame mainFrame;
    private JTable connectionTable;
    private DefaultTableModel connTableModel;

    public ConnectionPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        initializeUI();
    }

    private void initializeUI() {
        JPanel panel1 = new JPanel();
        JLabel nowConnectLabel = new JLabel("当前连接数");

        panel1.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel1.add(nowConnectLabel);

        add(panel1, BorderLayout.SOUTH);

        String[] form = {"序号", "用户名", "IP地址", "连接时间", "状态"};
        connTableModel = new DefaultTableModel(form, 0);
        connectionTable = new JTable(connTableModel);
        connectionTable.setDefaultEditor(Object.class, null);

        connectionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(connectionTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        JButton refresh = new JButton("刷新");

        refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                update();
                mainFrame.logMessage("连接列表已刷新");
                JOptionPane.showMessageDialog(mainFrame.getFrame(), "连接列表已刷新", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        buttonPanel.add(refresh);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    //添加新连接
    public void addConnection(String clientNum, String userName, String ipAddress) {
        String connectTime = getCurrentTime();
        Object[] rowData = {clientNum, userName, ipAddress, connectTime, "在线"};
        connTableModel.addRow(rowData);
        update();
    }

    //断开连接
    public void removeConnection(String clientId) {
        for (int i = 0; i < connTableModel.getRowCount(); i++) {
            if (connTableModel.getValueAt(i, 0).equals(clientId)) {
                String userName = (String) connTableModel.getValueAt(i, 1);
                String ipAddress = (String) connTableModel.getValueAt(i, 2);
                connTableModel.removeRow(i);
                mainFrame.logMessage("客户端断开连接: " + userName + " (" + ipAddress + ")");
                break;
            }
        }
        update();
    }

    //更新连接状态
    private void update() {
        int total = connTableModel.getRowCount();
        int online = 0;

        for (int i = 0; i < total; i++) {
            String status = (String) connTableModel.getValueAt(i, 4);
            if ("在线".equals(status)) {
                online++;
            }
        }
    }

    // 获取当前时间的方法
    private String getCurrentTime() {
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