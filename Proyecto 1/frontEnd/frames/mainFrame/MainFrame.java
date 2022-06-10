package frontEnd.frames.mainFrame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;


import backEnd.Dungeon;
import backEnd.DungeonListener;
import backEnd.content.character.Character;
import backEnd.content.character.CharacterListener;
import backEnd.content.character.Hero;
import backEnd.content.character.Monster;
import backEnd.dungeon.square.PassableSquare;
import backEnd.dungeon.square.PlayableContent;
import backEnd.dungeon.square.Square;
import backEnd.fileManagement.LoadGame;
import backEnd.fileManagement.SaveGame;
import backEnd.fileManagement.dungeonLoader.DungeonLoader;
import exceptions.DuplicateHeroException;
import exceptions.DuplicateName;
import exceptions.DuplicateSquareException;
import exceptions.FileFormatException;
import exceptions.HeroNotFoundException;
import exceptions.WrongDataException;
import frontEnd.content.drawable.Drawable;
import frontEnd.content.drawable.ErrorListener;
import frontEnd.content.drawable.FrontCreator;
import frontEnd.content.drawable.FrontPassableSquare;
import frontEnd.frames.boardsListFrame.BoardsListFrame;
import frontEnd.frames.boardsListFrame.BoardsListListener;


public class MainFrame extends JFrame {

	private static final int CELL_SIZE = 30;
	private GamePanel gp;
	private StatusPanel statusPanel;
	Dungeon dungeon;
	String heroName = "";
	String fileName = null;
	private Monster monsterInRange = null;
	private boolean gameLoaded = false;
	
	
	public MainFrame(int x, int y) {

		setTitle("Desktop Dungeon");
		setLayout(new BorderLayout());
		setBounds(x, y,  330, 200);

		JMenu menu = new JMenu("Menu");
		String[] text = { "Nuevo juego", "Reiniciar", "Cargar", "Guargar", "Salir" };
		JMenuItem[] item = new JMenuItem[text.length];
		for(int i = 0; i < text.length; i++) {
			item[i] = new JMenuItem(text[i]);
			menu.add(item[i]);
		}
		
		
		int NEW = 0, RESET = 1, LOAD = 2, SAVE = 3, EXIT = 4;
		
		item[NEW].addActionListener( new newGameOptListener() );		
		item[RESET].addActionListener( new ResetOptListener() );
		item[SAVE].addActionListener( new SaveOptListener());
		item[LOAD].addActionListener(new LoadOptListener());		
		item[EXIT].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(menu);
		setJMenuBar(menuBar);

	}
	

/* ------------------------------------- Listeners ------------------------------------------ */

	
	/* Listener que esta a la espera de que alguno de los personajes relevantes
	 * muera para terminar el juego.
	 */
	private class GameOverListener implements CharacterListener {
		
		@Override
		public void die(Character character) {
			
			Square sq = character.getContainer();
			try {
			gp.put( ((FrontPassableSquare)sq).getImage(), sq.getX(), sq.getY());
			} catch (Exception e) {
				(new ErrorMsg()).fatalError("IO Error");
			}
			
			gp.repaint();
			if ( character instanceof Hero )
				JOptionPane.showMessageDialog(null, "FIN DEL JUEGO");
			else
				JOptionPane.showMessageDialog(null, "HAS GANADO");
			remove(gp);
			KeyListener listener[] = getKeyListeners();
			removeKeyListener(listener[0]);
		}

	}
	
	
	/* Listener que esta a la espera de los eventos producidos en el dungeondurante
	 * un movimiento del personaje.
	 */
	private class GameListener implements DungeonListener {
		
		@Override
		public void revealSquare(Square sq) {
			try {
				gp.put( ((Drawable)sq).getImage() , sq.getX(), sq.getY());
			} catch (Exception e) {
				(new ErrorMsg()).fatalError("IO Error");
			}
		}

