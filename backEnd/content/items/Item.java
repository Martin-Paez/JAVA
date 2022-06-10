package backEnd.content.items;

import backEnd.content.character.Hero;
import backEnd.dungeon.square.PlayableContent;

public abstract class Item extends PlayableContent{
	protected int bonus;
	
	public Item (int bonus) {
		if ( bonus < 1 )
			throw new IllegalArgumentException();
		
		this.bonus = bonus;
	}
	
	/**
	 *  Le deja el paso al héroe para que se posicione sobre su celda
	 *  y luego el ítem es eliminado.
	 */
	public void interact(Hero hero) {
		getContainer().swapContents(hero.getContainer());
		getContainer().removeContent();
	}
	
	public int getBonus(){
		return bonus;
	}

}