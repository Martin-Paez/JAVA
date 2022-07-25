package backEnd.content.character;

public interface CharacterListener {

	/**
	 *  LLeba a cabo los procesos correspondientes a la muerte
	 *  del personaje que tenga asociado el listener.
	 *  
	 * @param character - Personaje que resulto muerto
	 */
	public void die( Character character );
	
	
}
