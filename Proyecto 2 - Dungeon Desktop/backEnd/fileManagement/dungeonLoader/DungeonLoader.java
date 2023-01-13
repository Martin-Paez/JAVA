package backEnd.fileManagement.dungeonLoader;

import java.io.IOException;


import backEnd.Dungeon;
import backEnd.DungeonListener;
import backEnd.content.character.CharacterListener;
import backEnd.dungeon.square.Square;

import exceptions.DuplicateHeroException;
import exceptions.DuplicateSquareException;
import exceptions.FileFormatException;
import exceptions.HeroNotFoundException;
import exceptions.WrongDataException;
import frontEnd.content.drawable.FrontPassableSquare;


/**
 *  Esta clase se encarga de levantar de un archivo los datos necesarios para
 *  crear una instancia de la clase Dungeon y luego retornarla cuando sea necesario.
 *  
 */
public class DungeonLoader {
	
	private DungeonParser parser;
	private DungeonListener squareListener;
	private Square[][] dungeon;
	
	
	public DungeonLoader(String fileName, String heroName, CharacterListener chctrListener, 
			DungeonListener squareListener, Creator creator ) throws IOException, 
			FileFormatException, WrongDataException, DuplicateSquareException, 
			DuplicateHeroException, HeroNotFoundException {

		this.squareListener = squareListener;
		parser = new DungeonParser( fileName, heroName, chctrListener, creator );
		dungeon = parser.handleDimension();
		loadDungeon(creator);

	}
	
	
	/*
	 * Levanta del archivo cuyo nombre recibe como parámetro los datos
	 * necesarios para crear una instancia de la clase Dungeon.
	 */
	private void loadDungeon(Creator creator) throws DuplicateSquareException,
			WrongDataException, HeroNotFoundException, IOException,
			FileFormatException, DuplicateHeroException {

		int rows = dungeon.length, columns = dungeon[1].length;

		try {
			parser.ignoreLine(); /* El nombre del mapa */
			while (parser.ready()) {

				Square square = parser.handleSquare(); /* DuplicateHeroException */
				int x = square.getX();
				int y = square.getY();

				if (dungeon[x][y] != null)
					throw new DuplicateSquareException();
				if (x > columns && x < 0 && y > rows && y < 0)
					throw new WrongDataException("Dimensión incorrecta");

				dungeon[x][y] = square;

			}

			if (!parser.hasHero())
				throw new HeroNotFoundException();

			for (int i = 0; i < rows; i++)
				for (int j = 0; j < columns; j++)
					if (dungeon[i][j] == null) {
						FrontPassableSquare sq = new FrontPassableSquare(i, j);
						dungeon[i][j] = sq;
					}
			
		} finally {
			if (parser != null)
				parser.closeFile();
		}

	}
	
	
	public Dungeon getDungeon() {
		return new Dungeon(dungeon, parser.getCharacters(), parser.getHeroSquare(), squareListener);
	}
	
	
	public int getColumns () {
		return dungeon.length;
	}

	
	public int getRows () {
		return dungeon[1].length;
	}
	
	
}
