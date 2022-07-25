package backEnd.content.character;

import java.io.Serializable;

import backEnd.dungeon.square.PlayableContent;


public abstract class Character extends PlayableContent implements Serializable { 
	
	protected int health, maxHealth, strenght;
	protected int level;
	protected String name;
	transient protected CharacterListener listener = null;

	
	public Character(String name, int level) {
	
		if (level < 1 || level > 3)
			throw new IllegalArgumentException();
		
		this.level = level;
		this.name = name;
	
	}

	/**
	 * Se lleva a cabo un enfrentamiento entre dos personajes. El personaje que
	 * receptor del mensaje siempre recive el primer golpe, pudiendo suceder
	 * entonces que el mismo resulte muerto sin haber dañado su contrincante.
	 * 
	 * @param chctr
	 *            - Personaje que entrará en batalla con el receptor del
	 *            mensaje.
	 * @return 
	 * 
	 * @return DEFEAT - Si el receptor del mesaje muere. VICTORY - Si el
	 *         personaje recivido como parámetro muere. DRAW - Si ningún
	 *         personaje resulta muerto.
	 * 
	 */
	final int NOT_DEAD = 0;
	public int hurt(int damage) {
	
		health -= damage;
		if ( health < 1 ) {
			health = 0;
			return die();
		}
		
		return NOT_DEAD;
	
	}
	

	/**
	 * Restaura (cura) n puntos de vida al personaje (no se acumulan).
	 * 
	 * @param health
	 *            - Cantidad de vida a restaurar. Si el valor es negativo no se
	 *            hace nada. Si n supera la vida máxima, los puntos no se
	 *            acumulan.
	 * 
	 */
	public void restoreHealth(float health) {
	
		if (health < 0)
			throw new IllegalArgumentException();

		if ((this.health += health) > maxHealth)
			this.health = maxHealth;
	
	}
	

	/* Se borra la referencia del casillero que lo contiene. Si el personaje
	 * tiene asociado un listener se llama al mismo, luego retorna el nivel
	 * del mismo ( siempre distinto de 0 = NOT_DEAD ).
	 */
	private int die() {
		
		container.removeContent();
		if ( listener != null )
			listener.die(this);
	
		return level;
	
	}
	
	
	public int getLevel() {
		return level;
	}
	
	public int getHealth() {
		return (int)health;
	}
	
	public int getMaxHealth() {
		return (int)maxHealth;
	}
	
	public int getStrength() {
		return (int)strenght;
	}

	public String getName() {
		return name;
	}

	public void setListener(CharacterListener listener) {
		this.listener = listener;
	}	
	
}