		@Override
		public void monsterInRange(Monster monster) {
			monsterInRange = monster;
			statusPanel.refreshMonsterLabels(monster);
		}
		
	}
		
	
	/* Listener que se activa cuando el usuario presiona una tecla. Actualiza el estado
	 * de la panatalla y llama al BanckEnd para avisarle lo sucedido.
	 */
	private void MovListener(KeyEvent e) {
		
		FrontPassableSquare heroSq = (FrontPassableSquare) dungeon.getHero().getContainer();		
		int x = heroSq.getX();
		int y = heroSq.getY();
		
		switch(e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			y--; break;
		case KeyEvent.VK_RIGHT:
			y++; break;
		case KeyEvent.VK_UP:
			x--; break;
		case KeyEvent.VK_DOWN:
			x++; break;	
		}

		if ( dungeon.inBounds(x, y) ) {
			statusPanel.clearMonsterLabels();
			monsterInRange = null;
			Square target = dungeon.getSquare(x, y);
			dungeon.movHero(x, y);
			try {
			gp.put( ((Drawable)target).getImage() , target.getX(), target.getY());
			gp.put( ((Drawable)heroSq).getImage() , heroSq.getX(), heroSq.getY());
			} catch (Exception error) {
				(new ErrorMsg()).fatalError("IO Error");
			}
			statusPanel.refreshPlayerLabels(dungeon.getHero());
			gp.repaint();
		}
		
	}
	
	
	/* Listener que se activa cuando el usuario mueve el mouse sobre el GamePanel. Se
	 * encarga de imprimir los datos referentes al monstruo sobre el cual el mouse
	 * se encuentra posicionado, o borrarlos en caso contrario.
	 */
	private class BaseGamePanelListener implements GamePanelListener {
		
		@Override
		public void onMouseMoved(int x,int y) {
			if ( ! dungeon.inBounds(x, y) )
				throw new RuntimeException();
			
			Square sq = dungeon.getSquare(x, y);
			PlayableContent cntnt = null;
			
			if ( sq instanceof PassableSquare && sq.isVisible() &&
				!((PassableSquare)sq).isEmpty() &&
				( cntnt = ((PassableSquare)sq).getContent() ) instanceof Monster ) 
			{	
				statusPanel.refreshMonsterLabels((Monster)cntnt);
			}
			else if ( monsterInRange == null )
				statusPanel.clearMonsterLabels();
			else
				statusPanel.refreshMonsterLabels(monsterInRange);
			
		}
		
	}
	
	
	/* Listener que se activa cuando el usuario presiona el boton OK en la ventana
	 * creada para elejir el tablero y el nombre del juegador. Da comienzo al juego
	 * llamando a los encargados de activar los listener, inicializar los paneles y
	 * cargar una nueva instancia de la clase Dungeon desde el archivo que contiene
	 * un mapa y cuyo nombre es recivido como parámetro.
	 */
	private class BoardsFrameListener implements BoardsListListener {

		@Override
		public void okBotton(String fileName, String heroName) {
			try {			
				MainFrame.this.fileName = fileName;
				MainFrame.this.heroName = heroName;
				DungeonLoader loader = new DungeonLoader(fileName, heroName, new GameOverListener(),
							new GameListener(), new FrontCreator(new ErrorMsg()));
			
				createPanels(loader.getRows(), loader.getColumns());
				dungeon = loader.getDungeon();				
				initGame();
				gameLoaded = false;
	
			} catch ( FileFormatException e ) {
				(new ErrorMsg()).fatalError("Error de Formato." + e.getMessage());
			} catch ( WrongDataException e ) {
				(new ErrorMsg()).fatalError("Datos inconsistentes" + e.getMessage());
			} catch ( IOException e ) {
				(new ErrorMsg()).fatalError("IO Error");
			} catch (DuplicateHeroException e) {
				(new ErrorMsg()).fatalError("Demasiados héroes");
			} catch (DuplicateSquareException e) {
				(new ErrorMsg()).fatalError("Casilleros duplicados");
			} catch (HeroNotFoundException e) {
				(new ErrorMsg()).fatalError("No hay ningún héroe");
			}
		}
		
	}
	
	
	/* Listener asociado al boton Nuevo Juego en la barra de menús. Abre una nueva 
	 * ventana para poder elejir el nombre del héroe y el tablero.
	 */
	private class newGameOptListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			BoardsListFrame frame;
			try {
				frame = new BoardsListFrame(new BoardsFrameListener());
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			} catch (FileNotFoundException e1) {
				(new ErrorMsg()).fatalError("No hay mapas. Por enunciado del tp se aborta el programa");
			} catch (WrongDataException e1) {
				(new ErrorMsg()).fatalError("Uno de los mapas no tiene nombre. Por enunciado del tp se aborta el programa");
			} catch (IOException e1) {
				(new ErrorMsg()).fatalError("IO Error");
			} catch (DuplicateName e1) {
				(new ErrorMsg()).fatalError("Hay dos mapas con el mismo nombre. Es irrelevante para el diseño del tp. Se decide abortar");
			}
		}
		
	}
	
	
	/* Listener asociado al boton Reiniciar en la barra de menús. Dependiendo de como
	 * fue cargado el juego la última vez, llama al encargado de levantar una partida
	 * guardada o al cuya función es levantar un mapa nuevo.
	 */
	private class ResetOptListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if ( fileName == null ) 
				return;
			else if ( heroName == null )
				throw new RuntimeException();
		
			if ( gameLoaded == false ) {
				(new BoardsFrameListener()).okBotton(fileName, heroName);
			}
			else  {
				loadGame();
			}
		}
		
	}
	

	/* Listener asociado al boton Cargar en la barra de menús. Abre una nueva 
	 * ventana para poder elejir el archivo y llama al encargado de levantar el
	 * archivo e inicializar el juego.
	 */
	private class LoadOptListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			JFileChooser chooser = new JFileChooser();
			if( chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
				return;
			
			fileName = chooser.getSelectedFile().getAbsolutePath();
			loadGame();
		}
	
	}
	
		
	/* Listener asociado al boton Guardar en la barra de menús. Abre una nueva 
	 * ventana para poder elejir donde guardar el archivo, verifica que no sea
	 * guardado en la carpeta /boards y lo guara.
	 */
	private class SaveOptListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
		
			if( gp == null)
				return;

			JFileChooser chooser = new JFileChooser();
			if(chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION)
				return;

			File chooserFile = new File(chooser.getSelectedFile().getPath());
			File boardsFolder = new File("boards");
			if ( chooserFile.getParent().equals(boardsFolder.getAbsolutePath())) {
				JOptionPane.showMessageDialog(null, "No está permitido guardar en esta carpeta");
				return;
			}

			SaveGame save = new SaveGame();
			
			try {
				save.saveGame( dungeon, chooser.getSelectedFile().getAbsolutePath());
			} catch (IOException e1) {
				(new ErrorMsg()).fatalError("IO Error");
			}				
		}
	
	}

	
	/* Imprime un mensaje de error y finaliza la aplicaicón.
	 */
	private class ErrorMsg implements ErrorListener {
	
		@Override
		public void fatalError(String errorMsg) {
			JOptionPane.showMessageDialog(null, errorMsg);
			System.exit(0);			
		}
	
	}
	
	
