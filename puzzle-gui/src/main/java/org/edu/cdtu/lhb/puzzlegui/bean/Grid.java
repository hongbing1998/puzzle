package org.edu.cdtu.lhb.puzzlegui.bean;

import javax.swing.*;
import java.awt.*;

/**
 * 格子类，继承JButton，每一格都是一个按钮，还有其他一些自定义的属性：每一格的序号、是否是空白块标志、本格显示的图片
 *
 * @author 李红兵
 */
public class Grid extends JButton {
    private static final long serialVersionUID = 1L;
    private int order;// 序号
	private Picture picture;// 该格的图片

    Grid() {
        super();
    }

    public int getOrder() {
        return order;
    }

    void setOrder(int order) {
        this.order = order;
    }

    public void setBlank(boolean blank) {
		// 空白块
		setEnabled(!blank);
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
        Image img = picture.getIcon().getImage();
        // 缩放图片，与格子大小一致
        img = img.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
        setIcon(new ImageIcon(img));
    }
}
