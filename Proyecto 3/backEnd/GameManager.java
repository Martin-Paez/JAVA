package backEnd;

import backEnd.board.Board;
import backEnd.board.Chip;

public class GameManager {

	private Board board;
	private Machine pc;
	private GameListener listener;
	public static final int PC_PLAY = 0, PC_PASS = PC_PLAY + 1; 
	
	public GameManager(boolean prune, boolean drawTree, boolean depth, double difficulty, GameListener listener) {
		board = new Board(listener);
		pc = new Machine(board, prune, drawTree, depth, difficulty);
		this.listener = listener;
	}

	/* Juega la ficha del jugador recibida como parametro, luego juega la maquina y retorna:
	 *
	 * 		GameManager.NEXT_TURN si la maquina jugo su ficha y le toca al jugador volver a jugar.
	 * 		GameManager.LOSE si el jugador perdio la partida
	 * 		GameManager.WIN si el jugador gano la partida
	 * 		GameManager.PC_PASS si la maquina paso el turno sin jugar.
	 *		GameManager.INVALID si es un movimiento invalido 
     *
	 */
	public void play(Chip playerMove) {
		if ( board.blackChips() + board.whiteChips() < 64 ) {
			
			int pcResult = PC_PASS;
			if ( playerMove == null || board.play(playerMove) ) {
				if ( pc.play( playerMove ) )
					pcResult = PC_PLAY;
			} else { 
				listener.invalidMove();
				return;
			}
			
			boolean WHITE = true;
			if ( board.moves(WHITE).next() == null ) {
				if ( pcResult == PC_PLAY ){
					listener.playerPass();
					play(null);
				}
			} else {
				if ( pcResult == PC_PLAY )
					listener.pcPlay();
				else
					listener.pcPass();
				return;
			}
		}
		if ( board.blackChips() > board.whiteChips() )
			listener.playerLose();
		else
			listener.playerWin();
	}
}
