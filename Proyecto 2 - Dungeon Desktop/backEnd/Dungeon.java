package backEnd;

import java.io.Serializable;
import java.util.List;

import backEnd.content.character.Character;
import backEnd.content.character.Hero;
import backEnd.content.character.Monster;
import backEnd.dungeon.square.PassableSquare;
import backEnd.dungeon.square.PlayableContent;
import backEnd.dungeon.square.Square;


public class Dungeon implements Serializable {

	private Square[][] dungeon;
	private Hero hero;
	private PassableSquare heroSq;/* Celda en la cual se encuenta el h�roe */
	private List<Character> chctrs; /* Lista con un puntero a cada personaje */
	transient private DungeonListener listener;

	/**
	 *  Constructor de la clase Dungeon.
	 *  
	 * @param dungeon	Matriz con los objetos del tablero
	 * @param chctrs	Lista de personajes
	 * @param heroSq	Casillero en donde se encuentra el h�roe
	 * @param listener	Listener para informar de los acontecimientos en el juego.
	 */
	public Dungeon(Square[][] dungeon, List<Character> chctrs,
			PassableSquare heroSq, DungeonListener listener) {
		
		this.dungeon = dungeon;
		this.chctrs = chctrs;
		this.heroSq = heroSq;
		hero = (Hero) heroSq.getContent();
		this.listener = listener;
		lookAround();
	
	}

	
	/**
	 * Da comienzo a la interaccion entre la celda del h�roe y la celda cuya
	 * posici�n en el tablero es recivida como par�metro. Luego de dicho
	 * interaci�n se revelan las celdas vecinas a la celda en la cual el jugador
	 * se encuentra en ese momento.
	 * 
	 * @param x
	 *            - Columna del tablero
	 * @param y
	 *            - Fila del tablero
	 */
	public void movHero(int x, int y) {
		
		if (!inBounds(x, y))
			throw new RuntimeException("No existe el casillero ");

		Square sq = dungeon[x][y];
		if (!((sq instanceof PassableSquare) && heroSq.isAdjacent(sq)))
			return;

		((PassableSquare) sq).interact(heroSq);
		heroSq = hero.getContainer();
		lookAround();
	
	}

	
	/*
	 * Revela las celdas aleda�as a la celda en la cual se encuentra el jugador.
	 * Se llama a los metodos de la implementaci�n de RevealListener, recivida
	 * como par�metro al instaciar la clase, para avisar si un monstruo se
	 * encuentra en alguna de dichas celdas y/o para dar a conocer aquellas
	 * celdas que pasaron a ser visibles.
	 */
	private void lookAround() {
		int cont = 0;

		for (int x = heroSq.getX() - 1; x <= 1 + heroSq.getX(); x++) {
			for (int y = heroSq.getY() - 1; y <= 1 + heroSq.getY(); y++) {
				if ( inBounds(x, y) ) {
					if ( !dungeon[x][y].isVisible()) {
					dungeon[x][y].reveal();
					listener.revealSquare(dungeon[x][y]);
					cont++;
					}
				
					if (dungeon[x][y] instanceof PassableSquare && !((PassableSquare) dungeon[x][y]).isEmpty()) {
						PlayableContent content = ((PassableSquare) dungeon[x][y]).getContent();
						if (content instanceof Monster) {
							listener.monsterInRange((Monster) content);
						}
					}
				}	
			}
		}
		for (Character chctr : chctrs) {
			chctr.restoreHealth(cont*chctr.getHealth());
		}
		
	}

	
	/**
	 * Determina si la posicion (x, y) pertenece se encuentra dentro de los
	 * l�mites del tablero.
	 * 
	 * @param x
	 *            - Columna del tablero
	 * @param y
	 *            - Fila del tablero
	 */
	public boolean inBounds(int x, int y) {
		return !(x < 0 || x >= dungeon.length || y < 0 || y >= dungeon[1].length);
	}

	
	public Square getSquare(int x, int y) throws ArrayIndexOutOfBoundsException {
		return dungeon[x][y];
	}

	
	public Hero getHero() {
		return hero;
	}

	
	public PassableSquare getHeroSquare() {
		return heroSq;
	}

	
	public int getColumns() {
		return dungeon.length;
	}

	
	public int getRows() {
		return dungeon[0].length;
	}

	
	public void setListener(DungeonListener listener) {
		this.listener = listener;
	}
	

	public List<Character> getCharacters() {
		return chctrs;
	}

}
