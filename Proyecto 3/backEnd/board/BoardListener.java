package backEnd.board;


public interface BoardListener {

	/* Avisa que una ficha del tablero fue volteada o agregada. */
	public void wakeUp(Chip chip);
	
}
