package backEnd.board;

public class Square {
	int row, col;				/* Visible dentro del paquete board */
	
	public Square(int row, int col) {
		this.row = row;
		this.col = col;
	}

	public int getFil() {
		return row;
	}

	public int getCol() {
		return col;
	}
	
	public boolean equals(Object chip) {
		if ( ! (chip instanceof Chip ) )
			return false;
		return row == ((Chip) chip).row && col == ((Chip) chip).col ;
	}
	
}
