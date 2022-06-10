package backEnd.fileManagement.dungeonLoader;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import backEnd.content.character.Character;
import backEnd.content.character.CharacterListener;
import backEnd.content.character.Hero;
import backEnd.content.character.Monster;
import backEnd.dungeon.square.PassableSquare;
import backEnd.dungeon.square.PlayableContent;
import backEnd.dungeon.square.Square;
import backEnd.fileManagement.FileLoader;

import exceptions.DuplicateHeroException;
import exceptions.FileFormatException;
import exceptions.WrongDataException;


public class DungeonParser {
	
	private FileLoader file;
	private String heroName;
	private CharacterListener chctrListener;
	private Creator creator;
	
	private List<Character> chctrs = new ArrayList<Character>();
	private PassableSquare heroSq = null;

	
	public DungeonParser(String fileName, String heroName, CharacterListener chctrListener, 
			Creator creator) throws FileFormatException, IOException, FileNotFoundException,
			WrongDataException {
		
		file = new FileLoader(fileName);
		this.heroName = heroName;
		this.chctrListener = chctrListener;
		this.creator = creator;
	}

	
	/**
	 *  Trata la siguiente línea del archivo como aquella línea en la cual se encuentran
	 *  las dimensiones del tablero, en caso de no haber errores en el archivo retorna 
	 *  una matriz de casilleros con esas dimensiones.
	 */
	public Square[][] handleDimension() throws IOException,
			FileFormatException, WrongDataException {
		
		int fil, col;
		Scanner scanner;
		try {
			scanner = new Scanner( file.nextLine() );
			scanner.useDelimiter(",");
			fil = scanner.nextInt();
			col = scanner.nextInt();
		} catch (Exception e) {
			throw new FileFormatException(file.getLineNumber());
		}
		
		if ( scanner.hasNext() )
			throw new FileFormatException(file.getLineNumber());
		if (col < 6 || col > 30 || fil < 6 || fil > 30)
			throw new WrongDataException(file.getLineNumber());

		return new Square[fil][col];
	}
	
	
	/**
	 *  Trata la siguiente línea del archivo como aquella línea en la cual se encuentra
	 *  la información  para instanciar un nuevo casillero, en caso de no haber errores
	 *  en el archivo retorna una instancia de dicho casillero.
	 */
	public Square handleSquare() throws IOException, FileFormatException,
			WrongDataException, DuplicateHeroException {
		final int HERO = 1, WALL = 2, MONSTER = 3, HEALTH_BONUS = 4, STRENGHT_BONUS = 5;
		
		String line = file.nextLine();
		if ( line == null )
			throw new FileFormatException(file.getLineNumber());
			
		Scanner scanner = new Scanner(line);
		scanner.useDelimiter(",");
		int type, x, y, MnstrType, level, bonus;
		try {
			type = scanner.nextInt();
			x = scanner.nextInt();
			y = scanner.nextInt();
			MnstrType = scanner.nextInt();
			level = scanner.nextInt();
			bonus = scanner.nextInt();
		} catch (Exception e) {
			throw new FileFormatException(file.getLineNumber());
		}
		if ( scanner.hasNext() )
			throw new FileFormatException(file.getLineNumber());

		if ( (type >= HERO && type <= MONSTER && bonus != 0) ||
			 (type != MONSTER && (MnstrType != 0 || level != 0))) {
			 	throw new WrongDataException(file.getLineNumber());
		}
		
		PlayableContent cntnt = null;
		Square square = null;
		switch (type) {
		case WALL:
			square = creator.square(x, y);
			break;
			
		case HEALTH_BONUS:
			cntnt = creator.healthBonus(bonus);
			break;

		case STRENGHT_BONUS:
			cntnt = creator.strenghtBonus(bonus);
			break;

		case HERO:
			if (heroSq != null)
				throw new DuplicateHeroException();
			
			cntnt = creator.hero(heroName, chctrListener);
			chctrs.add((Character) cntnt);
			break;

		case MONSTER:
			try {
				cntnt = parserMonster(MnstrType, level);
				chctrs.add((Character) cntnt);
			} catch (IllegalArgumentException e) { /* IOException */
				throw new WrongDataException(file.getLineNumber());
			}
			break;

		default:
			throw new WrongDataException(file.getLineNumber());
		}	
		
		if (square == null){
			square = creator.passableSquare(cntnt, x, y);
			if ( cntnt instanceof Hero )
				heroSq = (PassableSquare) square;
			cntnt.setContainer((PassableSquare) square);
		}
		
		return square;
	}
	

	/* Dado un tipo de monstruo y un nivel retorna una instancia del mismo */
	private Monster parserMonster(int MnstrType, int level) 
		throws IllegalArgumentException, IOException {		
		
		float strengthCte = 0, healthCte = 0;
		String name;
		final int GOLEM = 1, DRAGON = 2, SNAKE = 3; 
		
		switch(MnstrType) {
		case GOLEM:
			strengthCte = 0.7F;
			healthCte = 1.0F;
			name = "Golem";
			break;
		case DRAGON:
			strengthCte = 1.0F;
			healthCte = 1.35F;
			name = "Dragon";
			break;
		case SNAKE:
			strengthCte = 1.0F;
			healthCte = 1.0F;
			name = "Snake";
			break;
		default:
			throw new IllegalArgumentException();
		}
		
		Monster monster;
		if ( level == 3 )
			monster = creator.monster(name, level, strengthCte, healthCte, chctrListener);
		else
			monster = creator.monster(name, level, strengthCte, healthCte);
		
		return monster;
	}

	
	/**
	 *  Cierra el archivo.
	 */
	public void closeFile() throws IOException {
		file.closeFile();
	}

	
	/**
	 *  Informa si siguen habiendo líneas con información.
	 */
	public boolean ready() throws IOException {
		return file.ready();
	}
	
	
	/**
	 *  Ignora la siguiente línea del archivo.
	 */
	public void ignoreLine() throws IOException {
		file.nextLine();
	}

	
	/**
	 *  Entraga una lista co todos los personajes que fueron instanciados hasta el
	 *  momento.
	 */
	public List<Character> getCharacters() {
		return chctrs;
	}

	
	/**
	 *  Entraga la celda en la cual se encuentra el héroe, si todavía no se ha instaciado
	 *  ningún héroe.
	 */
	public PassableSquare getHeroSquare() {
		return heroSq;
	}
	
	
	/**
	 *  Informa ya se ha encontrado algún héroe.
	 */
	public boolean hasHero() {
		if ( heroSq == null )
			return false;
		return true;
	}
	
}
