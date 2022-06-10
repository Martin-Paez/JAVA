package backEnd.fileManagement.dungeonLoader;

import backEnd.content.character.CharacterListener;
import backEnd.content.character.Hero;
import backEnd.content.character.Monster;
import backEnd.content.items.HealthBonus;
import backEnd.content.items.StrengthBonus;
import backEnd.dungeon.square.PassableSquare;
import backEnd.dungeon.square.PlayableContent;
import backEnd.dungeon.square.Square;


public class DefaultCreator implements Creator{

	
	@Override
	public Hero hero(String name, CharacterListener listener) {
		return new Hero(name, listener);
	}

	@Override
	public Monster monster(String name, int level, float strengthCte, float healthCte ) {
		return new Monster(name, level, strengthCte, healthCte);
	}

	@Override
	public Monster monster(String name, int level, float strengthCte, float healthCte, CharacterListener listener) {
		return new Monster(name, level, strengthCte, healthCte, listener);
	}

	@Override
	public HealthBonus healthBonus(int bonus) {
		return new HealthBonus(bonus);
	}

	@Override
	public StrengthBonus strenghtBonus(int bonus) {
		return new StrengthBonus(bonus);
	}

	@Override
	public Square square(int x, int y) {
		return new Square(x, y);
	}

	@Override
	public PassableSquare passableSquare(PlayableContent cntnt, int x, int y) {
		return new PassableSquare(cntnt, x, y);
	}

	
}