/* --------------------------------------- Métodos privados ---------------------------------- */

	
	/* Carga la partida guardadaen el archivo cuyo nombre se enuentra en la vairable
	 * de instacia fileName. Luego llama al encargado de inicializar el juego
	 */
	private void loadGame() {
		
		if ( fileName == null )
			throw new RuntimeException();
		
		LoadGame load = new LoadGame();
		
		try {
			dungeon = load.loadGame(fileName, new GameListener(), new GameOverListener());
		} catch (FileNotFoundException e1) {
			(new ErrorMsg()).fatalError("No se encontró el archivo");
		} catch (IOException e1) {
			(new ErrorMsg()).fatalError("IO Error");
		} catch (ClassNotFoundException e1) {
			(new ErrorMsg()).fatalError("Error al cargar el archivo");
		}
		
		createPanels(dungeon.getRows(), dungeon.getColumns());
				
		for ( int i = 0; i < dungeon.getColumns(); i++) {
			for ( int j = 0; j < dungeon.getRows(); j++) {
				Square sq = dungeon.getSquare(i, j);
				try {
					gp.put( ((Drawable)sq).getImage() , sq.getX(), sq.getY());
				} catch (Exception err) {
					(new ErrorMsg()).fatalError("IO Error");
				}
			}
		}
		initGame();
		gameLoaded = true;
	}


	/* Actualiza la información en pantalla y activa el listener del teclado.
	 */
	private void initGame() {
		gp.repaint();
		
		statusPanel.refreshPlayerLabels(dungeon.getHero());
		
		setSize(gp.getWidth() + statusPanel.getWidth(),
				Math.max( gp.getHeight() + 55,  statusPanel.getHeight()) );
		
		KeyListener listener[] = getKeyListeners();
		if ( listener.length == 0 ) {
			addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(final KeyEvent e) {
						MovListener(e);
				}
			});
		}
	}


	/* Crea nuevos paneles de estado y juego.
	 */
	private void createPanels(int rows, int columns) {
		final int STATUS_WIDTH = 130;
		
		if ( gp != null && statusPanel != null ) {
			remove(gp);
			remove(statusPanel);
		}	
		
		statusPanel = new StatusPanel( STATUS_WIDTH );
		getContentPane().add(statusPanel, BorderLayout.EAST);
		
		gp = new GamePanel(rows, columns, CELL_SIZE, new BaseGamePanelListener(), Color.BLACK);
		getContentPane().add(gp, BorderLayout.CENTER);
		
	}

}
