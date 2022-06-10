package frontEnd.content.drawable;

import java.awt.Color;
import java.awt.Image;
import java.io.IOException;

import backEnd.content.character.CharacterListener;
import backEnd.content.character.Hero;

import frontEnd.ImageUtils;

public class FrontHero extends Hero implements Drawable {
	private String imageFile = "resources/hero.png";
	
	public FrontHero(String name,  CharacterListener listener) {
		
		super(name, listener);
	}

	/**
	 *  Retorna la imágen asociada al héroe con su correspondiente nivel.
	 */
	public Image getImage() throws IOException {
		
		Image img = ImageUtils.loadImage(imageFile);
		return ImageUtils.drawString(img, level + "", Color.WHITE);
		
	}

}
