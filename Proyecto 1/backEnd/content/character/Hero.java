package backEnd.content.character;

import backEnd.dungeon.square.PlayableContent;

public class Hero extends Character{
	private int experience = 0;
	private int strengthBonus = 0, healthBonus = 0;

	
	/**
	 * Crea héroe de nivel 1, con la vida "llena", sin bonus ni experiencia.
	 * 
	 * @param name
	 *            - Nombre del héroe.
	 */
	public Hero(String name,  CharacterListener listener) {
		super(name, 1);
		health = maxHealth = calcHealth();
		strenght = calcStrength();
		this.listener = listener;
	}
	
	/**
	 *  El héroe se defiende del ataque de un enemigo, si el héroe resulta 
	 *  muerto se llama al listener asociado. En caso de resistir el ataque el
	 *  héroe devuelve el golpe, si mata a su adversario aumenta su experiencia
	 *  y/o nivel en función del enemigo con el cual está peleano.
	 *  
	 * @param monster
	 * @param damage
	 */
	public void defend(Monster monster) {
		if ( hurt(monster.getStrength() ) == NOT_DEAD ) {
			experience += monster.hurt(strenght);
			if ( experience > 0 && experience >= level * 5 )
				levelUp();
		}
	}
	
	/* Calcula la vida máxima del héroe 
	 */
	private int calcHealth() {
		return 10 * level + healthBonus; 
	}
	
	/* Calcula la fuerza del héroe
	 */
	private int calcStrength() {
		return 5 * level + strengthBonus; 
	}

	/* Sube el nivel del personaje y setea los valores iniciales de cada nivel.
	 */
	private void levelUp() {
		experience = 0;
		level++;
		maxHealth = calcHealth();
		strenght = calcStrength();
	}

	/**
	 *  Aumenta la vida máxima del personaje (no lo cura).
	 *  
	 * @param health	-	Cantidad de vida a agregar. Si la cantidad es
	 * 						negativa no se hace nada.
	 * 
	 */
	public void incMaxHealth(int inc) {
		if ( inc < 0 )
			throw new IllegalArgumentException();
		healthBonus += inc;
		maxHealth = calcHealth();
	}
	
	/**
	 *  Aumenta la fuerza máxima del personaje.
	 *  
	 * @param health	-	Cantidad de fuerza a agregar. Si la cantidad es
	 * 						negativa no se hace nada.
	 * 
	 */
	public void incMaxStrength(int inc) {
		if ( inc < 0 )
			throw new IllegalArgumentException();
		strengthBonus += inc;
		strenght = calcStrength();
	}
	
	public int getExperience() {
		return experience;
	}
	
	public String getName() {
		return name;
	}

	/**
	 *  No se define ningun tipo de interacción con un objeto del tipo
	 *  PlayableContent, sin embargo si el objeto sabe interactuar con
	 *  el héroe dicha interacción se lleba a cabo.
	 */
	@Override
	public void interact(PlayableContent obj) {
		obj.interact(this);
	}	
}
