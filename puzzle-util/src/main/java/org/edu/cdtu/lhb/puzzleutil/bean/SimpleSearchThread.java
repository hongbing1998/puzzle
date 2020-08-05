package org.edu.cdtu.lhb.puzzleutil.bean;

import org.edu.cdtu.lhb.puzzleutil.util.PuzzleUtil;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * 简单难度搜索线程
 */
public class SimpleSearchThread extends Thread {
    private final int row;// 行数
    private final int col;// 列数
    private final Queue<String> frontQueue;// DBFS的正向工作队列
    private final Queue<String> backQueue;// DBFS的逆向工作队列
    private final Map<String, String> frontFathorMap;// 用于存储正向父状态的map
    private final Map<String, String> backFathorMap;// 用于存储逆向父状态的map

    public SimpleSearchThread(int row, int col, String initStatus, String finalStatus,
                              Map<String, String> frontFathorMap, Map<String, String> backFathorMap) {
        this.row = row;
        this.col = col;
        this.frontFathorMap = frontFathorMap;
        this.backFathorMap = backFathorMap;
        frontQueue = new LinkedList<>();
        backQueue = new LinkedList<>();
        frontQueue.offer(initStatus);// 将初始状态排入正向队列
        backQueue.offer(finalStatus);// 将最终状态排入逆向队列
    }

    @Override
    public void run() {
        // DBFS搜索步骤，各个步骤的关联通过map的键值对映射
        while (!PuzzleUtil.searched) {
            expand(frontQueue, frontFathorMap, backFathorMap);// 正向扩展
            expand(backQueue, backFathorMap, frontFathorMap);// 逆向扩展
        }
    }

    // 将状态进行扩展
    private void expand(Queue<String> currQueue, Map<String, String> thisMap, Map<String, String> anothorMap) {
        String currStatus = currQueue.poll();// 取出队首状态作为当前状态
        int currPos = currStatus.indexOf('\u0000');// 当前空白格的位置
        if (currPos - col >= 0) {
            mapping(currQueue, thisMap, anothorMap, exchange(currStatus, currPos - col), currStatus);// 上移
        }
        if ((currPos + 1) % col != 0) {
            mapping(currQueue, thisMap, anothorMap, exchange(currStatus, currPos + 1), currStatus);// 右移
        }
        if (currPos + col < row * col) {
            mapping(currQueue, thisMap, anothorMap, exchange(currStatus, currPos + col), currStatus);// 下移
        }
        if (currPos % col != 0) {
            mapping(currQueue, thisMap, anothorMap, exchange(currStatus, currPos - 1), currStatus);// 左移
        }
    }

    // 映射当前状态的信息（DBFS使用，未知最终状态）
    private void mapping(Queue<String> currQueue, Map<String, String> thisMap,
                         Map<String, String> anothorMap, String childStatus, String fathorStatus) {
        // 该子状态已在另一个map中被映射，即发生碰撞
        if (anothorMap.containsKey(childStatus)) {
            PuzzleUtil.searched = true;// 设置为已搜索到
            PuzzleUtil.crossStatus = childStatus;// 记录下这个交汇的状态
        }
        // 该子状态未被映射过，将之映射
        if (thisMap.putIfAbsent(childStatus, fathorStatus) == null) {
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
