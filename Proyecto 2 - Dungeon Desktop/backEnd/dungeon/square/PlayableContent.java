package backEnd.dungeon.square;

import java.io.Serializable;


public abstract class PlayableContent implements Serializable{
	protected PassableSquare container;
	
	
	public void setContainer(PassableSquare container) {
		this.container = container;
	}
	
	
	public PassableSquare getContainer() {
		return container;
	}
	
	/**
	 *  Interactúa con otro objeto PlayableContent. 
	 *  OBS: La interacción entre objetos que heredan a la clase queda
	 *  	 determinada por la naturaleza de los objetos en cuestión.
	 *    
	 * @param hero
	 */
	public abstract void interact(PlayableContent obj);


}
