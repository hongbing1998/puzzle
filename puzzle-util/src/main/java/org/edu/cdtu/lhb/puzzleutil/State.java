package org.edu.cdtu.lhb.puzzleutil;

import java.util.Arrays;

/**
 * 状态类，内部使用字符数组保存拼图当前的状态
 * 主要使用四个方向的move方法将当前状态进行扩展
 *
 * @author 李红兵
 */
public final class State implements Comparable<State> {
    private final int pos;
    private final int size;
    private final int score;
    private final State parent;
    private final char[] chars;

    public State(char[] chars, int pos, int size, int score, State parent) {
        this.pos = pos;
        this.size = size;
        this.chars = chars;
        this.score = score;
        this.parent = parent;
    }

    /**
     * 向上移动，产生一个新状态，不能移动的情况返回自身
     */
    public State moveUp() {
        int toPos = pos - size;
        return toPos < 0 ? this : newState(toPos);
    }

    /**
     * 向上移动，产生一个新状态，不能移动的情况返回自身
     */
    public State moveRight() {
        int toPos = pos + 1;
        return toPos % size == 0 ? this : newState(toPos);
    }

    /**
     * 向下移动，产生一个新状态，不能移动的情况返回自身
     */
    public State moveDown() {
        int toPos = pos + size;
        return toPos > size * size - 1 ? this : newState(toPos);
    }

    /**
     * 向左移动，产生一个新状态，不能移动的情况返回自身
     */
    public State moveLeft() {
        int toPos = pos - 1;
        return (toPos + 1) % size == 0 ? this : newState(toPos);
    }

    @Override
    public String toString() { return new String(chars); }

    @Override
    public int compareTo(State state) { return score - state.score; }

    @Override
    public int hashCode() { return Arrays.hashCode(chars); }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        return Arrays.equals(chars, ((State) o).chars);
    }

    /**
     * 传入要将空白格移动到的位置，生成新的状态
     */
    private State newState(int toPos) {
        char[] newChars = chars.clone();
        char toChar = newChars[toPos];
        newChars[pos] = toChar;
        newChars[toPos] = PuzzleUtil.SPACE_CHARACTER;
        // 只移动了一格，新分数就等于原总分数减去被移动的字符移动前分数加上移动后的分数
        int newScore = score - calculateDis(toChar, toPos) + calculateDis(toChar, pos);
        return new State(newChars, toPos, size, newScore, this);
    }

    /**
     * 传入当前字符和当前位置，计算该字符与正确位置的直线距离
     */
    private int calculateDis(char currChar, int currPos) {
        int rightPos = currChar - PuzzleUtil.FIRST_CHARACTER;
        int rowDis = (rightPos - currPos) / size;
        int colDis = rightPos % size - currPos % size;
        return (int) Math.sqrt(rowDis * rowDis + colDis * colDis);
    }

    /**
     * 获取当前空白格位置
     */
    public int getPos() { return pos; }

    /**
     * 获取父状态
     */
    public State getParent() { return parent; }
}
