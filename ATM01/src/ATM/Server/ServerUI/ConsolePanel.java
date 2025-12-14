package ATM.Server.ServerUI;

import ATM.Tools.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

public class ConsolePanel extends JPanel {
    private JTextArea controlArea;

    public ConsolePanel() {
        setLayout(new BorderLayout());
        initializeUI();
    }

    private void initializeUI() {
        JLabel explanation = new JLabel("显示服务器运行信息、连接日志和异常信息");
        add(explanation, BorderLayout.NORTH);

        controlArea = new JTextArea();
        controlArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(controlArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(scrollPane, BorderLayout.CENTER);

        //下方按键
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton clearButton = new JButton("清空");

        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controlArea.setText("");
            }
        });
        buttonPanel.add(clearButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    //控制台相关方法
    public void logMessage(String msg) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                String timestamp = getCurrentTime();
                String text = "["+timestamp+"]   "+msg + "\n";
                controlArea.append(text);

                Logger.log(msg);
            }
        });
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
