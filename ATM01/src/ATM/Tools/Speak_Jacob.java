package ATM.Tools;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class Speak_Jacob {

//    public static void main(String[] args) {
//        // 要朗读的文字，支持中文和英文混合。例如：
//        String text = "您好，正在进行离线文字转语音演示。";
//
//        speak(text);
//    }

    /**
     * 使用 Windows SAPI 进行文字朗读，仅播放，不保存文件。
     *
     * 原理：通过 Jacob 调用 COM 对象 Sapi.SpVoice 来播放语音。
     *
     * @param text 要合成并播放的文字内容
     */
    public static void speak(String text) {
        new Thread(() -> {
            ActiveXComponent ax = null;
            Dispatch spVoice = null;
            try {
                // 1. 创建 Sapi.SpVoice COM 对象
                ax = new ActiveXComponent("Sapi.SpVoice");
                spVoice = ax.getObject();

                // 2。设置音量（范围 0-100），默认是 100
                ax.setProperty("Volume", new Variant(100));

                // 3. 设置语速,范围 -10 到 +10，负数是慢速，正数是加速
                ax.setProperty("Rate", new Variant(0));

                // 4. 直接调用 Speak 方法朗读文字，播放合成语音
                Dispatch.call(spVoice, "Speak", new Variant(text));

                // 5. 释放资源
                spVoice.safeRelease();
                ax.safeRelease();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
