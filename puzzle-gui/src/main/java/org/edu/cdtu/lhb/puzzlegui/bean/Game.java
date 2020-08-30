package org.edu.cdtu.lhb.puzzlegui.bean;


import org.edu.cdtu.lhb.puzzlegui.listener.GridClickListener;
import org.edu.cdtu.lhb.puzzlegui.listener.MenuItemClickListener;
import org.edu.cdtu.lhb.puzzlegui.listener.TimeLabelActionListener;
import org.edu.cdtu.lhb.puzzleutil.PuzzleUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * 游戏类
 *
 * @author 李红兵
 */
public class Game {
    private Timer timer;// 计时器
    private int ROW = 3;// 初始总行数
    private int COL = 3;// 初始总列数
    private Grid blank = new Grid();// 空白格
    private Grid[][] grids;// 主界面显示的所有格子
    private Picture[] pictures;// 裁剪下来的所有的图片
    private BufferedImage picture = null;// 完整的图片
    private final JPanel back = new JPanel();// 中间后面层
    private final JPanel front = new JPanel();// 中间表面层
    private final JFrame mainFrame = new JFrame();// 主窗口
    private final JButton hint = new JButton();// 状态栏提示按钮
    private final JPanel top = new JPanel();// 上部状态栏承载面板
    private final JLabel time = new JLabel();// 状态栏时间显示标签
    private final JLabel step = new JLabel();// 状态栏难度显示标签
    private final JPanel center = new JPanel();// 中间主区域承载面板
    private final CardLayout card = new CardLayout();// 中间主区域卡片布局

    public Game() {
        super();
        init();
    }

