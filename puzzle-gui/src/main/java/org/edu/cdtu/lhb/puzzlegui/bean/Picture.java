package org.edu.cdtu.lhb.puzzlegui.bean;

import javax.swing.*;

/**
 * 图片类，包括图片内容和图片的序号两个属性
 *
 * @author 李红兵
 */
public class Picture {
    private int order;// 图片序号
    private ImageIcon icon;// 图片内容

    Picture() {
        super();
    }

    public int getOrder() {
        return order;
    }

    void setOrder(int order) {
        this.order = order;
    }

    ImageIcon getIcon() {
        return icon;
    }

    void setIcon(ImageIcon icon) {
        this.icon = icon;
    }
}
