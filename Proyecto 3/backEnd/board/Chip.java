package backEnd.board;

public class Chip extends Square{
	protected boolean isWhite;
	
	public Chip(int row, int col, boolean isWhite) {
		super(row, col);
		this.isWhite = isWhite;
	}

	public boolean isWhite() {
		return isWhite;
	}

	/* Observacion:
	 * 		Dos fichas son iguales si ocupan la misma posición, independiente
	 * 		de que lado de la ficha sea visible (blanco o negro). */	
	
}
