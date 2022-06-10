package backEnd.dungeon.square;

import java.io.Serializable;


public class Square implements Serializable {

	private int x, y; 			/* Posición del casillero */
	private boolean visible;
	
	
	/* Crea una casillero vacía  */
	public Square(int x, int y) {
		
		this.x = x;
		this.y = y;
		visible = false;
	
	}

	
	/**
	 *  Hace visible un casillero
	 */
	public void reveal() {
		visible = true;
	}
	
	
	/**
	 *  Informa sobre la visibilidad del casillero ( visible o no visible ).
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 *  Dos casilleros se consideran adyacentes si están unidos por un
	 *  mismo lado, no así por un mismo vertice.
	 *  
	 * @param square  -  celda a determinar adyacencia.
	 */
	public boolean isAdjacent(Square square) {
		return (x == square.x && Math.abs(y - square.y) == 1)
				|| (Math.abs(x - square.x) == 1 && y == square.y);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
}
