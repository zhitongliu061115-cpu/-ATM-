package ATM;



public class Account {
    //定义账户、余额、密码、邮箱
    private String accountNumber;
    private double balance;
    private String password;
    private String email;

    //初始化
    public Account(String accountNumber, String password, double balance, String email) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.password = password;
        this.email = email;
    }

    public Account() {
    }

    public String getAccountNumber() {
        return accountNumber;

    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;

    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;

    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    //验证密码
    public boolean checkPassword(String password) {
        if(this.password.equals(password)){
            return true;
        }else {
            return false;
        }
    }

    //存款
    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("存款金额只能为正数");
        } else {
            this.balance += amount;
        }
    }

    //取款
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("取款金额只能为正数");

        } else if (this.balance < amount) {
            System.out.println("余额不足");
            return false;
        } else {
            this.balance -= amount;
            return true;
        }
        return false;
    }

    //修改密码
    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public String toString() {
        // 格式：账号,密码,余额
        return accountNumber + "," + password + "," + balance + "," + email;
    }
}
