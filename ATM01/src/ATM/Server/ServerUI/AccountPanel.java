package ATM.Server.ServerUI;

import ATM.Account;
import ATM.Server.service;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


public class AccountPanel extends JPanel {
    private MainFrame mainFrame;
    private service service;
    private JTable acccountTable;
    private DefaultTableModel acccountTableModel;
    private JTextField accountSearch;

    public AccountPanel(service service,MainFrame mainFrame) {
        this.service = service;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        initializeUI();
        loadAccountData();
    }

    private void initializeUI() {
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        //上侧查询用户账户信息
        searchPanel.add(new JLabel("账号:"));
        accountSearch = new JTextField(15);
        JButton searchButton = new JButton("查询");
        JButton refreshButton = new JButton("刷新全部");

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchAccount();
            }
        });
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadAccountData();
            }
        });

        searchPanel.add(accountSearch);
        searchPanel.add(searchButton);
        searchPanel.add(refreshButton);
        add(searchPanel, BorderLayout.NORTH);

        String[] form = {"账号", "余额", "密码","邮箱"};
        acccountTableModel = new DefaultTableModel(form, 0);
        //设置表格状态
        acccountTable = new JTable(acccountTableModel);
        acccountTable.setDefaultEditor(Object.class, null);

        acccountTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(acccountTable);
        add(scrollPane, BorderLayout.CENTER);

        //下方按钮
        JPanel buttonPanel = new JPanel();
        JButton addAccountButton = new JButton("添加账户");
        JButton editAccountButton = new JButton("修改余额");
        JButton resetPasswordButton = new JButton("重置密码");
        buttonPanel.add(addAccountButton);
        buttonPanel.add(editAccountButton);
        buttonPanel.add(resetPasswordButton);
        add(buttonPanel, BorderLayout.SOUTH);

        //给按钮添加监听器
        //"添加账户"
        addAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addAccount();
            }
        });

        //"修改余额"
        editAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editAccount();
            }
        });

        //"重置密码"
        resetPasswordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetPassword();
            }
        });

    }

    //加载账户信息
    public void loadAccountData() {
        acccountTableModel.setRowCount(0);

        List<Account> accounts = service.getAccounts();

        for (Account account : accounts) {
            Object[] rowData = {
                    account.getAccountNumber(),
                    String.format("%.2f", account.getBalance()),
                    account.getPassword(),
                    account.getEmail() == null ? "" : account.getEmail()
            };
            acccountTableModel.addRow(rowData);
        }

        mainFrame.logMessage("账户数据加载完成，共" + accounts.size() + "个账户");
    }

    //查询用户账户信息
    private void searchAccount() {
        String accountNum = accountSearch.getText();
        if (accountNum.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame.getFrame(), "请输入账号：", " 提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        double balance = service.getBalance(accountNum);
        if (balance == -1) {
            JOptionPane.showMessageDialog(mainFrame.getFrame(), "账户不存在", "提示", JOptionPane.WARNING_MESSAGE);
            mainFrame.logMessage("查询账户" + accountNum + "失败，账户不存在");
        } else {
            acccountTableModel.setRowCount(0);
            Account account=service.getAccounts(accountNum);
            Object[] rowData = {accountNum, String.format("%.2f", balance),account.getPassword(),account.getEmail() };
            acccountTableModel.addRow(rowData);
            accountSearch.setText("");
            mainFrame.logMessage("查询账户" + accountNum + "成功，余额" + balance + "元");
        }
    }

    //添加账户
    private void addAccount() {
        JDialog dialog = new JDialog(mainFrame.getFrame(), "添加账户", true);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(mainFrame.getFrame());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 5, 5));

        JTextField accountNum = new JTextField(15);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setEchoChar('●');
        JTextField balanceField = new JTextField("0.00");
        JTextField emailField=new JTextField("");

        panel.add(new JLabel("账号："));
        panel.add(accountNum);
        panel.add(new JLabel("密码："));
        panel.add(passwordField);
        panel.add(new JLabel("余额："));
        panel.add(balanceField);
        panel.add(new JLabel("邮箱："));
        panel.add(emailField);

        JButton confirmButton = new JButton("确定");
        JPanel confirmPanel = new JPanel();
        confirmPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        confirmPanel.add(confirmButton);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(confirmPanel, BorderLayout.SOUTH);

        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String accountNumber = accountNum.getText();
                String password = new String(passwordField.getPassword());
                String balance = balanceField.getText();
                String email = emailField.getText();

                if (accountNumber.isEmpty() || password.isEmpty() || balance.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "请完整填写", "提示", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                double Balance = Double.parseDouble(balance);
                if (Balance < 0) {
                    JOptionPane.showMessageDialog(dialog, "余额不能为负", "提示", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (service.getBalance(accountNumber) != -1) {
                    JOptionPane.showMessageDialog(dialog, "账户已存在", "提示", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String result = service.addAccount(accountNumber, password, Balance,email);
                if ("添加账户成功".equals(result)) {
                    loadAccountData();
                    dialog.dispose();
                    mainFrame.logMessage("添加新账户: " + accountNumber + ", 初始余额: " + balance);
                    mainFrame.refreshChartData(); // 刷新图表
                } else {
                    JOptionPane.showMessageDialog(dialog, result, "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        dialog.setVisible(true);
    }

    //修改余额
    private void editAccount() {
        int selectedRow = acccountTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(mainFrame.getFrame(), "请先选择要修改余额的账户", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String accountNumber = (String) acccountTable.getValueAt(selectedRow, 0);
        String currentBalance = (String) acccountTable.getValueAt(selectedRow, 1);
        String newBalance = JOptionPane.showInputDialog(mainFrame.getFrame(), "请输入新的余额：");

        if (!(newBalance.isEmpty())) {
            double Balance = Double.parseDouble(newBalance);
            if (Balance < 0) {
                JOptionPane.showMessageDialog(mainFrame.getFrame(),"余额不能为负","提示",JOptionPane.WARNING_MESSAGE);
                return;
            }
            String result = service.changeBalance(accountNumber, Balance);
            if("修改余额成功".equals(result)) {
                loadAccountData();
                mainFrame.logMessage("修改账户余额: " + accountNumber + ", 新余额: " + newBalance);
                mainFrame.refreshChartData(); // 刷新图表
            }else {
                JOptionPane.showMessageDialog(mainFrame.getFrame(),"错误","提示",JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
    }

    //重置密码
    private void resetPassword() {
        int selectedRow = acccountTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(mainFrame.getFrame(),"请先选择要重置密码的账户","提示",JOptionPane.WARNING_MESSAGE);
            return;
        }

        String accountNumber = (String) acccountTable.getValueAt(selectedRow, 0);
        String newPassword=JOptionPane.showInputDialog(mainFrame.getFrame(),"请输入新密码：");

        if (!newPassword.isEmpty()) {
            String result= service.changePassword(accountNumber, newPassword);
            mainFrame.logMessage("重置账户"+accountNumber+"的密码为："+newPassword);
            JOptionPane.showMessageDialog(mainFrame.getFrame(),"密码修改成功，请牢记新密码，切勿告诉他人","提示",JOptionPane.WARNING_MESSAGE);
            loadAccountData();
            mainFrame.refreshChartData(); // 刷新图表
        }else{
            JOptionPane.showMessageDialog(mainFrame.getFrame(),"密码不能为空","提示",JOptionPane.WARNING_MESSAGE);
        }
    }
}
