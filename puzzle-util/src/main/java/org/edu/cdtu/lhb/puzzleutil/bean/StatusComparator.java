package org.edu.cdtu.lhb.puzzleutil.bean;

import java.util.Comparator;

public class StatusComparator implements Comparator<String> {
    private final int row;
    private final int col;// 拼图的行数和列数

    public StatusComparator(int row, int col) {
        this.row = row;
        this.col = col;
    }

    // 权值越小优先出列
    @Override
    public int compare(String str1, String str2) {
        return getScore(str1) - getScore(str2);
    }

    // 获取当前状态的权值（所有格现在的位置与正确位置的直线距离之和）
    private int getScore(String str) {
        char[] chars = str.toCharArray();
        int score = 0, index = 0, num, numX, numY;
        // x，y代表该格当前的坐标
        for (int x = 0; x < row; x++) {
            for (int y = 0; y < col; y++) {
                num = chars[index++] - 'a';// 计算出当前是第几格
                numX = num / col;// 该格应该所在的x坐标
                numY = num % col;// 该格应该所在的y坐标
                score += (int) Math.sqrt((numX - x) * (numX - x) + (numY - y) * (numY - y));
//                score += numX - x + numY - y;
            }
        }
        return score;
    }
}
