package ATM.Server;

import ATM.Account;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import ATM.Tools.Mail;


public class service {
    private static final String DATA = "data.txt";
    private final List<Account> accounts = new ArrayList<>();

    public service() {
        load();
        if (accounts.isEmpty()) {
            accounts.add(new Account("1001", "123456", 1000.0,""));
            save();
        }
    }

    //加载用户列表
    private void load() {
        accounts.clear();
        File file = new File(DATA);
        if (!file.exists()) {
            System.out.println("未找到文件");
            return;
        }
        BufferedReader br = null;
        try{
            br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",",4);
                if (data.length < 3) continue;
                String accountNumber = data[0];
                String password = data[1];
                double balance = Double.parseDouble(data[2]);
                String email = data[3];
                accounts.add(new Account(accountNumber, password, balance,email));

            }
        } catch (IOException e) {
            System.err.println("加载账户数据失败: " + e.getMessage());
        }
        try {
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    //保存账户
    private synchronized void save() {
        BufferedWriter bw = null;
        try {
           bw = new BufferedWriter(new FileWriter(DATA));
            for (Account account : accounts) {
                bw.write(account.toString());
                // 逐个写入账户信息，格式为：账户号,密码,余额,邮箱
                bw.newLine();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
//        File file = new File(DATA);
//        try {
//            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA));
//            out.writeObject(accounts);
//        } catch (IOException e) {
//            System.out.println("保存失败" + e.getMessage());
//        }




    //查找账户
    private Account findAccount(String accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }


    // 验证身份
    public synchronized boolean checkIdentify(String accountNumber, String password) {
        Account a = findAccount(accountNumber);
        if ((a != null) && (a.checkPassword(password))) {
            return true;
        }
        return false;
    }

    //查询余额
    public synchronized double getBalance(String accountNumber) {
        Account a = findAccount(accountNumber);
        if (a != null) {
            return a.getBalance();
        }
        return -1;
    }

    //存款
    public synchronized String deposit(String accountNumber, double amount) {
        Account a = findAccount(accountNumber);
        if (amount <= 0) {
            return "金额需为正数";

        } else if (a == null) {
            return "账户不存在";
        } else if (a != null) {
            a.deposit(amount);
            save();
            return "存款" + amount + "元成功,余额：" + a.getBalance() + "元";
        }
        return "error";

    }

    //取款
    public synchronized String withdraw(String accountNumber, double amount) {
        Account a = findAccount(accountNumber);
        if (amount <= 0) {
            return "金额需为正数";

        }else if (a == null) {
            return "账户不存在";

        }else if (a != null) {
           if( a.withdraw(amount)){
            save();
           return "取款"+amount+"元成功，余额"+a.getBalance()+"元";
           }else{
               return "余额不足";
           }
        }
        return "error";
    }

    //转账
    public synchronized String transfer(String from, String to, double amount) {
        Account a = findAccount(from);
        Account b = findAccount(to);
        if(amount <= 0) {
            return "金额需为正数";
        }
        if (a==null) {
            return "转出账户不存在";
        }
        if (b==null) {
            return "转入账户不存在";
        }
        if (a != null&&b != null) {
            if(!a.withdraw(amount)){
                return "余额不足";
            }
            b.deposit(amount);
            save();
            return "转账成功";
        }
        return "error";
    }

    //修改密码
    public synchronized String changePassword(String account, String newPassword) {
        Account a = findAccount(account);
        if (a == null) {
            return "账户不存在";
        }
       a.changePassword(newPassword);
        save();

        //发送邮件
        String toEmail = a.getEmail();

        new Thread(() -> {
            try {
                String subject = "【百川银行】密码修改通知";
                String body = String.format(
                        "尊敬的用户，您好！\n\n" +
                                "您的账户 %s 的登录密码已修改成功。\n\n" +
                                "如非本人操作，请立即致电银行。\n"+
                                "24小时客服热线：010-810-8088\n\n" +
                                "祝您生活愉快！",
                        account
                );
                Mail.sendEmail(toEmail, subject, body);
            } catch (Exception e) {
                System.err.println("修改密码邮件发送失败: " + e.getMessage());
            }
        }).start();


        return "密码修改成功";
    }



    //获取账户
    public synchronized List<Account> getAccounts() {
        return new ArrayList<>(accounts);
    }

    public synchronized Account getAccounts(String accountNumber) {
        return findAccount(accountNumber);
    }



    //添加账户
    public synchronized String addAccount(String accountNumber, String password, double balance,String email) {
        Account a = findAccount(accountNumber);
        if (findAccount(accountNumber) != null) {
            return "账户已存在";
        }
        if(balance<0){
            return"余额不能小于0";
        }
        a = new Account(accountNumber, password, balance, email);
        accounts.add(a);
        save();


        //发送邮件
        final String toEmail= a.getEmail();
        //新开线程
        new Thread(()->{
try{
    String subject = "【百川银行】欢迎注册";
    String body = String.format(
            "尊敬的用户，您好！\n\n" +
                    "您的账户 %s 注册成功。\n" +
                    "初始余额：%.2f 元。\n\n" +
                    "如果不是您本人操作，请及时致电银行。\n" +
                    "24小时客服热线：010-810-8088\n\n" +
                    "祝您生活愉快！",

            accountNumber, balance
    );

    Mail.sendEmail(toEmail, subject, body);
}catch (Exception e){
    System.err.println("注册邮件发送失败: " + e.getMessage());
}
        }).start();
        return "添加账户成功";
    }


    //修改余额
    public synchronized String changeBalance(String accountNumber, double newBalance) {
        Account a = findAccount(accountNumber);
        if (a == null) {
            return "账户不存在";
        }
        if(newBalance<0){
            return"余额不能小于0";

        }
        a.setBalance(newBalance);
        save();
        return "修改余额成功";
    }

    //删除账户
    public synchronized String deleteAccount(String accountNumber) {
        Account a = findAccount(accountNumber);
        if (a == null) {
            return "账户不存在 ";

        }
        accounts.remove(a);
        save();
        return "账户已删除";
    }

    // 获取账户余额分布统计
    public synchronized Map<String, Integer> getBalanceDistribution() {
        Map<String, Integer> distribution = new HashMap<>();
        distribution.put("0-1000", 0);
        distribution.put("1000-5000", 0);
        distribution.put("5000-10000", 0);
        distribution.put("10000+", 0);

        for (Account account : accounts) {
            double balance = account.getBalance();
            if (balance < 1000) {
                distribution.put("0-1000", distribution.get("0-1000") + 1);
            } else if (balance < 5000) {
                distribution.put("1000-5000", distribution.get("1000-5000") + 1);
            } else if (balance < 10000) {
                distribution.put("5000-10000", distribution.get("5000-10000") + 1);
            } else {
                distribution.put("10000+", distribution.get("10000+") + 1);
            }
        }
        return distribution;
    }

    //获取全部账户余额
    public synchronized Map<String, Double> getAllAccountBalances() {
        Map<String, Double> result = new HashMap<>();
        for (Account account : accounts) {
            // 使用账户号作为 key，余额作为 value
            result.put(account.getAccountNumber(), account.getBalance());
        }
        return result;
    }

    // 获取账户数量
    public synchronized int getTotalAccounts() {
        return accounts.size();
    }

    // 获取总资产
    public synchronized double getTotalAssets() {
        double total = 0;
        for (Account account : accounts) {
            total += account.getBalance();
        }
        return total;
    }

    // 获取平均余额
    public synchronized double getAverageBalance() {
        if (accounts.isEmpty()) return 0;
        return getTotalAssets() / accounts.size();
    }
}

