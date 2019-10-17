package org.edu.cdtu.lhb.puzzleutil.bean;

import java.util.Comparator;

public class StatusComparator implements Comparator<String> {
    private int row, col;// 拼图的行数和列数

    public StatusComparator(int row, int col) {
        this.row = row;
        this.col = col;
    }

    // 权值越小优先出列
    @Override
    public int compare(String str1, String str2) {
        return getScore(str1) - getScore(str2);
    }

    // 获取当前状态的权值（所有格的哈曼顿距离之和）
    private int getScore(String str) {
        int score = 0, index = 0, num, numX, numY;
        char[] chars1 = str.toCharArray();// 将.替换成最后一个字符并转成数组
        // x，y代表该格当前的位置
        for (int x = 0; x < row; x++) {
            for (int y = 0; y < col; y++) {
                num = chars1[index++] - 'a';// 该格的数字索引值
                numX = num / col;// 该格应该所在的行号
                numY = num % col;// 该格应该所在的列号
                score += (int) Math.sqrt((numX - x) * (numX - x) + (numY - y) * (numY - y));
            }
        }
        return score;
    }
}
