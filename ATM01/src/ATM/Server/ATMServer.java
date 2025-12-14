package ATM.Server;

import ATM.Server.ServerUI.MainFrame;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ATMServer {
    private static final int PORT = 8083;
    private static final int THREADS = 10;
    private static MainFrame serverUI;
    private static AtomicInteger clientIdCounter = new AtomicInteger(1);



    public static void main(String[] args) {
        service service=new service();
        serverUI = new MainFrame(service);


        serverUI.logMessage("=== ATM服务器启动 ===");
        serverUI.logMessage("端口："+PORT+"  等待客户端连接 ");

        ExecutorService pool = Executors.newFixedThreadPool(THREADS);
        ServerSocket server= null;

        try {
            server = new ServerSocket(PORT);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        while (true) {
            try {
                Socket socket = server.accept();
                String clientIP = socket.getInetAddress().getHostAddress();
                String clientID="Client_0"+clientIdCounter.getAndIncrement();
                serverUI.logMessage("新客户连接：: " + clientIP);

                pool.execute(new clientHandler(socket, service,serverUI,clientID));
            } catch (IOException e) {
                serverUI.logMessage("客户端连接失败"+e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }
}
