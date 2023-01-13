package frontEnd.frames.mainFrame;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import backEnd.content.character.Hero;
import backEnd.content.character.Monster;


public class StatusPanel extends JPanel {
	final int NAME = 0, HEALTH = NAME + 2, STRENGHT = HEALTH + 1;
	final int LEVEL = STRENGHT + 1, EXPERIENCE = LEVEL + 1;
	final int MONSTER = EXPERIENCE + 3;
	final int SIZE = MONSTER + NAME + HEALTH + LEVEL + EXPERIENCE;
	JLabel label[] = new JLabel[SIZE];
	final int HEIGHT = SIZE * 15;
	
	/**
	 *  Crea el panel con el estado del jugador y/o monstruo indicado.
	 *  
	 * @param width		-	Ancho del panel
	 */
	public StatusPanel(int width) {
		setSize(width, HEIGHT);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout( SIZE, 0));

		for (int i = 0; i < label.length; i++) {
			label[i] = new JLabel("");
			panel.add(label[i]);
		}
		add(panel,BorderLayout.SOUTH);
	
	}

	/**
	 *	Imprime el estado del héroe recivido como parámetro
	 */
	public void refreshPlayerLabels(Hero hero) {
		label[NAME].setText("Nombre:  " + hero.getName());
		label[HEALTH].setText("Salud:  " + hero.getHealth() + "/" + hero.getMaxHealth() );
		label[STRENGHT].setText("Fuerza:  " + hero.getStrength() );
		label[LEVEL].setText("Nivel:  " + hero.getLevel() );
		label[EXPERIENCE].setText("Experiencia:  " + hero.getExperience() + "/" + hero.getLevel() * 5);
	}
	
	/**
	 *	Imprime el estado del monstruo recivido como parámetro
	 */
	public void refreshMonsterLabels(Monster monster) {
		label[NAME + MONSTER].setText("Nombre:  " + monster.getType());
		label[HEALTH + MONSTER].setText("Salud:  " +  monster.getHealth() + "/" + monster.getMaxHealth() );
		label[STRENGHT + MONSTER].setText("Fuerza:  " + monster.getStrength() );
		label[LEVEL + MONSTER].setText("Nivel:  " + monster.getLevel() );
	}
	
	/**
	 *	Borra la información respectiva al monstruo.
	 */
	public void clearMonsterLabels() {
		label[NAME + MONSTER].setText("");
		label[HEALTH + MONSTER].setText("");
		label[STRENGHT + MONSTER].setText("");
		label[LEVEL + MONSTER].setText("");
	}
	
	public int getHeight() {
		return HEIGHT;
	}
	
}
