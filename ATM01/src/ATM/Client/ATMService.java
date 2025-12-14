package ATM.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ATMService {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ATMService(Socket socket) throws IOException {
        this.socket = socket;
        this.in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out=new PrintWriter(socket.getOutputStream(),true);

    }

    //登录
    public String login(String account, String password) {
        String command="Login "+account+" "+password;
        out.println(command);
        try {
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //注册
    public String register(String account, String password, double balance, String email) {

        String command = "Register " + account + " " + password + " " + balance+ " " + email;
        out.println(command);
        try {
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException("注册请求失败：" + e.getMessage());
        }}

    //查询余额
    public String inquire(String account) {
        if (account == null || account.isEmpty()) {
            return "账户无效";
        }
        String command="Inquiry "+account;
        out.println(command);
        try {
            String response=in.readLine();
            if (response == null) {
                return "服务器未响应";
            } else if(response.equals("-1")){
                return "用户不存在";

            }
            return response;
        } catch (IOException e) {
            throw new RuntimeException("查询余额失败：" + e.getMessage());
        }
    }

    //存款
    public String deposit(String account, double amount) {
        String command="Deposit "+account+" "+amount;
        out.println(command);
        try {
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    //取款
    public String withdraw(String account, double amount) {
        String command="Withdraw "+account+" "+amount;
        out.println(command);
        try {
            return  in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //转账
    public String transfer(String from, String to, double amount) {
        String command="Transfer "+from+" "+to+" "+amount;
        out.println(command);
        try {
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //修改密码
    public String changePassword(String account,  String newPassword) {
        String command="ChangePassword "+account+" "+newPassword;
        out.println(command);
        try {
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //退出
    public void exit(String account) {
       try {
           String command="Exit";
           out.println(command);
       }catch (Exception e){
           System.err.println("退出时发生异常: " + e.getMessage());
       }

    }


}
