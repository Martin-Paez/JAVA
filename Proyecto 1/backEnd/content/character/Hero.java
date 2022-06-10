package backEnd.content.character;

import backEnd.dungeon.square.PlayableContent;

public class Hero extends Character{
	private int experience = 0;
	private int strengthBonus = 0, healthBonus = 0;

	
	/**
	 * Crea h�roe de nivel 1, con la vida "llena", sin bonus ni experiencia.
	 * 
	 * @param name
	 *            - Nombre del h�roe.
	 */
	public Hero(String name,  CharacterListener listener) {
		super(name, 1);
		health = maxHealth = calcHealth();
		strenght = calcStrength();
		this.listener = listener;
	}
	
	/**
	 *  El h�roe se defiende del ataque de un enemigo, si el h�roe resulta 
	 *  muerto se llama al listener asociado. En caso de resistir el ataque el
	 *  h�roe devuelve el golpe, si mata a su adversario aumenta su experiencia
	 *  y/o nivel en funci�n del enemigo con el cual est� peleano.
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
	
	/* Calcula la vida m�xima del h�roe 
	 */
	private int calcHealth() {
		return 10 * level + healthBonus; 
	}
	
	/* Calcula la fuerza del h�roe
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
	 *  Aumenta la vida m�xima del personaje (no lo cura).
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
	 *  Aumenta la fuerza m�xima del personaje.
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
	 *  No se define ningun tipo de interacci�n con un objeto del tipo
	 *  PlayableContent, sin embargo si el objeto sabe interactuar con
	 *  el h�roe dicha interacci�n se lleba a cabo.
	 */
	@Override
	public void interact(PlayableContent obj) {
		obj.interact(this);
	}	
}
