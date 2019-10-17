package org.edu.cdtu.lhb.puzzlegui.listener;


import org.edu.cdtu.lhb.puzzlegui.Puzzle;
import org.edu.cdtu.lhb.puzzlegui.bean.Grid;
import org.edu.cdtu.lhb.puzzlegui.bean.Picture;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 格子按钮点击事件监听类
 *
 * @author 李红兵
 *
 */
public class GridClickListener implements ActionListener {
	private Grid current;// 当前格
	private static int steps = 0;// 步数
	private static boolean firstClick = true;// 第一次点击

	public static void setFirstClick(boolean firstClick) {
		GridClickListener.firstClick = firstClick;
	}

	public static void setSteps(int step) {
		GridClickListener.steps = step;
	}

	public GridClickListener(Grid current) {
		this.current = current;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		boolean complete = true;// 是否完成标志
		// 各个方向可以移动的条件
		boolean upMove = current.getOrder() + Puzzle.game.getCOL() == Puzzle.game.getBlank().getOrder();
		boolean downMove = current.getOrder() - Puzzle.game.getCOL() == Puzzle.game.getBlank().getOrder();
		boolean leftMove = current.getOrder() + 1 == Puzzle.game.getBlank().getOrder();
		leftMove = leftMove && (current.getOrder() + 1) % Puzzle.game.getCOL() != 0;
		boolean rightMove = current.getOrder() - 1 == Puzzle.game.getBlank().getOrder();
		rightMove = rightMove && current.getOrder() % Puzzle.game.getCOL() != 0;

		// 满足一个移动条件即可
		if (leftMove || rightMove || upMove || downMove) {
			if (firstClick) {
				Puzzle.game.getTimer().start();
				firstClick = false;
			}
			// 交换当前格与空白格的部分属性
			Picture tempPicture;
			current.setBlank(true);
			tempPicture = Puzzle.game.getBlank().getPicture();
			Puzzle.game.getBlank().setBlank(false);
			Puzzle.game.getBlank().setPicture(current.getPicture());
			Puzzle.game.setBlank(current);
			current.setPicture(tempPicture);
			Puzzle.game.getStep().setText("步数：" + ++steps);
		}

		// 判断格子序号与图片序号是否全部一致
		labe: for (int i = 0; i < Puzzle.game.getROW(); i++) {
			for (int j = 0; j < Puzzle.game.getCOL(); j++) {
				if (Puzzle.game.getGrids()[i][j].getOrder() != Puzzle.game.getGrids()[i][j].getPicture().getOrder()) {
					complete = false;
					break labe;// 一旦有一个不一致就结束判断，提升响应速度
				}
			}
		}

		// 游戏完成弹框提示，确认后以当前难度重新开始游戏
		if (complete) {
			Puzzle.game.getCard().show(Puzzle.game.getCenter(), "back");
			Puzzle.game.getTimer().stop();
			String message = "恭喜你，完成了拼图！(★ᴗ★)\n用时：";
			message += TimeLabelActionListener.getMinutes() == 0 ? "" : (TimeLabelActionListener.getMinutes() + "分");
			message += TimeLabelActionListener.getSeconds() + "秒      步数：" + steps + "步";
			JOptionPane.showMessageDialog(Puzzle.game.getMainFrame().getJMenuBar(), message, "完成", JOptionPane.INFORMATION_MESSAGE);
			Puzzle.game.restart(Puzzle.game.getROW(), Puzzle.game.getCOL());
		}
	}
}
