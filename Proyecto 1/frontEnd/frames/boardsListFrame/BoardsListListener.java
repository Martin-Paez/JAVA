package frontEnd.frames.boardsListFrame;

public interface BoardsListListener {

	/**
	 *  Listener del boton OK perteneciente a {@link BoardsListFrame}
	 * 
	 * @param fileName	-	Nombre del archivo que fue seleccionado.
	 * @param heroName	-	Nombre ingresado por el usuario.
	 */
	public void okBotton(String fileName, String heroName);
	
}
