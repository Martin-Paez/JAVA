package backEnd.fileManagement;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import backEnd.Dungeon;



public class SaveGame {
	
	
	/**
	 *  Guarda en el archivo de nombre 'fileName' una instancia de la clase Dungeon
	 *  con todo su contenido. Para cargar el dungeon desde el archivo se debe  usar
	 *  una instancia de la clase LoadGame.
	 */
	public void	saveGame( Dungeon dungeon, String fileName ) throws IOException{
		
		File fichero = new File(fileName);
		if( !fichero.exists()) {
			fichero.createNewFile();
		}
		
		ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(fichero));
		
		salida.writeObject( dungeon );
		salida.close();
		
	}
	
}

