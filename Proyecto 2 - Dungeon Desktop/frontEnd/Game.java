package frontEnd;

import javax.swing.JFrame;

import frontEnd.frames.mainFrame.MainFrame;


public class Game {

	public static void main(String[] args) {
		MainFrame frame = new MainFrame(330,200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}