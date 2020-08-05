package org.edu.cdtu.lhb.puzzleutil;

import java.util.stream.IntStream;

public class PuzzleUtil {
    public static final char SPACE = 0;
    public static final char FIRST = '0';
    public static final int TIMEOUT = 500;

    /**
     * 传入所有字符计算总分数
     */
    public static int calculateScore(char[] chars) {
        char currChar;
        int score = 0, size = (int) Math.sqrt(chars.length), rightPos, rowDis, colDis;

        for (int currPos = 0; currPos < chars.length; currPos++) {
            currChar = chars[currPos];
            if (currChar != SPACE) {
                rightPos = currChar - FIRST;
                rowDis = (rightPos - currPos) / size;
                colDis = rightPos % size - currPos % size;
                score += (int) Math.sqrt(rowDis * rowDis + colDis * colDis);
            }
        }

        return score;
    }

    /**
     * 获取最终的正确状态字符串
     */
    public static String getRightStr(int size) {
        StringBuilder builder = new StringBuilder();
        IntStream.range(FIRST, FIRST + size * size).forEach(n -> builder.append((char) n));
        builder.setCharAt(size * size - 1, SPACE);
        return builder.toString();
    }

    /**
     * 生成可解状态（随机交换除最后一位的所有位偶数次）
     */
    public static String getSolveAbleStr(int size) {
        StringBuilder builder = new StringBuilder(getRightStr(size));
        int exchageTime = (int) (5 + Math.random() * size * size) * 2;// 交换次数（保证是偶数）
        for (int i = 0; i < exchageTime; i++) {
            int index1 = 0, index2 = 0;
            // 生成两个不相等的随机下标（不包含最后一个）
            while (index1 == index2) {
                index1 = (int) (Math.random() * (size * size - 1));
                index2 = (int) (Math.random() * (size * size - 1));
            }
            char char1 = builder.charAt(index1);
            char char2 = builder.charAt(index2);
            builder.setCharAt(index1, char2);
            builder.setCharAt(index2, char1);
        }
        return builder.toString();
    }

    /**
     * 判断当前状态是否可解
     */
    public static boolean isSolveAble(String currStr) {
        boolean solveAble = true;
        char[] chars = currStr.replace(SPACE, (char) (FIRST + currStr.length() - 1)).toCharArray();
        for (int i = 0; i < chars.length - 1; i++) {
            for (int j = i + 1; j < chars.length; j++) {
                if (chars[i] > chars[j]) solveAble = !solveAble;
            }
        }
        return solveAble;
    }
}
