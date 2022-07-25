package backEnd.fileManagement.boardsNameReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import backEnd.fileManagement.FileLoader;

import exceptions.DuplicateName;
import exceptions.WrongDataException;


/**
 * Esta clase se encarga de identificar todos los mapas que se encuentran en la
 * carpeta /boards.
 * 
 */
public class BoardsNameReader {

	Map<String, String> boards = null; /* < Nombre, Nombre_del_archivo> */

	
	/**
	 * Retorna una pila con los nombres de todos los mapas que se encuentran en
	 * la carpeta /boards.
	 * 
	 * @throws WrongDataException
	 *             - Si ALGUNO de los mapas no tiene nombre.
	 * @throws FileNotFoundException
	 *             - Si no hay mapas en la carpeta /boards.
	 * @throws DuplicateName
	 *             - Si hay dos mapas con el mismo nombre en la carpeta /boards.
	 * @throws IOException
	 */
	public Stack<String> refreshBoards() throws WrongDataException,
			FileNotFoundException, IOException, DuplicateName {

		boards = new HashMap<String, String>();
		File file = new File("boards");
		Stack<String> names = new Stack<String>();

		String[] fileName = file.list();
		for (int i = 0; i < fileName.length; i++) {
			if (fileName[i].endsWith(".board")) {
				
				String text = handleName("boards" + File.separator + fileName[i]);
				if (this.boards.containsKey(text))
					throw new DuplicateName();
				names.add(text);
				this.boards.put(names.peek(), "boards" + File.separator
						+ fileName[i]);
			}
		}
		
		if (names.size() == 0)
			throw new FileNotFoundException();

		return names;

	}

	
	/**
	 * Retorna el nombre del archivo correspondiente al nombe del mapa. Antes de
	 * invocar a este método es necesario usar el método refreshBoards(), para
	 * actualizar la lista de mapas.
	 * 
	 * @param boardName
	 *            - Nombre del mapa.
	 * @return - Archivo que contiene al mapa.
	 */
	public String getFileName(String boardName) {
		return boards.get(boardName);
	}

	
	/**
	 *  Retorna el nombre del mapa dentro del archivo recivido como parámetro.
	 *
	 * @param fileName	-	Nombre del archivo.
	 * @return			-	Nombre del mapa.
	 *
	 * @throws WrongDataException	-	Si el mapa no tiene nombre.
	 * @throws IOException
	 */
	private String handleName(String fileName) throws IOException,
			WrongDataException {
		
		FileLoader file = null;
		String name;
		try {
			file = new FileLoader(fileName);
	
			file.nextLine(); /* Ignoro la dimensión */
			name = file.nextLine();
			if (name == null)
				throw new WrongDataException(file.getLineNumber());
		}finally {
			if ( file != null )
				file.closeFile();
		}
		return name;
	
	}

	
}
