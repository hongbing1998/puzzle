package org.edu.cdtu.lhb.puzzleutil.util;

import org.edu.cdtu.lhb.puzzleutil.bean.ComplexSearchThread;
import org.edu.cdtu.lhb.puzzleutil.bean.SimpleSearchThread;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class PuzzleUtil {
    public static String crossStatus;// 交汇时的状态
    public static Boolean searched;// 是否搜索到标志
    public static CountDownLatch cdl;// 执行中线程计数器
    private static Map<String, String> frontFathorMap;// 用于存储正向父状态的map
    private static Map<String, String> backFathorMap;// 用于存储父反向状态的map

    /**
     * 获取复原步骤，线程安全
     * 状态用小写字母字符串按顺序表示，空白格用\u0000（ASCII码为0的字符）表示，
     * 返回的步骤是一个数字列表，表示点击第几格，下标从1开始
     *
     * @param initStatus  初始状态
     * @param finalStatus 最终状态
     * @param row         行数
     * @param col         列数
     */
    public static synchronized List<Integer> searchSteps(String initStatus, String finalStatus, int row, int col) throws InterruptedException {
        searched = false;// 初始化是否搜索到标志
        frontFathorMap = new HashMap<>();
        backFathorMap = new HashMap<>();
        if (row * col <= 9) {
            Thread searchThread = new SimpleSearchThread(row, col, initStatus, finalStatus, frontFathorMap, backFathorMap);
            searchThread.start();// 开始单线程双向广搜
            searchThread.join();// 单线程创建一个线程的计数器也行，这里直接将搜索线程与主线程（依赖线程）合并
        } else {
            cdl = new CountDownLatch(2);// 创建两个线程的计数器
            Thread frontSearchThread = new ComplexSearchThread(row, col, initStatus, frontFathorMap, backFathorMap);
            Thread backSearchThread = new ComplexSearchThread(row, col, finalStatus, backFathorMap, frontFathorMap);
            frontSearchThread.start();// 开始正向搜索线程
            backSearchThread.start();// 开始逆向搜索线程
            // 阻塞等待所有线程执行完毕或者超时
            if (!cdl.await(15, TimeUnit.SECONDS)) {
                searched = true;// 超时，手动设为已搜索到，以达到终止线程的目的
                return new ArrayList<>();// 超时，停止运行，返回空步骤
            }
        }
        return buildSteps(initStatus, finalStatus);// 返回构建好的步骤列表
    }

    // 根据双向广搜搜索结果构建步骤
    private static List<Integer> buildSteps(String initStatus, String finalStatus) {
        LinkedList<Integer> steps = new LinkedList<>();// 步骤列表
        String currStep = crossStatus;// 当前处于交汇处
        // 在正向搜索结果中拼接从交汇处到初始状态的步骤
        // 但需要的是从初始状态到交汇处的步骤，所以每次步骤填充到步骤队列头
        while (!currStep.equals(initStatus)) {
            steps.addFirst(currStep.indexOf('\u0000') + 1);// 将步骤序号填充到链表头
            currStep = frontFathorMap.get(currStep);// 在fathorMap中一级一级往前找
        }
        currStep = crossStatus;// 回到交汇处
        // 在逆向搜索结果中拼接从交汇处到最终状态的步骤，每一步直接填充到步骤队列尾
        while (!currStep.equals(finalStatus)) {
            currStep = backFathorMap.get(currStep);// 在fathorMap中一级一级往前找
            steps.addLast(currStep.indexOf('\u0000') + 1);// 将步骤序号填充到链表尾
        }
        // 如果正向线程太快（逆向线程抢占不到CPU），已经搜索出了最终状态，这时候最后两步就是重复的，需要移除
        if (steps.size() > 2 && steps.get(steps.size() - 3) == finalStatus.indexOf('\u0000') + 1) {
            steps.removeLast();
            steps.removeLast();
        }
        frontFathorMap.clear();// 用完清空容器
        backFathorMap.clear();
        frontFathorMap = backFathorMap = null;
        return steps;
    }
}
