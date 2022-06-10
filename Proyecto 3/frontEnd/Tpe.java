package frontEnd;

import java.io.IOException;

import javax.swing.JFrame;

import backEnd.Machine;
import backEnd.board.Board;
import backEnd.board.BoardListener;
import backEnd.board.Chip;

public class Tpe {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for (int gameMode=0; gameMode<args.length; gameMode++) {
			System.out.println(gameMode + " = " + args[gameMode]);
		}
		parser(args);
	}
	
	private static void parser(String[] args) {
		if ( args.length < 3 )
			throw new IllegalArgumentException();
		
		boolean tree = false, prune = false, depth = false;
		
		int param = 1;
		if (args[args.length - param].equals("-tree")) {
			tree = true;
			param++;
		}
		if (args[args.length - param].equals("-prune")) {
			prune = true;
			param++;
		}
		/* dif = indice para tipo de dificultad (depth, maxtime) */
		int index = args.length - param - 1; 
		if (args[index].equals("-depth")) {
			depth = true;
		} else if (! args[index].equals("-maxtime"))
			throw new IllegalArgumentException();
		/* dif = nivel de dificultad (segundos o altura, en función del valor de depth) */
		double dif = Double.valueOf( args[index + 1] );	
		
		int gameMode = 0;
		boolean illegalArgument = false;
		
		if (args[gameMode].equals("-visual") ) {
			if ( args.length < 3 || args.length > 5 || tree )
				throw new IllegalArgumentException();
			play( prune, depth, dif );
		} else if (args[gameMode].equals("-file") ) {
		
			if ( args.length < 6 || args.length > 8 )
				throw new IllegalArgumentException();
		
			if ( args[gameMode+2].equals("-player") ) {
				int color = Integer.valueOf(args[gameMode+3]);
				if ( color == 2 || color == 1 ) {
					play(args[gameMode+1], color == 1, prune, tree, depth, dif);
				} else
					illegalArgument = true;
			} else
				illegalArgument = true;
			
		} else
			illegalArgument = true;
		
		if ( illegalArgument )
			throw new IllegalArgumentException();
	}
	
	private static void play(String fileName, boolean playWhites, boolean prune, boolean drawTree,
			boolean depth, double difficulty) {
		
		Board board;
		try {
			board = new Board(fileName, new BoardListener() {
				@Override
				public void wakeUp(Chip chip) {}
			});
		} catch (IOException e1) {
			throw new RuntimeException("Error al cargar el tablero");
		}
		
		Machine pc = new Machine(board, prune, drawTree, depth, difficulty);
		try {
			System.out.println( pc.quickPlay(playWhites) );
		} catch (IOException e) {
			throw new RuntimeException("Error al crear el archivo tree.dot");
		}
	}
	
	private static void play(boolean prune, boolean depth, double difficulty) {
		Visual frame = new Visual(200, 150, prune, depth, difficulty);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
}
