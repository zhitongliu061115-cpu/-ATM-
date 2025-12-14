package ATM.Server;


import ATM.Server.ServerUI.MainFrame;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class clientHandler implements Runnable {
    private Socket socket;
    private service service;
    private MainFrame serverUI;
    private String clientId;
    private String currentUser = null; // 当前登录的用户
    private String clientIP;

    public clientHandler(Socket socket, service service, MainFrame serverUI, String clientId) {
        this.socket = socket;
        this.service = service;
        this.serverUI = serverUI;
        this.clientId = clientId;
        this.clientIP = socket.getInetAddress().getHostAddress();// 获取客户端 IP

    }


    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
           try {
             in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
          out = new PrintWriter(socket.getOutputStream(), true);


            serverUI.logMessage("客户端 " + clientId + " (" + clientIP + ") 连接");
            String line;
            while ((line = in.readLine()) != null) {
                String[] tokens = line.split(" ");
                String command = tokens[0];
                String response= " ";

                serverUI.logMessage(clientId+":"+command);
                switch (command) {
                    //登录
                    case "Login":
                    String account = tokens[1];
                    String password = tokens[2];

                    if(service.checkIdentify(account, password)) {
                        currentUser = account;
                        serverUI.addConnection(clientId,currentUser,clientIP);
                        serverUI.logMessage("登录成功  账户："+currentUser+",IP:"+clientIP);
                        response="登录成功";
                    }else{
                        serverUI.logMessage("登录失败  账户："+account+",IP:"+clientIP);
                        response="登录失败";
                    }

                    break;

                    //注册
                    case "Register":
                        if (tokens.length >= 4) {
                            String newAccount = tokens[1];
                            String newPassword = tokens[2];
                            double initBalance;
                            try {
                                initBalance = Double.parseDouble(tokens[3]);
                                String email = tokens[4];
                            } catch (NumberFormatException ex) {
                                response = "初始余额格式错误";
                                serverUI.logMessage("注册失败 - 余额格式非法: " + tokens[3]);
                                break;
                            }

                            String email=tokens[4];
                            String addResult = service.addAccount(newAccount, newPassword, initBalance,email);
                            response = addResult;
                            serverUI.logMessage("尝试注册账号：" + newAccount + "，" + addResult);
                        } else {
                            response = "注册命令格式错误";
                            serverUI.logMessage("注册命令格式错误，tokens 长度 < 4");
                        }
                        break;


                        //
                     //查询余额
                    case  "Inquiry":
                        if(tokens.length >=2) {
                            String accountName = tokens[1];
                            double accountBalance = service.getBalance(accountName);
                            response=String.valueOf(accountBalance);
                            serverUI.logMessage("账户："+accountName+"查询余额  余额：" + accountBalance);
                        }else{
                            serverUI.logMessage("查询余额命令格式错误");
                            response="-1";
                        }
                        break;


                     //存款
                     case "Deposit":
                         if(tokens.length >=3) {
                             String accountName = tokens[1];
                             double amount = Double.parseDouble(tokens[2]);
                             response= service.deposit(accountName, amount);
                             serverUI.logMessage("账户"+accountName+"存款"+amount+"元");

                         }else{
                             serverUI.logMessage("存款命令格式错误");
                             response="存款命令格式错误";
                         }
                         break;

                     //取款
                    case "Withdraw":
                        if(tokens.length >=3) {
                            String accountName = tokens[1];
                            double amount = Double.parseDouble(tokens[2]);
                            response=service.withdraw(accountName, amount);
                            serverUI.logMessage("账户"+accountName+"取款"+amount+"元");

                        }else{
                            serverUI.logMessage("取款命令格式错误");
                            response="取款命令格式错误";
                        }
                        break;

                    //转账
                    case "Transfer":
                      if(tokens.length >=4) {
                          String fromAccount = tokens[1];
                          String toAccount = tokens[2];
                          double amount = Double.parseDouble(tokens[3]);
                          response=service.transfer(fromAccount, toAccount, amount);
                          serverUI.logMessage("账户"+fromAccount+"转账给："+toAccount+amount+"元");
                      }else{
                          serverUI.logMessage("转账命令格式错误");
                          response="转账命令格式错误";
                      }

                         break;

                    //修改密码
                    case "ChangePassword":
                        if(tokens.length >=3) {
                            String accountName = tokens[1];
                            String newPassword = tokens[2];
                            response=service.changePassword(accountName, newPassword);
                            serverUI.logMessage("账户"+accountName+"修改密码为："+newPassword);

                        }else{
                            serverUI.logMessage("修改密码命令格式错误");
                            response="修改密码命令格式错误";
                        }
                        break;

                    //退出
                    case "Exit":
                        serverUI.removeConnection(clientId);
                        socket.close();
                        serverUI.logMessage("账户"+tokens[1]+"退出");
                        return;
                    default:
                        break;
                }
                out.println(response);
                serverUI.logMessage("发送给"+tokens[1]+":"+response);
            }
        } catch (IOException e) {
               SwingUtilities.invokeLater(() ->
                       serverUI.removeConnection(clientId)
               );
               serverUI.logMessage("客户端异常断开: " + clientId + " (" + clientIP + ")");
        }
        try {
            in.close();
            out.close();
            socket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
