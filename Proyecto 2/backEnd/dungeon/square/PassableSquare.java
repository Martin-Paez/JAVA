package backEnd.dungeon.square;

import backEnd.content.character.Hero;
import exceptions.EmptySquareException;


public class PassableSquare extends Square {

	private PlayableContent content;
	
	
	public PassableSquare(int x, int y) {
		super(x, y);
	}
	
	
	public PassableSquare(PlayableContent content, int x, int y) {
		
		this(x,y);
		if ( content == null )
			throw new RuntimeException("null no es un contenido válido");
		
		this.content = content;
		content.setContainer(this);
	
	}
	
	
	/**
	 *  Informa si la celda está vacía ( no tiene contenido )
	 */
	public boolean isEmpty() {
		return content == null;
	}

	
	/**
	 *  Elimina el contenido de la celda. Si la celda no tiene contenido no
	 *  pasa nada.
	 */
	public void removeContent() {
		content = null;
	}

	
	/**
	 *  Retorna el contenido del casillero. Tener en cuenta que si el contenido
	 *  fué asignado a otro contenedor ( sin importar si es un casillero ), 
	 *  entonces automáticamente se considera que el casillero dejo de contenerlo.  
	 *  
	 * @throws EmptySquareException - Si la celda no tiene contenido.
	 */
	public PlayableContent getContent() {
		
		if ( isEmpty() || content.getContainer() != this )
			throw new EmptySquareException();
		
		return content;
	
	}

	
	/**
	 *  Se intercambian contenidos entre dos celdas. Si solo una de las celdas
	 *  tiene contenido al finalizar la celda que no estaba vacía pasa a estarlo.
	 *  Si ninguna de las celdas tiene contenido no hay cambios.
	 *  
	 */
	public void swapContents(PassableSquare square) {
		
		PlayableContent cntn = null;
		if ( !square.isEmpty() && square.getContent().getContainer() == square ) {
			cntn = square.getContent();
			cntn.setContainer(this);
		}
		if ( !isEmpty() && content.getContainer() == this )
			content.setContainer(square);
		
		square.content = content;
		content = cntn;
		
	}
	
	/**
	 *  Se intercambian contenidos entre dos celdas. Si solo una de las celdas
	 *  tiene contenido al finalizar la celda que no estaba vacía pasa a estarlo.
	 *  Si ninguna de las celdas tiene contenido no hay cambios.
	 *  
	 */
	public void interact(PassableSquare heroSq) throws ClassCastException, EmptySquareException {
		
		if ( isEmpty() ) {
			swapContents(heroSq);
		}
		else
			content.interact((Hero) heroSq.getContent());
	
	}

}
