package org.edu.cdtu.puzzleutil;

import org.edu.cdtu.lhb.puzzleutil.util.PuzzleUtil;
import org.junit.Test;

import java.util.List;

public class PuzzleUtilModuleTest {
    @Test
    public void createStatusTest() {
        int row = 5, col = 5;
        String newStatus = createStatus(getFinalStatus(row, col), row, col);
        System.out.println("newStatus = " + newStatus);
    }

    @Test
    public void stepsSearchTest() throws InterruptedException {
        String initStatus = "abcd\u0000fgeh";
        int size = (int) Math.sqrt(initStatus.length());
        String finalStatus = getFinalStatus(size, size);
        MeasureTime.start();
        List<Integer> steps = PuzzleUtil.searchSteps(initStatus, finalStatus, size, size);
        MeasureTime.stop();
        System.out.printf("搜索完毕，一共%d步，耗时%d毫秒\n", steps.size(), MeasureTime.getMeasureResult());
        for (int i = 0; i < steps.size(); i++) {
            System.out.printf("%2d %s", steps.get(i), ((i + 1) % 40 == 0 ? "\n" : ""));
        }
    }

    @Test
    public void maxStepsTest() throws InterruptedException {
        int maxSteps = 0, row = 4, col = 4;
        String finalStatus = getFinalStatus(row, col), currStatus;
        for (int i = 0; i < 50; i++) {
            currStatus = createStatus(finalStatus, row, col);
            List<Integer> steps = PuzzleUtil.searchSteps(currStatus, finalStatus, row, col);
            if (steps.size() > maxSteps) {
                maxSteps = steps.size();
            }
        }
        System.out.println("maxSteps = " + maxSteps);
    }

    /**
     * 给定初始状态，生成可解状态（随机交换除最后一位的所有位偶数次）
     */
    private String createStatus(String status, int row, int col) {
        int exchageTime = (int) (5 + Math.random() * row * col) * 2;// 交换次数（保证是偶数）
        for (int i = 0; i < exchageTime; i++) {
            int index1 = 0, index2 = 0;
            // 生成两个不相等的随机下标（不包含最后一个）
            while (index1 == index2) {
                index1 = (int) (Math.random() * (row * col - 1));
                index2 = (int) (Math.random() * (row * col - 1));
            }
            char char1 = status.charAt(index1);
            char char2 = status.charAt(index2);
            status = status.replace(char1, '*').replace(char2, char1).replace('*', char2);
        }
        return status;
    }

    /**
     * 重复测试搜索到的步骤能否复原
     */
    @Test
    public void resultTest() throws InterruptedException {
        int row = 4, col = 4;
        String finalStatus = getFinalStatus(row, col);
        for (int i = 0; i < 100; i++) {
            String initStatus = createStatus(finalStatus, row, col);
            moveWithSteps(initStatus, finalStatus, row, col);
        }
    }

    /**
     * 按照搜索到的步骤移动
     */
    private void moveWithSteps(String initStatus, String finalStatus, int row, int col) throws InterruptedException {
        List<Integer> steps = PuzzleUtil.searchSteps(initStatus, finalStatus, row, col);
        String currStatus = initStatus;
        for (int toIndex : steps) {
            int currIndex = currStatus.indexOf('\u0000');
            char char1 = currStatus.charAt(currIndex);
            char char2 = currStatus.charAt(toIndex - 1);
            currStatus = currStatus.replace(char1, '*').replace(char2, char1).replace('*', char2);
        }
        if (!currStatus.equals(finalStatus)) {
            System.out.println("发生错误，初始状态：" + initStatus + "，当前状态：" + currStatus);
            System.out.println("步骤如下：");
            for (int i = 0; i < steps.size(); i++) {
                System.out.print(steps.get(i) + " " + ((i + 1) % 20 == 0 ? "\n" : ""));
            }
        }
    }

    /**
     * 按照规格获取最终状态
     */
    private String getFinalStatus(int row, int col) {
        String finalStatus = "abcdefghijklmnopqrstuvwxy";
        return finalStatus.substring(0, row * col - 1) + '\u0000';
    }
}

class MeasureTime {
    private static long startTime;
    private static long stopTime;

    static void start() {
        startTime = System.currentTimeMillis();
    }

    static void stop() {
        stopTime = System.currentTimeMillis();
    }

    static long getMeasureResult() {
        return stopTime - startTime;
    }
}
