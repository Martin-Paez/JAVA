package backEnd.content.character;

import backEnd.dungeon.square.PlayableContent;

public class Monster extends Character {	
		
	public Monster(String type, int level, float strengthCte, float healthCte){
		super(type, level);
		maxHealth = health = calcHealth(healthCte);
		strenght = calcStrength(strengthCte);
	}
	
	public Monster(String type, int level, float strengthCte, float healthCte, CharacterListener listener){
		this(type, level, strengthCte, healthCte);
		
		if ( listener == null )
			throw new RuntimeException("Listener inválido");
		
		this.listener = listener;
	}

	/**
	 *  Con el único objeto que un monstruo solo sabe interactuar, es con 
	 *  una instancia de la clase Hero.
	 *  El monstruo ataca al héroe golpeandolo mediante el método fight 
	 *  implementado en la clase Hero.
	 *  Como resultado de la pelea alguno de los dos personajes puede morir,
	 *  en tal caso, si el monstruo tiene asosido un Characterlistener se lo
	 *  llama.
	 */
	@Override
	public void interact(PlayableContent hero) {
		try {
		( ( Hero ) hero).defend(this);
		} catch ( ClassCastException e ) {
			throw new RuntimeException("Un monstruo no sabe como interactuar con  con el objeto recivido.");
		}
	}
	
	/**
	 * Calcula la vida máxima del mostruo, a partir de la constante de
	 * vida del mostruo y su nivel.
	 * 
	 * @param level - Nvel del mostruo
	 * 
	 * @see getHealthCte
	 */
	protected int calcHealth(float heallthCte) {
		return (int) Math.floor( ( (level+3) * (level + 3) - 10 ) * heallthCte ); 
	}
	
	/**
	 * Calcula la fuerza del mostruo, a partir de la constante de fuerza
	 * del mostruo y su nivel.
	 * 
	 * @param level - Nvel del mostruo
	 * 
	 * @see getStrengthCte
	 */
	protected int calcStrength(float strengthCte) {
		return (int) Math.floor( (level + 5 )* level * 0.5F * strengthCte ); 
	}
	
	public String getName() {
		throw new RuntimeException("No debería ser usado");
	}
	
	public String getType() {
		return super.getName();
	}

}
