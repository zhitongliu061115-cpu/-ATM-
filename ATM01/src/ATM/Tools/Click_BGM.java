package ATM.Tools;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class Click_BGM {
    private static Clip clickClip;



    static {
        try {
            // 通过类加载器定位 click.wav
            URL url = Click_BGM.class.getResource("/ATM/resources/click.wav");
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);

            clickClip = AudioSystem.getClip();
            clickClip.open(ais);

            FloatControl volumeControl = (FloatControl) clickClip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(-10.0f); // 负数，调小音量（例如 -20f 更安静）

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | NullPointerException e) {
            // 如果加载失败，clickClip 保持 null，不影响程序其他逻辑
            clickClip = null;
            System.err.println("【SoundUtil】加载 click.wav 失败：" + e.getMessage());
        }
    }

    public static void playClick() {
        if (clickClip == null) {
            return;
        }
        if (clickClip.isRunning()) {
            clickClip.stop();
        }
        clickClip.setFramePosition(0);
        clickClip.start();
    }
}
