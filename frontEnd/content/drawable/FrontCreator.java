package frontEnd.content.drawable;

import backEnd.content.character.CharacterListener;
import backEnd.content.character.Hero;
import backEnd.content.character.Monster;
import backEnd.content.items.HealthBonus;
import backEnd.content.items.StrengthBonus;
import backEnd.dungeon.square.PassableSquare;
import backEnd.dungeon.square.PlayableContent;
import backEnd.dungeon.square.Square;
import backEnd.fileManagement.dungeonLoader.Creator;


/**
 *  Esta clase implementa la interfáz {@link Creator}. Se encarga de instanciar
 *  las clases gráficas.
 */
public class FrontCreator implements Creator{
	
	ErrorListener errorListener;
	
	
	public FrontCreator(ErrorListener ErrorListener) {
		this.errorListener = ErrorListener;
	}
	
	
	/**
	 *  Retorna una instacia de la clase FrontHero 
	 */
	@Override
	public Hero hero(String name, CharacterListener listener){
		return new FrontHero(name, listener);
	}
	
	
	/**
	 *   Retorna una instacia de la clase FrontHealthBonus 
	 */
	@Override
	public HealthBonus healthBonus(int amount) {
		return new FrontHealthBonus(amount);
	}
	
	
	/** 
	 *   Retorna una instacia de la clase FrontStrengthBonus 
	 */
	@Override
	public StrengthBonus strenghtBonus(int amount) {
		return new FrontStrenghtBonus(amount);
	}

	
	/** 
	 *   Retorna una instacia de la clase FrontStrengthBonus 
	 */
	@Override
	public Square square(int x, int y) {	
		return new Wall(x, y);
	}

	
	/** 
	 *   Retorna una instacia de la clase FrontPassableSquare 
	 */
	@Override
	public PassableSquare passableSquare(PlayableContent cntnt, int x, int y) {
		return new FrontPassableSquare(cntnt, x, y);
	}


	/** 
	 *   Retorna una instacia de la clase FrontMonster 
	 */
	@Override
	public Monster monster(String type, int level, float strengthCte, float healthCte ) {
		return new FrontMonster(type, level, strengthCte, healthCte, getMonsterImage(type));
	}
	
	
	/** 
	 *  Retorna una instacia de la clase FrontMonster que utiliza un listener
	 */
	@Override
	public Monster monster(String type, int level, float strengthCte, float healthCte, 
			CharacterListener listener ) {	
		return new FrontMonster(type, level, strengthCte, healthCte, getMonsterImage(type),
				listener);
	}
	
	
	/**
	 *  Retorna el path del archivo que contiene la imágen del monstro que
	 *  corresponde con el tipo recibido como parámetro.
	 */
	private String getMonsterImage (String type) {
		
		String imgFile = null;
		if ( type == "Golem" ) { 
				imgFile = "resources/golem.png";
		}else if ( type == "Dragon" ) {
				imgFile = "resources/dragon.png"; 
		}else if ( type == "Snake" ) {
				imgFile = "resources/serpent.png";
		}else {
				errorListener.fatalError("Formato incorrecto");
		}
		
		return imgFile;
	}
	
	
}
