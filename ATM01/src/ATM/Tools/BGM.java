package ATM.Tools;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class BGM {
    private static Clip clip;

    static {
        try {

            URL wavUrl = BGM.class.getResource("/ATM/resources/background.wav");
            if (wavUrl == null) {
                System.err.println("找不到 /ATM/resources/background.wav （getResource 返回 null）");
            } else {
                System.out.println("成功找到背景音乐background.wav： " + wavUrl);
            }


            // 1. 从 classpath 加载 background.wav
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(wavUrl);

            // 2. 获取一个可播放的 Clip
            clip = AudioSystem.getClip();
            // 3. 把音频流打开到 Clip 中
            clip.open(audioStream);
//调节音量
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(-10.0f); // 负数，调小音量


            // 4. 循环播放
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            // 5. 立即开始播放
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            // 加载失败时打印错误信息（也可以选择弹窗提示）
            System.err.println("无法加载背景音乐: " + e.getMessage());
            clip = null;
        }
    }

    public static void init() {
    }



}