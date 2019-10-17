package org.edu.cdtu.lhb.puzzlegui.listener;

import org.edu.cdtu.lhb.puzzlegui.Puzzle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 时间标签事件监听类，每隔一秒重设标签文本，达到计时的效果
 *
 * @author 李红兵
 */
public class TimeLabelActionListener implements ActionListener {
    private static int seconds;// 秒
    private static int minutes;// 分钟

    @Override
    public void actionPerformed(ActionEvent e) {
        // 分钟和秒不足10空位补0
        minutes = ++seconds / 60 % 60;
        // 表示时间显示框的文本内容
        String text = "时间：" + (minutes < 10 ? ("0" + minutes) : minutes) + ":";
        text += seconds % 60 < 10 ? ("0" + seconds % 60) : seconds % 60;
        Puzzle.game.getTimeLable().setText(text);// 设置时间框的文本内容
    }

    public static void setSeconds(int Seconds) {
        seconds = Seconds;
    }

    static int getSeconds() {
        return seconds % 60;
    }

    static int getMinutes() {
        return minutes;
    }
}
