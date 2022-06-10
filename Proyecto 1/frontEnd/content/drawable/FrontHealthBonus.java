package frontEnd.content.drawable;

import java.awt.Color;
import java.awt.Image;
import java.io.IOException;

import backEnd.content.items.HealthBonus;

import frontEnd.ImageUtils;

public class FrontHealthBonus extends HealthBonus implements Drawable {
	private String imageFile = "resources/healthBoost.png";
	
	
	public FrontHealthBonus(int bonus) {
		super(bonus);
	}

	
	/**
	 *  Retorna la im�gen asociada al �tem con su correspondiente valor.
	 */
	@Override
	public Image getImage() throws IOException {
		
		Image img = ImageUtils.loadImage(imageFile);
		return ImageUtils.drawString(img, bonus + "", Color.ORANGE);
		
	}

}
