package ATM.Tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

public class Logger {

    private static PrintWriter writer;

    static {
        try {
            File dir = new File("logs");
            if (!dir.exists()) dir.mkdir();
            writer = new PrintWriter(new FileWriter("logs/server.log", true), true);
        } catch (IOException e) {
            System.err.println("无法初始化日志文件: " + e.getMessage());
        }
    }

    public static void log(String msg) {
        if (writer == null) return;
        String time = getCurrentTime();
        writer.println("[" + time + "] " + msg);
    }




    public static String getCurrentTime() {
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