    // 初始化游戏参数、界面等配置
    private void init() {
        mainFrame.setLayout(new BorderLayout());
        addMenu();
        addTop();
        addCenter();
        mainFrame.setTitle("智能拼图");
        mainFrame.setSize(500, 500 * 4 / 3);
        mainFrame.setResizable(false);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource("img/ping.png")));
    }

    /**
     * 添加菜单栏方法，包括添加控件和注册事件
     */
    private void addMenu() {
        // 创建一个菜单栏
        JMenuBar menu = new JMenuBar();

        // 创建菜单按钮
        JMenu choise = new JMenu("选项(C)");
        JMenu more = new JMenu("更多(M)");

        // 创建子菜单项
        JMenuItem three = new JMenuItem("简单(3×3)");
        JMenuItem four = new JMenuItem("普通(4×4)");
        JMenuItem five = new JMenuItem("困难(5×5)");
        JMenuItem sex = new JMenuItem("超难(6×6)");
        JMenuItem seven = new JMenuItem("超难(7×7)");
        JMenuItem eight = new JMenuItem("超难(8×8)");
        JMenuItem autoComplete = new JMenuItem("自动拼图");
        JMenuItem choosePic = new JMenuItem("选择图片");
        JMenuItem exit = new JMenuItem("退 出");
        JMenuItem help = new JMenuItem("帮助");
        JMenuItem advice = new JMenuItem("反馈");
        JMenuItem about = new JMenuItem("关于");

        // 创建子菜单项事件监听器
        MenuItemClickListener menuItemClick = new MenuItemClickListener();

        // 设置事件指令
        three.setActionCommand("three");
        four.setActionCommand("four");
        five.setActionCommand("five");
        sex.setActionCommand("sex");
        seven.setActionCommand("seven");
        eight.setActionCommand("eight");
        autoComplete.setActionCommand("autoComplete");
        choosePic.setActionCommand("choosePic");
        exit.setActionCommand("exit");
        help.setActionCommand("help");
        advice.setActionCommand("advice");
        about.setActionCommand("about");

        // 添加菜单按钮点击事件
        three.addActionListener(menuItemClick);
        four.addActionListener(menuItemClick);
        five.addActionListener(menuItemClick);
        sex.addActionListener(menuItemClick);
        seven.addActionListener(menuItemClick);
        eight.addActionListener(menuItemClick);
        autoComplete.addActionListener(menuItemClick);
        choosePic.addActionListener(menuItemClick);
        exit.addActionListener(menuItemClick);
        help.addActionListener(menuItemClick);
        advice.addActionListener(menuItemClick);
        about.addActionListener(menuItemClick);

        // 将菜单项添加到菜单按钮中
        choise.add(three);
        choise.add(four);
        choise.add(five);
        choise.add(sex);
        choise.add(seven);
        choise.add(eight);
        choise.add(autoComplete);
        choise.add(choosePic);
        choise.addSeparator();
        choise.add(exit);
        more.add(help);
        more.add(advice);
        more.add(about);

        // 将菜单按钮添加到菜单栏中
        menu.add(choise);
        menu.add(more);

        mainFrame.setJMenuBar(menu);
    }

    /**
     * 添加上部状态栏面板
     */
    private void addTop() {
        time.setText("时间：00:00");
        time.setPreferredSize(new Dimension(185, 40));
        time.setFont(new Font("宋体", Font.BOLD, 30));
        time.setForeground(Color.white);
        time.setBorder(BorderFactory.createLoweredBevelBorder());
        step.setText("步数：0");
        step.setPreferredSize(new Dimension(175, 40));
        step.setFont(new Font("宋体", Font.BOLD, 30));
        step.setForeground(Color.white);
        step.setBorder(BorderFactory.createLoweredBevelBorder());
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("img/smile.jpg")) {
            picture = ImageIO.read(Objects.requireNonNull(is));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image img = picture.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        hint.setIcon(new ImageIcon(img));
        hint.setToolTipText("按住看大图");
        hint.setBorder(BorderFactory.createRaisedBevelBorder());
        hint.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                card.show(center, "back");// 显示后面层面板
            }

            @Override
            public void mouseReleased(MouseEvent event) {
                card.show(center, "front");// 显示前面层面板
            }
        });
        timer = new Timer(1000, new TimeLabelActionListener());

        top.setBorder(BorderFactory.createEtchedBorder());
        top.setBackground(new Color(128, 128, 255));

        top.add(time);
        top.add(new JLabel("        "));
        top.add(hint);
        top.add(new JLabel("        "));
        top.add(step);

        mainFrame.add("North", top);
    }

    // 添加中间部分主区域面板，包括所有按钮控件和注册事件
    private void addCenter() {
        JLabel picLable = new JLabel();
        Image pic;
        front.setLayout(new GridLayout(COL, ROW));
        front.setBorder(BorderFactory.createRaisedBevelBorder());
        picLable.setBounds(2, 2, 489, 490 * 4 / 3 - 99);
        pic = picture.getScaledInstance(picLable.getWidth(), picLable.getHeight(), Image.SCALE_SMOOTH);
        picLable.setIcon(new ImageIcon(pic));
        back.setLayout(null);
        back.add(picLable);
        back.setBorder(BorderFactory.createRaisedBevelBorder());
        setGrids();// 设置所有格子的属性
        // 把格子依次添加到中间面板中
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                front.add(grids[i][j]);
            }
        }
        center.setLayout(card);
        center.add(front, "front");
        center.add(back, "back");
        mainFrame.add("Center", center);// 把中间面板添加到主窗口中
    }

    // 设置每一格的所有属性
    private void setGrids() {
        int rowRan;// 随机行标
        int colRan;// 随机列标
        int order = 0;// 序号自增器
        int width = 489 / COL;
        int height = (490 * 4 / 3 - 99) / ROW;
        grids = new Grid[COL][ROW];// 主界面显示的所有格子
        Picture tempPicture;// 用于交换的临时图片变量
        ImageIcon imgicon = new ImageIcon(Objects.requireNonNull(this.getClass().getClassLoader().getResource("img/blank.jpg")));
        Image blankImage = imgicon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        int times = ROW * COL % 2 == 0 ? ROW * COL - 2 : ROW * COL - 1;// 打乱次数，必须为偶数
        cutPicture();// 剪切图片
        // 初始化并设置每一格和每张图片的属性
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                grids[i][j] = new Grid();
                grids[i][j].setOrder(order);// 设置格子序号
                grids[i][j].setSize(width, height);// 设置格子大小
                grids[i][j].setPicture(pictures[order++]);// 设置格子图片
                grids[i][j].setDisabledIcon(new ImageIcon(blankImage));// 设置空白显示图片
                grids[i][j].addActionListener(new GridClickListener(grids[i][j]));// 添加格子按压事件
            }
        }

        // 随机打乱偶数次
        labe:
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                do {
                    rowRan = (int) (Math.random() * ROW);
                    colRan = (int) (Math.random() * COL);
                    // 排除当前格和最后一格
                } while ((rowRan == i || rowRan == ROW - 1) && (colRan == j || colRan == COL - 1));
                // 交换当前格与随机格的图片
                tempPicture = grids[i][j].getPicture();
                grids[i][j].setPicture(grids[rowRan][colRan].getPicture());
                grids[rowRan][colRan].setPicture(tempPicture);
                if (--times == 0) {
                    break labe;// 达到打乱次数结束打乱
                }
            }
        }

        grids[COL - 1][ROW - 1].setBlank(true);// 设置最后一格为空白块
        blank = grids[COL - 1][ROW - 1];// 把最后一格设置为主窗口类的空白块
    }

    // 切割图片，按游戏难度平均切割图片
    private void cutPicture() {
        int order = 0;
        int width = picture.getWidth() / COL;
        int height = picture.getHeight() / ROW;
        pictures = new Picture[ROW * COL];// 裁剪下来的所有图片

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                pictures[order] = new Picture();// 初始化
                pictures[order].setOrder(order);// 设置图片序号
                pictures[order++].setIcon(new ImageIcon(picture.getSubimage(width * j, height * i, width, height)));// 设置图片内容
            }
        }
    }

    /**
     * 重新开始游戏，进行一些初始化和清理操作
     *
     * @param row 重新开始游戏的难度
     * @param col 重新开始游戏的难度
     */
    public void restart(int row, int col) {
        ROW = row;
        COL = col;
        Image img = picture.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        hint.setIcon(new ImageIcon(img));// 重新设置提示按钮图标
        timer.stop();// 停止计时器
        GridClickListener.setFirstClick(true);// 设置为第一次点击状态
        front.removeAll();// 表面面板移除所有格子
        back.removeAll();// 后面面板清除提示图案
        center.remove(front);
        center.remove(back);
        mainFrame.remove(center);// 主窗口移除中间面板
        addCenter();// 重新添加中间面板
        time.setText("时间：00:00");// 重设时间显示框内容
        TimeLabelActionListener.setSeconds(0);// 重设时间标签事件监听器的时间为0
        step.setText("步数：0");// 重设步数显示框内容
        GridClickListener.setSteps(0);// 重设步数为0
        center.updateUI();// 更新中心区域
        System.gc();// 回收垃圾
    }

    public Grid[][] getGrids() {
        return grids;
    }

    public String getRightStr() {
        return PuzzleUtil.getRightStr(ROW);
    }

    public String getCurrStr() {
        StringBuilder status = new StringBuilder();
        for (Grid[] grid : grids) {
            for (Grid value : grid) {
                status.append((char) (PuzzleUtil.FIRST_CHARACTER + value.getPicture().getOrder()));
            }
        }
        status.setCharAt(ROW * COL - 1, PuzzleUtil.SPACE_CHARACTER);
        return status.toString();
    }

    public int getROW() {
        return ROW;
    }

    public int getCOL() {
        return COL;
    }

    public Grid getBlank() {
        return blank;
    }

    public Timer getTimer() {
        return timer;
    }

    public JLabel getTimeLable() {
        return time;
    }

    public void setBlank(Grid blank) {
        this.blank = blank;
    }

    public void setPicture(BufferedImage picture) {
        this.picture = picture;
    }

    public JLabel getStep() {
        return step;
    }

    public JPanel getCenter() {
        return center;
    }

    public CardLayout getCard() {
        return card;
    }

    public JFrame getMainFrame() {
        return mainFrame;
    }
}
