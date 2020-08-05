package org.edu.cdtu.lhb.puzzleutil;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Intelligetor {
    private State crossState;// 交汇时状态
    private boolean timeout;// 是否搜索超时
    private boolean searched;// 是否已搜索到
    private final State initState;// 初始状态
    private final State rightState;//正确状态
    private final Set<State> frontSet;// 正向已访问状态集合
    private final Set<State> backSet;// 正向已访问状态集合
    private final LinkedList<Integer> steps;// 搜索到的步骤列表
    private final Queue<State> frontQueue;// DBFS的正向工作队列
    private final Queue<State> backQueue;// DBFS的逆向工作队列

    public Intelligetor(String currStr, String rightStr) {
        frontSet = new HashSet<>();
        backSet = new HashSet<>();
        steps = new LinkedList<>();
        char[] currChars = currStr.toCharArray();
        char[] rightChars = rightStr.toCharArray();
        int size = (int) Math.sqrt(currStr.length());
        frontQueue = size <= 3 ? new LinkedList<>() : new PriorityQueue<>();
        backQueue = new LinkedList<>();// 3*3采用DBFS算法，更高阶的采用DBFS+A*算法
        initState = new State(currChars, currStr.indexOf(PuzzleUtil.SPACE), size, PuzzleUtil.calculateScore(currChars), null);
        rightState = new State(rightChars, rightStr.indexOf(PuzzleUtil.SPACE), size, 0, null);
    }

    public List<Integer> searchSteps() {
        State currState = initState;
        int size = (int) Math.sqrt(initState.toString().length()), timeoutTimes;
        for (timeoutTimes = 0; timeoutTimes < size * 4 - 5; timeoutTimes++) {
            search(currState);
            if (!timeout) break;

            System.out.printf("%s ──→ %s 第%d次搜索超时\n", initState, currState, (timeoutTimes + 1));
            // 搜索超时，清理容器，顺时针旋转一周后再重新搜索
            clearContainers();

            for (int i = 0; i < size - 1; i++) {
                assert currState != null;
                currState = currState.moveLeft();
            }
            for (int i = 0; i < size - 1; i++) {
                assert currState != null;
                currState = currState.moveUp();
            }
            for (int i = 0; i < size - 1; i++) {
                assert currState != null;
                currState = currState.moveRight();
            }
            for (int i = 0; i < size - 1; i++) {
                assert currState != null;
                currState = currState.moveDown();
            }
        }

        // 旋转（size * 4 - 5）次后又会恢复到初始状态，不再进行旋转，直接返回空步骤
        return timeoutTimes == size * 4 - 5 ? steps : buildSteps();
    }

    private void search(State currState) {
        searched = timeout = false;
        frontQueue.offer(currState);
        backQueue.offer(rightState);
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(() -> searched = timeout = true, PuzzleUtil.TIMEOUT, TimeUnit.MILLISECONDS);

        // 进行DBFS步骤搜索
        while (true) {
            // 从当前状态正向扩展
            currState = frontQueue.poll();
            assert currState != null;
            frontRecord(currState.moveUp());
            frontRecord(currState.moveRight());
            frontRecord(currState.moveDown());
            frontRecord(currState.moveLeft());

            if (searched) break;

            // 从最终状态逆向扩展
            currState = backQueue.poll();
            assert currState != null;
            backRecord(currState.moveUp());
            backRecord(currState.moveRight());
            backRecord(currState.moveDown());
            backRecord(currState.moveLeft());
        }

        executor.shutdownNow();// 超时时间内已搜索完毕，取消定时计划
    }

    /**
     * 记录正向扩展结果
     */
    private void frontRecord(State newState) {
        if (newState != null) {
            if (backSet.contains(newState)) {
                // 逆向结果集中已有记录，结束搜索，并记录该交叉的状态
                searched = true;
                crossState = newState;
            }

            if (frontSet.add(newState)) {
                frontQueue.offer(newState);
            }
        }
    }

    /**
     * 记录逆向扩展结果
     */
    private void backRecord(State newState) {
        if (newState != null) {
            if (backSet.add(newState)) {
                backQueue.offer(newState);
            }
        }
    }


    /**
     * 根据搜索结果构建步骤
     */
    private List<Integer> buildSteps() {
        State crossInFront = crossState, crossInBack = null;

        // 找到逆向结果中的最后状态
        for (State state : backSet) {
            if (state.equals(crossState)) {
                crossInBack = state;
                break;
            }
        }

        // 在正向结果中逆向拼接步骤
        for (State currState = crossInFront; currState != null; currState = currState.getParent()) {
            steps.addFirst(currState.getPos() + 1);
        }

        steps.removeFirst();// 移除第一步

        // 在逆向结果中正向拼接步骤
        assert crossInBack != null;
        for (State currState = crossInBack.getParent(); currState != null; currState = currState.getParent()) {
            steps.addLast(currState.getPos() + 1);
        }

        clearContainers();

        return steps;
    }

    /**
     * 清理所有容器
     */
    private void clearContainers() {
        frontSet.clear();
        backSet.clear();
        frontQueue.clear();
        backQueue.clear();
    }
}
