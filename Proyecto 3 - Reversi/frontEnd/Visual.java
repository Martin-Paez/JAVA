package frontEnd;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import backEnd.GameListener;
import backEnd.GameManager;
import backEnd.board.Chip;

public class Visual extends JFrame implements GameListener, GamePanelListener{

	private static final long serialVersionUID = 1L;
	private boolean WHITE = true;
	private static final int CELL_SIZE = 61;
	private GamePanel gp;
	private GameManager  game;
	
	public Visual(int x, int y, boolean prune, boolean depth, double difficulty) {

		setTitle("Reversi");
		setLayout(new BorderLayout());
		setBounds(x, y, 8 * CELL_SIZE + 8, 8 * CELL_SIZE + 33);
		
		try {
			gp = new GamePanel(CELL_SIZE, this);
		} catch (IOException e) {
			exitMsg("Error al cargar las imagenes");
		}
		getContentPane().add(gp, BorderLayout.CENTER);
		
		final boolean DRAW_TREE = false;
		game = new GameManager(prune, DRAW_TREE, depth, difficulty, this );
		repaint();
	}
	
	public void exitMsg(String errorMsg) {
		JOptionPane.showMessageDialog(null, errorMsg);
		System.exit(0);			
	}
	
	@Override
	public void play(int row, int column) {
		try {
			game.play( new Chip(row, column, WHITE ) );
		} catch (Exception e) {
			exitMsg("Error critico");
		}
		repaint();
	}
	
	@Override
	public void wakeUp(Chip chip) {
		gp.put(chip);
	}
		
	@Override
	public void playerPlay() {}

	@Override
	public void playerPass() {
		repaint();
		JOptionPane.showMessageDialog(null, "No podes realizar movimientos, la maquina vuelve a jugar.");
	}

	@Override
	public void pcPlay() {}

	@Override
	public void pcPass() {
		JOptionPane.showMessageDialog(null, "La maquina paso el turno sin jugar");
	}

	@Override
	public void playerLose() {
		repaint();
		exitMsg("Perdiste");
	}

	@Override
	public void playerWin() {
		repaint();
		exitMsg("Ganaste");
	}

	@Override
	public void invalidMove() {
		JOptionPane.showMessageDialog(null, "Movimiento invalido");
	}
	
	
			
}
