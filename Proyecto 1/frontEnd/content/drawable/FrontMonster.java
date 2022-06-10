package frontEnd.content.drawable;

import java.awt.Color;
import java.awt.Image;
import java.io.IOException;

import backEnd.content.character.CharacterListener;
import backEnd.content.character.Monster;

import frontEnd.ImageUtils;

public class FrontMonster extends Monster implements Drawable {
	
	private String imageFile;
	
	
	public FrontMonster(String type, int level, float strengthCte, float healthCte,
			String imageFile) {
		
		super(type, level, strengthCte, healthCte);
		this.imageFile = imageFile;
		
	}

	
	public FrontMonster(String type, int level, float strengthCte, float healthCte,
			String imageFile, CharacterListener listener) {
		
		this(type, level, strengthCte, healthCte, imageFile);
		this.listener = listener;
		
	}

	
	/**
	 *  Retorna la imágen asociada al monstruo con su correspondiente nivel.
	 */
	public Image getImage() throws IOException {
	
		Image img = ImageUtils.loadImage(imageFile);
		return ImageUtils.drawString(img, level + "", Color.WHITE);
	
	}


}