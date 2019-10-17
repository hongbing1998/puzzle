package org.edu.cdtu.lhb.puzzlegui;

import java.util.function.Consumer;

public class PuzzleGuiModuleTest {
    private static int j = 0;

    private static synchronized void changeJ(int offset) {
        j += offset;
    }

    public static void main(String[] args) {
        Consumer<Integer> changeJ = PuzzleGuiModuleTest::changeJ;
        new Thread(() -> changeJ.accept(1)).start();
        new Thread(() -> changeJ.accept(1)).start();
        new Thread(() -> changeJ.accept(-1)).start();
        new Thread(() -> changeJ.accept(-1)).start();
    }
}
