package backEnd.fileManagement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import frontEnd.content.drawable.FrontHero;

import backEnd.Dungeon;
import backEnd.DungeonListener;
import backEnd.content.character.Character;
import backEnd.content.character.CharacterListener;


public class LoadGame {
	
	
	/**
	 *  Carga un juego desde un archivo que fué guardado utilizando una instancia
	 *  de la clase SaveGame.
	 *  
	 * @param fileName	-	Path y nombre del archivo previamente devuelto por una instancia de la clase SaveGame.
	 * @param revealListener	-	Listener que aplicado al dungeon que va a ser instanciado.
	 * @param characterListener		-	Listener aplicado a los personajes contenidos en el tablero.
	 * 
	 * @return	-	Una instancia de la clase Dungeon.
	 * 
	 */
	public Dungeon loadGame( String fileName, DungeonListener revealListener,
			CharacterListener characterListener ) throws FileNotFoundException,
			IOException, ClassNotFoundException {
		
		File fichero = new File(fileName);	
		ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(fichero));
		
		Dungeon dungeon = (Dungeon) entrada.readObject();
		dungeon.setListener(revealListener);
		
		for (Character chctr : dungeon.getCharacters()) {
			if ( chctr.getLevel() == 3 || chctr instanceof FrontHero ){
				chctr.setListener(characterListener);
			}
		}
		
		entrada.close();
		
		return dungeon;
	}

}
