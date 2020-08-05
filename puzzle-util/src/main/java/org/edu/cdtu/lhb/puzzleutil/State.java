package org.edu.cdtu.lhb.puzzleutil;

import java.util.Arrays;

public final class State implements Comparable<State> {
    private final int size;// 规格
    private final int score;// 权值
    private final int pos;// 空白格位置
    private final State parent;// 父状态
    private final char[] chars;// 所有字符

    public State(char[] chars, int pos, int size, int score, State parent) {
        this.pos = pos;
        this.size = size;
        this.chars = chars;
        this.score = score;
        this.parent = parent;
    }

    @Override
    public String toString() {
        return new String(chars);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(chars);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Arrays.equals(chars, ((State) o).chars);
    }

    @Override
    public int compareTo(State state) {
        return score - state.score;// 权值小的优先出列
    }

    public State moveUp() {
        int toPos = pos - size;
        return toPos > -1 ? newState(toPos) : null;
    }

    public State moveRight() {
        int toPos = pos + 1;
        return toPos % size != 0 ? newState(toPos) : null;
    }

    public State moveDown() {
        int toPos = pos + size;
        return toPos < size * size ? newState(toPos) : null;
    }

    public State moveLeft() {
        int toPos = pos - 1;
        return (toPos + 1) % size != 0 ? newState(toPos) : null;
    }

    public int getPos() {
        return pos;
    }

    public State getParent() {
        return parent;
    }


    /**
     * 传入当前字符和当前位置，计算该字符与正确位置的直线距离
     */
    private int calculateDis(char currChar, int currPos) {
        int rightPos = currChar - PuzzleUtil.FIRST;
        int rowDis = (rightPos - currPos) / size;// 该格应该所在的行减去当前行
        int colDis = rightPos % size - currPos % size;// 该格应该所在的列减去当前列
        return (int) Math.sqrt(rowDis * rowDis + colDis * colDis);
    }

    /**
     * 传入要将空白格移动到的位置，生成新的状态
     */
    private State newState(int toPos) {
        char[] newChars = chars.clone();// 拷贝所有字符
        char toChar = newChars[toPos];// 获取将要被替换掉的字符
        newChars[pos] = toChar;// 把将要被替换的字符移到当前空白位置
        newChars[toPos] = PuzzleUtil.SPACE;// 将目标字符替换成空白

        // 只移动了一格，新分数就等于原总分数减去被移动的字符移动前分数加上移动后的分数
        int newScore = score - calculateDis(toChar, toPos) + calculateDis(toChar, pos);

        return new State(newChars, toPos, size, newScore, this);
    }
}
