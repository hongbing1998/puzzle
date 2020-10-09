package org.edu.cdtu.lhb.puzzleutil;

import org.edu.cdtu.lhb.puzzleutil.bean.State;
import org.edu.cdtu.lhb.puzzleutil.util.PuzzleUtil;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author 李红兵
 */
public class Intelligetor {
    private State crossState;
    private boolean searched;
    private boolean timeouted;
    private final State initState;
    private final State rightState;
    private final Set<State> frontSet;
    private final Set<State> backSet;
    private final LinkedList<Integer> steps;
    private final Queue<State> frontQueue;
    private final Queue<State> backQueue;

    public Intelligetor(String currStr) {
        steps = new LinkedList<>();
        String rightStr = PuzzleUtil.getRightStr(currStr);
        char[] currChars = currStr.toCharArray();
        char[] rightChars = rightStr.toCharArray();
        int size = (int) Math.sqrt(currStr.length());
        frontSet = new HashSet<>(PuzzleUtil.getSetInitialCapacity(size));
        backSet = new HashSet<>(PuzzleUtil.getSetInitialCapacity(size));
        // 3*3采用DBFS算法，更高阶的采用DBFS+A*算法
        backQueue = new LinkedList<>();
        frontQueue = size < 4 ? new LinkedList<>() : new PriorityQueue<>(PuzzleUtil.getQueueInitialCapacity(size));
        initState = new State(currChars, currStr.indexOf(PuzzleUtil.SPACE_CHARACTER), size, PuzzleUtil.calculateScore(currChars), null);
        rightState = new State(rightChars, rightStr.indexOf(PuzzleUtil.SPACE_CHARACTER), size, 0, null);
    }

    /**
     * 调用此方法获取拼图的还原步骤
     */
    public List<Integer> searchSteps() {
        State currState = initState;
        for (int i = 0; i < PuzzleUtil.MAX_TIMEOUT_TIMES; i++) {
            search(currState);
            if (!timeouted) { return buildSteps(); }

            // 搜索超时，随机移动后重新搜索
            System.out.printf("%s ──→ %s 第%d次搜索超时\n", initState, currState, (i + 1));
            clearContainers();

            currState = randomMove(currState);
        }

        // 超过最大超时次数，放弃搜索，直接返回空步骤
        return steps;
    }

    /**
     * 随机移动
     */
    private State randomMove(State currState) {
        Random random = new Random();
        int currDire, prevDire = -1;
        int moveTimes = 17 + random.nextInt(17);
        for (int i = 0; i < moveTimes; i++) {
            currDire = random.nextInt(40) % 4;
            if (Math.abs(currDire - prevDire) == 2) { continue; }
            switch ((prevDire = currDire)) {
                case 0:
                    currState = currState.moveUp();
                    break;
                case 1:
                    currState = currState.moveRight();
                    break;
                case 2:
                    currState = currState.moveDown();
                    break;
                case 3:
                    currState = currState.moveLeft();
                    break;
                default:
            }
        }

        return currState;
    }

    /**
     * 从给定的当前状态开始搜索直到搜索到最终正确状态或者搜索超时为止
     */
    private void search(State currState) {
        searched = timeouted = false;
        frontQueue.offer(currState);
        backQueue.offer(rightState);
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(() -> searched = timeouted = true, PuzzleUtil.SEARCH_TIMEOUT, TimeUnit.MILLISECONDS);

        // 进行DBFS步骤搜索
        while (true) {
            // 从当前状态正向扩展
            currState = frontQueue.poll();
            assert currState != null;
            frontRecord(currState.moveUp());
            frontRecord(currState.moveRight());
            frontRecord(currState.moveDown());
            frontRecord(currState.moveLeft());

            if (searched) { break; }

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
        if (backSet.contains(newState)) {
            // 逆向结果集中已有记录，结束搜索，并记录该交叉的状态
            searched = true;
            crossState = newState;
        }

        if (frontSet.add(newState)) {
            frontQueue.offer(newState);
        }
    }

    /**
     * 记录逆向扩展结果
     */
    private void backRecord(State newState) {
        if (backSet.add(newState)) {
            backQueue.offer(newState);
        }
    }


    /**
     * 根据搜索结果构建步骤
     */
    private List<Integer> buildSteps() {
        State crossInFront = crossState;

        // 找到逆向结果中的交叉状态
        Map<String, State> stateMap = backSet.stream().collect(Collectors.toMap(State::toString, s -> s, (s1, s2) -> s1));
        State crossInBack = stateMap.get(crossState.toString());

        // 在正向结果中逆向拼接步骤
        for (State currState = crossInFront; currState != null; currState = currState.getParent()) {
            steps.addFirst(currState.getPos() + 1);
        }

        // 第一步是最初始状态，不作为步骤
        steps.removeFirst();

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
