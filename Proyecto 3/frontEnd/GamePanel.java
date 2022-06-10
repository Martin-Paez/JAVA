package frontEnd;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import javax.swing.JPanel;
import backEnd.board.Chip;

/**
 * Panel que representa una grilla de imágenes, siendo posible agregarle y quitarle imágenes. Asimismo, cuenta con una
 * interfaz que permite a quien la utilice ser notificada cuando el usuario posiciona el mouse sobre una celda de la grilla.
 */
public class GamePanel extends JPanel {


	private static final long serialVersionUID = 1L;

	private final int SIZE = 8;
	private int cellSize;
	private Image[][] images;
	private Image whiteChipImg, blackChipImg;
	
	/**
 	 * Crea un nuevo panel con las dimensiones indicadas.
	 * 
	 * @param SIZE Cantidad de filas.
	 * @param SIZE Cantidad de columnas.
	 * @param cellSize Ancho y alto de cada imagen en píxeles.
	 * @param listener Listener que será notificado cuando el usuario se posicione sobre una celda de la grilla.
	 * @param color Color de fondo del panel.
	 */
	public GamePanel(final int cellSize, final GamePanelListener gpListener) throws IOException {

		setSize(SIZE * cellSize, SIZE * cellSize);
		images = new Image[SIZE][SIZE];
		this.cellSize = cellSize;
		whiteChipImg = ImageUtils.loadImage("resources/whiteChipFile.png");
		blackChipImg = ImageUtils.loadImage("resources/blackChipFile.png");
		
		Image whiteBackgroundImg = ImageUtils.loadImage("resources/whiteBackgroundFile.png");
		Image blackBackgroundImg = ImageUtils.loadImage("resources/blackBackgroundFile.png");
		
		for (int r = 0; r < SIZE; r++) {
			for (int c = 0; c < SIZE; c++) {
				if ( (r % 2 == 0 && c % 2 == 0)  || (c % 2 == 1 && r % 2 == 1) )
					images[r][c] = whiteBackgroundImg;
				else
					images[r][c] = blackBackgroundImg;
			}
		}
		
		addMouseListener( new MouseListener() {
			GamePanelListener listener = gpListener;
			
			@Override
			public void mousePressed(MouseEvent e) {
				int row = e.getY() / cellSize;
				int column = e.getX() / cellSize;

				if (row >= SIZE || column >= SIZE || row < 0 || column < 0) {
					return;
				}
				
				listener.play(row, column);
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {}
			
			@Override
			public void mouseReleased(MouseEvent arg0) {}
		});
		
		repaint();
	}

	public void put(Chip chip) {
		int r = chip.getFil(), c = chip.getCol(); 
		if ( chip.isWhite() )
			images[r][c] = ImageUtils.overlap(images[r][c], whiteChipImg);
		else
			images[r][c] = ImageUtils.overlap(images[r][c], blackChipImg);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.BLACK);
		int aux = SIZE * cellSize; 
		g.fillRect(0, 0, aux, aux);
	
		for (int r = 0; r < SIZE; r++) {
			for (int c = 0; c < SIZE; c++) {	 
				g.drawImage(images[r][c], c * cellSize, r * cellSize, null);
			}
		}
	}
	
}