package org.edu.cdtu.lhb.puzzleutil.util;

import java.util.stream.IntStream;

/**
 * 拼图工具类，针对系统提供了一些通用方法，以及一些常量的维护
 *
 * @author 李红兵
 */
public class PuzzleUtil {
    /*状态字符串空白格字符*/
    public static final char SPACE_CHARACTER = 0;
    /*状态字符串的起始字符*/
    public static final char FIRST_CHARACTER = '0';
    /*搜索超时时间*/
    public static final int SEARCH_TIMEOUT = 300;
    /*最大超时次数*/
    public static final int MAX_TIMEOUT_TIMES = 10;

    /**
     * 传入所有字符计算总分数
     */
    public static int calculateScore(char[] chars) {
        int score = 0, size = (int) Math.sqrt(chars.length), rightPos, rowDis, colDis, currChar;
        for (int currPos = 0; currPos < chars.length; currPos++) {
            currChar = chars[currPos];
            if (currChar != SPACE_CHARACTER) {
                rightPos = currChar - FIRST_CHARACTER;
                rowDis = (rightPos - currPos) / size;
                colDis = rightPos % size - currPos % size;
                score += (int) Math.sqrt(rowDis * rowDis + colDis * colDis);
            }
        }

        return score;
    }

    /**
     * 根据规格获取最终的正确状态字符串
     */
    public static String getRightStr(int size) {
        StringBuilder builder = new StringBuilder();
        IntStream.range(FIRST_CHARACTER, FIRST_CHARACTER + size * size).forEach(n -> builder.append((char) n));
        builder.setCharAt(size * size - 1, SPACE_CHARACTER);
        return builder.toString();
    }

    /**
     * 根据当前状态字符串获取最终的正确状态字符串
     */
    public static String getRightStr(String currStr) {
        int size = (int) Math.sqrt(currStr.length());
        return getRightStr(size);
    }


    /**
     * 生成可解状态（随机交换除最后一位的所有位偶数次）
     */
    public static String getSolveAbleStr(int size) {
        StringBuilder builder = new StringBuilder(getRightStr(size));
        int exchageTime = (int) (5 + Math.random() * size * size) * 2;
        // 随机交换除空白格的其他格偶数次
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
        char[] chars = currStr.replace(SPACE_CHARACTER, (char) (FIRST_CHARACTER + currStr.length() - 1)).toCharArray();
        for (int i = 0; i < chars.length - 1; i++) {
            for (int j = i + 1; j < chars.length; j++) {
                if (chars[i] > chars[j]) { solveAble = !solveAble; }
            }
        }
        return solveAble;
    }

    /**
     * 根据规格获取Set容器初始容量
     */
    public static int getSetInitialCapacity(int size) {
        switch (size) {
            case 3:
                return 1450;
            case 4:
                return 3250;
            case 5:
                return 20000;
            case 6:
                return 130000;
            default:
                return 200000;
        }
    }

    /**
     * 根据规格获取Queue容器初始容量
     */
    public static int getQueueInitialCapacity(int size) {
        switch (size) {
            case 3:
                return 550;
            case 4:
                return 1650;
            case 5:
                return 11000;
            case 6:
                return 75000;
            default:
                return 100000;
        }
    }
}
