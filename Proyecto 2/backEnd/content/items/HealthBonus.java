package backEnd.content.items;

import backEnd.content.character.Hero;
import backEnd.dungeon.square.PlayableContent;

public class HealthBonus extends Item{
	
	public HealthBonus(int bonus) {
		super(bonus);
	}

	/**
	 *  Con el �nico objeto con el cual un item sabe interactuar es con
	 *  una instacia de la clase Hero.
	 *  Aumenta la vida del h�ro que recibe como par�metro y le deja
	 *  el paso para que se posicione sobre su celda . Luego el �tem es
	 *  eliminado.
	 */
	@Override
	public void interact(PlayableContent hero) {
		try {
		
		super.interact((Hero) hero);
		( (Hero)hero ).restoreHealth(bonus);
		
		} catch ( ClassCastException e ) {
			throw new RuntimeException("El bonus no sabe como interactuar con el objeto recivido.");
		}
		
	}
	
}
