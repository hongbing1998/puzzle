package org.edu.cdtu.puzzleutil;

import org.edu.cdtu.lhb.puzzleutil.Intelligetor;
import org.edu.cdtu.lhb.puzzleutil.PuzzleUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PuzzleUtilModuleTest {
    @Test
    public void otherTest() {
    }

    @Test
    public void createStatusTest() {
        int size = 6;
        String rightStr = PuzzleUtil.getRightStr(size);
        String currStr = PuzzleUtil.getSolveAbleStr(size);
        assert PuzzleUtil.isSolveAble(currStr);

        printTitle("可解状态生成测试");
        System.out.printf("原状态：%s\t可解状态：%s\n", rightStr, currStr);
        printHr();
    }

    @Test
    public void singleSearchTest() {
        int size = 7;
        String rightStr = PuzzleUtil.getRightStr(size);
        String currStr = PuzzleUtil.getSolveAbleStr(size);
//      currStr = "0@MG1H:N593>LAD=?<EJRFCBP7Q4K628O;I" + PuzzleUtil.SPACE;
        Intelligetor intelligetor = new Intelligetor(currStr, rightStr);

        MeasureTime.start();
        List<Integer> steps = intelligetor.searchSteps();
        MeasureTime.stop();

        printTitle("步骤搜索测试");
        System.out.printf("规格：%d×%d\n", size, size);
        System.out.printf("初始状态：%s\t\t最终状态：%s\t\t一共%d步\t\t耗时%dms\n", currStr, rightStr, steps.size(), MeasureTime.getMeasureResult());
        for (int i = 0; i < steps.size(); i++) {
            System.out.printf("%2d%s", steps.get(i), ((i + 1) % 42 == 0 ? "\n" : " "));
        }
        System.out.println();
        printHr();
    }

    @Test
    public void multiSearchTest() {
        int size = 5;
        String rightStr = PuzzleUtil.getRightStr(size);
        printTitle("批量搜索测试");
        for (int i = 1; i <= 3000; i++) {
            new Intelligetor(PuzzleUtil.getSolveAbleStr(size), rightStr).searchSteps();
        }
        printHr();
    }

    @Test
    public void statistic() {
        statistic(3, 13);
        statistic(4, 12);
        statistic(5, 11);
        statistic(6, 10);
        printHr();
    }


    private void statistic(int size, int number) {
        ArrayList<Long> times = new ArrayList<>(number);
        ArrayList<Integer> steps = new ArrayList<>(number);
        ArrayList<Intelligetor> intelligetors = new ArrayList<>();

        for (int i = 0; i < number; i++) {
            intelligetors.add(new Intelligetor(PuzzleUtil.getSolveAbleStr(size), PuzzleUtil.getRightStr(size)));
        }

        printTitle("时间步数统计");
        for (Intelligetor intelligetor : intelligetors) {
            MeasureTime.start();
            steps.add(intelligetor.searchSteps().size());
            MeasureTime.stop();
            times.add(MeasureTime.getMeasureResult());
        }

        long minTime = times.stream().min(Long::compareTo).get();
        long maxTime = times.stream().max(Long::compareTo).get();
        double averageTime = times.stream().mapToLong(Long::longValue).average().getAsDouble();
        int minStep = steps.stream().min(Integer::compareTo).get();
        int maxStep = steps.stream().max(Integer::compareTo).get();
        double averageStep = steps.stream().mapToInt(Integer::intValue).average().getAsDouble();

        System.out.printf("规格：%d×%d\t共搜索%d次", size, size, times.size());
        System.out.printf("\n最短%dms\t\t\t最长%dms\t\t\t平均%fms", minTime, maxTime, averageTime);
        System.out.printf("\n最短%d步\t\t\t最长%d步\t\t\t平均%f步\n", minStep, maxStep, averageStep);
    }


    private void printTitle(String title) {
        printHr();
        System.out.println(title);
    }

    private void printHr() {
        System.out.println("------------------------------------------------------------------------------------------------------------------------------");
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
