package frontEnd.content.drawable;

import java.awt.Image;

import backEnd.content.character.Hero;
import backEnd.content.character.Monster;
import backEnd.dungeon.square.PassableSquare;
import backEnd.dungeon.square.PlayableContent;

import frontEnd.ImageUtils;

public class FrontPassableSquare extends PassableSquare implements Drawable {
	
	private String background = "resources/background.png";
	private String bloodImage = "resources/blood.png";	
	private boolean bloodStained = false;
	
	
	public FrontPassableSquare(PlayableContent content, int x, int y) {
		super(content, x, y);
	}
	
	
	public FrontPassableSquare(int x, int y) {
		super(x, y);
	}

	
	/**
	 *  Retorna la imágen asociada a la celda. Dependiendo de lo ocurrido en el
	 *  transcurso del juego, se agrega la imágen de la sangre y/o la de su 
	 *  contenido. Si el casillero no es visible el método retorna null.
	 */
	@Override
	public Image getImage() throws Exception{
		
		Image img = null;
		if ( isVisible() ) {
			img = ImageUtils.loadImage(background);
			if ( bloodStained )
				img = ImageUtils.overlap(img, ImageUtils.loadImage(bloodImage));
			if ( !isEmpty() ) 
				img = ImageUtils.overlap(img, ((Drawable)getContent()).getImage());
		}
		
		return img;
	
	}
	
	
	/**
	 *  Se elimina el contenido de la celda , si el mismo es un personaje
	 *  se la "mancha con sangre".
	 */
	/* OBS: Se aprobecha el hecho de que el "Front conoce al Back" y que la
	 * la accion de ensangrentar la celda es un "efecto visual" determinado
	 * por el proceso llevado a cabo en el back durante el transcurso del juego
	 * sin que el mismo tenga relevancia en lo que a la lógica se refiere.
	 */
	@Override
	public void removeContent() {
	
		PlayableContent content = getContent();
		if ( content instanceof Hero || content instanceof Monster ) {
			bloodStained = true;
		}
		super.removeContent();
	
	}
	
	
}
