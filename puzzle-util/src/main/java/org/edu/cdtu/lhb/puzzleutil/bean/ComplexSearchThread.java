package org.edu.cdtu.lhb.puzzleutil.bean;

import org.edu.cdtu.lhb.puzzleutil.util.PuzzleUtil;

import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * 复杂难度搜索线程
 */
public class ComplexSearchThread extends Thread {
    private final int row;// 行数
    private final int col;// 列数
    private final Queue<String> currQueue;// DBFS的工作队列
    private final Map<String, String> thisMap;// 本线程用于存储父状态的map
    private final Map<String, String> anothorMap;// 另一个线程用于存储父状态的map
    private final HashSet<String> visitedStatuses;// 已访问过的状态哈希集合，用于判重

    public ComplexSearchThread(int row, int col, String initStatus, Map<String, String> thisMap, Map<String, String> anothorMap) {
        this.row = row;
        this.col = col;
        this.thisMap = thisMap;
        this.anothorMap = anothorMap;
        visitedStatuses = new HashSet<>();
        currQueue = new PriorityQueue<>(new StatusComparator(row, col));
        currQueue.offer(initStatus);// 将初始状态排入队列
    }

    @Override
    public void run() {
        // DBFS搜索步骤，各个步骤的关联通过map的键值对映射
        while (!PuzzleUtil.searched) {
            expand();// 取出队首状态扩展出子状态
        }
        PuzzleUtil.cdl.countDown();// 该线程结束，计数器减1
    }

    // 取出队首状态扩展出子状态
    private void expand() {
        String currStatus = currQueue.poll();// 取出队首状态作为当前状态;
        visitedStatuses.add(currStatus);// 将当前状态放入已访问过的Hash集合中
        int currPos = currStatus.indexOf('\u0000');// 当前空白格的位置
        if (currPos + col < row * col) {
            mapping(exchange(currStatus, currPos + col), currStatus);// 下移
        }
        if ((currPos + 1) % col != 0) {
            mapping(exchange(currStatus, currPos + 1), currStatus);// 右移
        }
        if (currPos - col >= 0) {
            mapping(exchange(currStatus, currPos - col), currStatus);// 上移
        }
        if (currPos % col != 0) {
            mapping(exchange(currStatus, currPos - 1), currStatus);// 左移
        }
    }

    // 映射当前状态的信息
    private void mapping(String childStatus, String fathorStatus) {
        // 该子状态已在另一个map中被映射，即发生碰撞
        if (anothorMap.containsKey(childStatus)) {
            // 防止被修改两次，导致值被覆盖
            if(!PuzzleUtil.searched) {
                PuzzleUtil.searched = true;// 设置为已搜索到
                PuzzleUtil.crossStatus = childStatus;// 记录下这个交汇的状态
            }
        }
        if (!visitedStatuses.contains(childStatus)) {
            thisMap.put(childStatus, fathorStatus);// 把当前状态作为子状态的父状态
            currQueue.offer(childStatus);// 将子状态排入工作队列
        }
    }

    // 交换源位置和目的位置的字符，需要用到另一个字符作为介质
    private String exchange(String currStatus, int desPos) {
        char desChar = currStatus.charAt(desPos);
        return currStatus.replace('\u0000', '*')
                .replace(desChar, '\u0000')
                .replace('*', desChar);
    }
}
