package frontEnd.content.drawable;

import java.awt.Image;
import java.io.IOException;

import backEnd.dungeon.square.Square;

import frontEnd.ImageUtils;

public class Wall extends Square implements Drawable  {
	
	private String image = "resources/wall.png";
	
	
	public Wall(int x, int y) {
		super(x,y);
	}

	
	/**
	 *  Retorna la imágen asociada a la pared. Si la mismo no es visible
	 *  el método retorna null.
	 */
	@Override
	public Image getImage() throws IOException {
	
		if ( isVisible() )
			return 	ImageUtils.loadImage(image);	
		return null;
	
	}

}
