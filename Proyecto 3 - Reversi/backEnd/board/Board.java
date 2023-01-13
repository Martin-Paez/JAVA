package backEnd.board;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Board implements Cloneable {

	
	/* -------------------------  Variables de Instancia  ------------------------------- */
	
	/* Variables referida al calculo del valor de heuristica en funcion a la posicion
	 * de las fichas en el tablero */
	private final int EXCELLENT = 17, GOOD = 6, NORMAL = 5, BAD = 3, VERY_BAD = 0;
	private int heuristicOfPosition = 0;
	
	/* Las siguientes "constantes" no se declaran final ya que con el fin de evitar copiar
	 * la matriz entera al clonar la clase, se modifica el valor de las mismas ( las cuales
	 * permaneceran constantes para cada una de las instancias clonadas ) y mediante las 
	 * correspondientes mascaras se logra trabajar con la misma matriz de forma independiente
	 * en cada una de ellas.
	 * Es importante saber que despues de clonar la clase aproximadamente 60 veces, la matriz
	 * es compiada por completo para repetirse nuevamente el proceso.
	 */
	private int WHITE, BLACK, EMPTY = 0;
	private int COLOR_MASK;
	private int HEURISTIC_MASK;
		
	/* Variables referida al calculo del valor de heuristica en funcion a la la cantidad
	 * de fichas en juego */
	private int numberOfBlackChips, numberOfWhiteChips;
	
	private int board[][] = {	{ EXCELLENT, VERY_BAD, 	 GOOD, 	 GOOD, 	 GOOD,  GOOD,  VERY_BAD,  EXCELLENT },
								{ VERY_BAD,  VERY_BAD, 	  BAD, 	  BAD, 	  BAD, 	  BAD, VERY_BAD,  VERY_BAD 	},
								{ GOOD, 		  BAD, 	 GOOD, NORMAL, NORMAL, 	 GOOD, 		BAD,  GOOD 		},
								{ GOOD, 		  BAD, NORMAL, NORMAL, NORMAL, NORMAL, 		BAD,  GOOD 		},
								{ GOOD, 		  BAD, NORMAL, NORMAL, NORMAL, NORMAL, 		BAD,  GOOD 		},
								{ GOOD, 		  BAD, 	 GOOD, NORMAL, NORMAL, 	 GOOD, 		BAD,  GOOD 		},
								{ VERY_BAD,  VERY_BAD,	  BAD, 	  BAD, 	  BAD, 	  BAD, VERY_BAD,  VERY_BAD 	},
								{ EXCELLENT, VERY_BAD,   GOOD,   GOOD,   GOOD,   GOOD, VERY_BAD, EXCELLENT 	},
		};
	
	/*
	 * Las posiciones (3,3) y (4,4) son las esquinas que determinan la 'submatriz'
	 * cuadrada central del tablero, Mat, que forman las cuatro fichas centrales.
	 * La variable view determina el tamaño de otra 'submatriz' cuadrada, Mat2, 
	 * que se obtiene al expadir todos los lados de Mat en view lugares y que tiene
	 * la propiedad de ser la minima matriz cuadrada que contiene a todas las
	 * fichas del tablero.
 	 * Me permite recorrer el tablero evitando pasar por posiciones sin importancia
 	 * dependiendo de la catidad de fichas jugadas.
	 */
	private int view = 1;

	private BoardListener listener;
	private BoardListener emptyListener = new BoardListener() {
												@Override
												public void wakeUp(Chip chip) {}
											};
	
	/* -------------------------------  Metodos  ------------------------------------------- */
	
	
	public Board(BoardListener listener) {
		this();
		this.listener = listener;
		board[3][3] |= board[4][4] |= WHITE;
		board[3][4] |= board[4][3] |= BLACK;
		listener.wakeUp(new Chip(3, 3, true));
		listener.wakeUp(new Chip(4, 4, true));
		listener.wakeUp(new Chip(3, 4, false));
		listener.wakeUp(new Chip(4, 3, false));	
		numberOfBlackChips = 2;
		numberOfWhiteChips = 2;
	}

	public Board(String fileName, BoardListener listener) throws IOException {
		this();
		BufferedReader file = new BufferedReader( new FileReader(fileName));
		final int SIZE = 8;
		for (int i = 0; i < SIZE; i++) {
			String line = file.readLine();
			for (int j = 0; j < SIZE; j++) {
				char c = line.charAt(j);
				if ( c == '1' || c == '2' )
					/* 1 * 8 = WHITE, 2 * 8 = BLACK */
					board[i][j] += Integer.valueOf(c)*WHITE;
				else if ( c != ' ')
					throw new RuntimeException("Error de formato.");
			}
		}
	}

	private Board() {
		int i = 0;
		double pow;
		while ( ( pow = Math.pow(2, i++) ) <= EXCELLENT );
		WHITE = (int) pow;
		BLACK = WHITE * 2;
		COLOR_MASK = WHITE | BLACK;
		HEURISTIC_MASK = ~ COLOR_MASK;
	}
	
	
	/*
	 * Coloco una ficha y volteo aquellas que determinan las reglas del juego.
	 */
	public boolean play(Chip chip) {
		return possibleMove(chip, true);
	}
	
	public MoveIterator moves(boolean playWhites) {
		final int MIN_CENTER = 3, LENGTH;
		int zoom = (view < 3) ? view + 1 : 3;
		final int FIL = MIN_CENTER - zoom, COL = FIL;
		
		switch (view) {
		case 0:
			LENGTH = 4; break;
		case 1:
			LENGTH = 6; break;
		default:
			LENGTH = 8; break;
		}
		return new ConcretMoveIterator(FIL, COL, LENGTH, playWhites);
	}
	
	public int blackChips() {
		return numberOfBlackChips;
	}
	
	public int whiteChips() {
		return numberOfWhiteChips;
	}
	
	/* 
	 * Retorna un valor que indica cuanto mas favorable es un tablero para las fichas
	 * de un color. Cuanto mas positivo sea el numero, mas favorable es el tablero para
	 * las fichas negras y cuanto mas negativo mas favorable para las blancas. 
	 * El valor de retorno es mayor estricto a Integer.MIN_VALUE y menor estricto a
	 * Integer.MAX_VALUE.
	 */
	public double calcHeuristic() {
		final int CTE = 1;
		int heuristicOfNumberOfchips = (numberOfBlackChips - numberOfWhiteChips) / CTE;
		if ( numberOfBlackChips + numberOfWhiteChips == 64 ) {
			if ( heuristicOfNumberOfchips < 0 )
				return -100;
			return 100;
		}
				
		int gameStage = numberOfBlackChips + numberOfWhiteChips;
		/* No se reemplazan los valores 64, 42 = (2/3 * 64), 21 = (1/3 * 64) por constantes
		 * ya que son lo suficientemente claros */
		double finalStage = gameStage / 64.0;
		double mediumStage = (gameStage < 42) ? gameStage / 42.0 : 42.0 / gameStage;
		
		return 	heuristicOfPosition * ( finalStage + mediumStage ) +  
				heuristicOfNumberOfchips * finalStage;
	}
	
	private int content(int r, int c) {
		return board[r][c] & COLOR_MASK;
	}
		
	/* Clona la clase completa, con la excepcion de que el clon ya no tendra
	 * un BoardListener asignado.  */
	@Override
	public Board clone() {
		try {
			Board newBoard = (Board) super.clone();
			newBoard.board = new int[8][8];
			final int LENGTH = 8;
			for (int i = 0; i < LENGTH; i++) {
				for (int j = 0; j < LENGTH; j++) {
					newBoard.board[i][j] |= board[i][j];
				}	
			}
			newBoard.listener = emptyListener;
			return newBoard;
			
		} catch (Exception e){}
		return null;
	}

	/*
	 * Determina si es posible jugar la ficha que se recibe como parametro. Si
	 * applyChanges == true y es posible jugar entonces se coloca la ficha en 
	 * el tablero y se voltean las que corresponda segun establecen las reglas 
	 * del juego.
	 */
	private boolean possibleMove(Chip chip, boolean applyChanges) {
		int row = chip.row, col = chip.col;
		int zoom = (view < 3) ? view + 1 : 3;
		if ( outOfRange(row, col, zoom) || content(row, col) != EMPTY  )
			return false;

		int opponentColor, color;
		if ( chip.isWhite() ) {
			opponentColor = BLACK;
			color = WHITE;
		} else {
			opponentColor = WHITE;
			color = BLACK;
		}
		boolean changed = false;
		/* Chequeo si es posible jugar y si applyChanges == true volteo las fichas que corresponda */
		for (int x = -1, k = 0; x < 2; x++) {
			for (int y = -1; y < 2; y++, k++) {
				int r = row + y, c = col + x;
				if ( ! outOfRange(r, c, zoom) && content(r, c) == opponentColor 
						&& possibleChange(r, c, y, x, applyChanges) )
					
					if ( ! applyChanges )
						return true;	
					else 
						changed = true;
				
			}
		}
		/* Si es posible jugar y applyChanges == true agrego la nueva ficha al tablero */
		if (applyChanges && changed) {
			listener.wakeUp(chip);
			/* Acualizo la informacion referida a la heuristica */
			if ( chip.isWhite ) {
				++numberOfWhiteChips;
				heuristicOfPosition += NORMAL - board[row][col];
			} else {
				++numberOfBlackChips;
				heuristicOfPosition += board[row][col] - NORMAL;
			}
			/* Coloco la ficha */
			board[row][col] |= color;
			/* Acualizo la informacion referida a la busqueda de eficiencia */
			if ( outOfRange(row, col, view) )
				view = zoom;
		}
		return changed;
	}

	/*
	 * Determina si en la direccion dada por vrtMove y hzMove se encuentran ubicadas 
	 * consecutivamente fichas del mismo jugador que la que se asume se encuentra en
	 * la posicion (row, col) seguidas por otra de distinto color (sin espacios 
	 * intermedios). Si applyChanges == true entonces se voltean las fichas de manera
	 * que queden del mismo color de la ultima.
	 * Los resultados se desconocen si la posicion (row, col) esta vacia ( se toma esta
	 * decision ya que el metodo privado esta pensado para ser invocado por possibleMoves() ).
	 */
	private boolean possibleChange(int r, int c, int vrtMove, int hzMove, boolean applyChanges) {		
		int nxtF = r + vrtMove;
		int nxtC = c + hzMove;
		if ( outOfRange(nxtF, nxtC, view) )
			return false;
		int next = content(nxtF, nxtC);
		if ( next == EMPTY )
			return false;
		int color = content(r, c);
		if ( color != next  || possibleChange(nxtF, nxtC, vrtMove, hzMove, applyChanges)) {
			if (applyChanges) {
				color = ~color & COLOR_MASK;
				boolean isWhite = color == WHITE;
				if ( isWhite ) {
					numberOfWhiteChips++;
					numberOfBlackChips--;
				} else {
					numberOfBlackChips++;
					numberOfWhiteChips--;
				}
				board[r][c] &= HEURISTIC_MASK;
				heuristicOfPosition += (board[r][c] - NORMAL) * ((isWhite)? -2 : 2);
				board[r][c] |= color;
				listener.wakeUp( new Chip(r, c, isWhite ) );
			}
			return true;
		}
		return false;
	}
	
	/*
	 * El parametro view del mismo modo que la variable de instancia view
	 * determina una matriz cuadrada usada en este caso para delimitar un
	 * cuadrante. El metodo retorna true si la posicion (row,col) esta
	 * contenida en dicho cuadrante.
	 */
	private boolean outOfRange(int row, int col, int view) {
		int minCenter = 3, maxCenter = 4;
		int minPos = minCenter - view, maxPos = maxCenter + view;
		return row < minPos || row > maxPos || col < minPos || col > maxPos;
	}	
	
	/* Implementacion de a interface MoveIterator.
	 * Los resultados no se aseguran si se modifica el tablero mientras se esta
	 * iterando, lo correcto es utilizar una nueva instancia del iterador si el  
	 * tablero fue modificado.
	 */
	private class ConcretMoveIterator extends MoveIterator {
		CuadrantIterator itr;
		
		public ConcretMoveIterator(int col, int row, int sideLength , boolean playWhites) {
			super(playWhites);
			int hzMove = 1, vrtMove = 1;
			itr = new CuadrantIterator(col, row, hzMove, vrtMove, sideLength);
		}

		@Override
		public Chip next() {
			Square square = itr.nextPos();
				
			if ( square == null )
				return null;
			
			Chip chip = new Chip (square.row, square.col, playWhites );
			if ( possibleMove(chip, false) )
				return chip;
			return next();

		}
		
	}
	
	/*
	 * Clase que modela un iterador sobre una subconjunto de posiciones del tablero.
	 */
	private class CuadrantIterator {
		private int col, hzMove, vrtMove, sideLength;
		private int i = 0, j = 0, currentCol, currentFil;
		
		/* Los parametros vrtMove y hzMove determinan la distancia de filas (vrtMove) y
		 * columnas (hzMove) que hay entre cada elemento del subconjunto de posiciones
		 * del tablero sobre las cuales se desea iterar. La primer posicion esta 
		 * determinada por los parametros row y col.
		 * Por construccion el subconjunto queda dentro de una submatriz cuadrada de
		 * la matriz board cuyos elementos estan igualmente espaciados, siendo sideLength
		 * la cantidad de elementos del subconjunto por cada fila o columna de la 
		 * submatriz.
		 */
		public CuadrantIterator(int col, int row, int hzMove, int vrtMove, int sideLength ) {
			if ( (col + (sideLength-1) * hzMove) > 7 || (row + (sideLength-1) * vrtMove) > 7 )
				throw new RuntimeException("Error critico: Out of bounds exception");
			this.col = currentCol = col - hzMove;
			currentFil = row;
			this.hzMove = hzMove;
			this.vrtMove = vrtMove;
			this.sideLength = sideLength;
		}
		
		/*
		 * Retorna el valor almacenado en la siguiente posicion o -1 si ya se ha iterado
		 * sobre todas las posiciones. 
		 */
		public int next() {
			if ( j % (sideLength - 1) == 0 && i % sideLength == 0 && j > 0)
				return -1;
			
			if ( i % sideLength == 0 && i > 0 ) {
				i = 0;
				currentCol = col;
				j++;
				currentFil += vrtMove;
			}
			i++;
			currentCol += hzMove;		
			return content(currentFil, currentCol);
		}
		
		/*
		 * Retorna una instancia de la clase Square con la siguiente posicion, o null
		 * si ya se ha iterado sobre todas.
		 */
		public Square nextPos() {
			if ( next() == - 1 )
				return null;
			return new Square(currentFil, currentCol);
		}

	}
	
}
