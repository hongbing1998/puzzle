package org.edu.cdtu.lhb.puzzlegui.listener;


import org.edu.cdtu.lhb.puzzlegui.Puzzle;
import org.edu.cdtu.lhb.puzzlegui.bean.Grid;
import org.edu.cdtu.lhb.puzzleutil.Intelligetor;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * 菜单选项事件监听类，根据菜单选项的命令执行对应的操作
 *
 * @author 李红兵
 */
public class MenuItemClickListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "three":
                Puzzle.game.restart(3, 3);
                break;
            case "four":
                Puzzle.game.restart(4, 4);
                break;
            case "five":
                Puzzle.game.restart(5, 5);
                break;
            case "sex":
                Puzzle.game.restart(6, 6);
                break;
            case "seven":
                Puzzle.game.restart(7, 7);
                break;
            case "eight":
                Puzzle.game.restart(8, 8);
                break;
            case "choosePic":
                choosePic();
                break;
            case "autoComplete":
                autoComplete();
                break;
            case "exit":
                System.exit(0);
                break;
            case "help":
                String helpText = "点击空白块相邻的块，去把需要移动的块移动到空白块的位置";
                JOptionPane.showMessageDialog(Puzzle.game.getCenter(), helpText, "帮助信息", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "advice":
                StringBuilder adviceText = new StringBuilder();
                adviceText.append("有什么好的建议或者遇到什么问题请及时反馈给我\n");
                adviceText.append("QQ：1227911097\n");
                adviceText.append("感谢支持\n");
                JOptionPane.showMessageDialog(Puzzle.game.getCenter(), adviceText, "反馈建议", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "about":
                StringBuilder aboutText = new StringBuilder();
                aboutText.append("作者：低调。\n");
                aboutText.append("名称：智能拼图\n");
                aboutText.append("版本：1.1.12\n");
                JOptionPane.showMessageDialog(Puzzle.game.getCenter(), aboutText, "关于", JOptionPane.INFORMATION_MESSAGE);
                break;
            default:
                break;
        }
    }

    private void autoComplete() {
        int col = Puzzle.game.getCOL();
        String currStr = Puzzle.game.getCurrStr();
        // 新开线程执行搜索步骤并按步骤拼图
        new Thread(() -> {
            Intelligetor intelligetor = new Intelligetor(currStr);
            List<Integer> steps = intelligetor.searchSteps();
            Grid[][] grids = Puzzle.game.getGrids();
            assert steps != null;
            for (int step : steps) {
                int x = (step - 1) / col;
                int y = (step - 1) % col;
                grids[x][y].doClick();
            }
        }).start();
    }

    // 选择图片
    private void choosePic() {
        BufferedImage picture = null;
        JFileChooser picChooser = new JFileChooser("/");// 到当前根目录
        picChooser.setDialogTitle("选择一张图片");
        picChooser.setAcceptAllFileFilterUsed(false);// 禁止选其他格式文件
        // 文件筛选器
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().endsWith(".jpg") || file.getName().endsWith(".png") || file.isDirectory();
            }

            @Override
            public String getDescription() {
                return "图片(*.jpg;*.png)";
            }
        };
        picChooser.setFileFilter(filter);// 绑定文件筛选器
        picChooser.showOpenDialog(null);
        if (picChooser.getSelectedFile() == null) {
            return;// 取消选择图片后不执行后续操作
        }
        try {
            picture = ImageIO.read(picChooser.getSelectedFile());
        } catch (IOException e) {
            e.printStackTrace();
            try {
                picture = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("img/smile.jpg")));
                JOptionPane.showMessageDialog(Puzzle.game.getCenter(), "选择图片失败，已设为默认图片！", "错误", JOptionPane.ERROR_MESSAGE);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        Puzzle.game.setPicture(picture);// 修改游戏图片为选择的图片
        Puzzle.game.restart(Puzzle.game.getROW(), Puzzle.game.getCOL());// 以当前难度重新开始游戏
    }
}