package backEnd.fileManagement.dungeonLoader;

import backEnd.content.character.CharacterListener;
import backEnd.content.character.Hero;
import backEnd.content.character.Monster;
import backEnd.content.items.HealthBonus;
import backEnd.content.items.StrengthBonus;
import backEnd.dungeon.square.PassableSquare;
import backEnd.dungeon.square.PlayableContent;
import backEnd.dungeon.square.Square;


/**
 *  Esta interf�z permite a la implementaci�n del front crear clases gr�ficas
 *  de los objetos que interct�an en el juego.
 *  Se probee una implementaci�n default, {@link DefaultCreator}, en caso de no
 *  se necesario implementar la interf�z.
 *   
 */
public interface Creator {

	public Hero hero(String name, CharacterListener listener);
	
	public Monster monster(String name, int level, float strengthCte, float healthCte);
	
	public Monster monster(String name, int level, float strengthCte, float healthCte, CharacterListener listener);
	
	public HealthBonus healthBonus(int bonus);
	
	public StrengthBonus strenghtBonus(int bonus);
	
	public Square square(int x, int y);
	
	public PassableSquare passableSquare(PlayableContent cntnt, int x, int y);
	
	
}
