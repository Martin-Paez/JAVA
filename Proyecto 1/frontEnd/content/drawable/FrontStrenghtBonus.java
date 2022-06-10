package frontEnd.content.drawable;

import java.awt.Color;
import java.awt.Image;
import java.io.IOException;

import backEnd.content.items.StrengthBonus;
import frontEnd.ImageUtils;

public class FrontStrenghtBonus extends StrengthBonus implements Drawable {
	
	private String imageFile = "resources/attackBoost.png";

	
	public FrontStrenghtBonus(int bonus) {
		super(bonus);
	}

	
	/**
	 *  Retorna la imágen asociada al ítem con su correspondiente valor.
	 */
	@Override
	public Image getImage() throws IOException {
		
		Image img = ImageUtils.loadImage(imageFile);
		return ImageUtils.drawString(img, bonus + "", Color.ORANGE);
	
	}

	
}
