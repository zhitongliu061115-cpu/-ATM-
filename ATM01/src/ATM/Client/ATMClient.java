package ATM.Client;

import ATM.Client.ClientUI.MainFrame;

import java.io.IOException;
import java.net.Socket;

public class ATMClient {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1",8083);
            ATMService service = new ATMService(socket);

            MainFrame mainFrame = new MainFrame(service);
            mainFrame.setVisible(true);

        } catch (IOException e) {
            System.err.println("连接服务器失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}










