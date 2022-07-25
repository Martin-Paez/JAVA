package backEnd;

import backEnd.content.character.Monster;
import backEnd.dungeon.square.Square;


public interface DungeonListener {

	
	/**
	 * Utilizada para dar a conocer que un casillero pas� a ser visible.
	 * 
	 * @param sq
	 *            - Casillero que pas� a ser visible.
	 */
	public void revealSquare(Square sq);
	
	
	/**
	 * Utilizada para avisar que un determinado monstruo se encuentra en
	 * el rango de ataque del personaje.
	 * 
	 * @param sq
	 *            - Monstruo dentro del rango de ataque.
	 */
	public void monsterInRange(Monster monster);

	
}
