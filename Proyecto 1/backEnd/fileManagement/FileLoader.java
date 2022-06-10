package backEnd.fileManagement;
import java.io.*;


/**
 *  Esta clase se encarga de abrir un archivo y devolver lineas sin comentarios,
 *  espacios ni tabulaciones. Se considera comentarios a todo lo que haya despu�s
 *  de un '#' en la l�nea seleccionada. Las lineas que solo contienen un salto de
 *  l�nea son ignoradas.
 *
 */

public class FileLoader {
	File file;
	BufferedReader buffer;
	int lineNumber;
	
	
	public FileLoader(String fileName) throws FileNotFoundException {
		
		file = new File(fileName);
		buffer = new BufferedReader(new FileReader(file));
		lineNumber = 0;
	
	}
	
	
	/**
	 * Retorna una l�nea del archivo que no sea un comentario, linea en blanco 
	 * ni tabulaci�n.
	 * Permite iterar sobre las l�neas de un archivo, cada llamado sucesivo
	 * retorna las l�neas comenzando por la primera hasta llegar a la �ltima. 
	 * Si ya no hay lineas que leer, retorna null.
	 * 
	 */
	public String nextLine() throws IOException {	
		
		String line;
		do {
			line = refineLine( buffer.readLine() );
			lineNumber++;
		}
		while ( line != null && line.length() == 0 );

		return line;
		
	}	
	
	
	/**
	 *  Informa si es posible leer m�s l�neas del archivo.
	 */
	final int LIMIT = 10000;
	public boolean ready() throws IOException{
		
		if ( buffer.ready() ) {
			buffer.mark(LIMIT);
			if ( nextLine() != null ) {
				buffer.reset();
				return true;
			}
		}
		return false;
		
	}

	
	/**
	 *  Cierra el archivo.
	 */
	public void closeFile() throws IOException{
		buffer.close();
	}
	
	
	public String getFileName() {
		return file.getName();
	}
	
	
	public String getLineNumber() {
		return lineNumber + "";
	}
	
	
	/* Retorna una linea sin comentarios, tabulaciones ni espacios. */
	private String refineLine(String line) {
		
		if ( line == null )
			return null;
		
		if (line.contains("#"))
			line = line.substring(0, line.indexOf("#"));
		
		return line.replaceAll(" ", "").replaceAll("	", "");
	
	}	
	
	
}
