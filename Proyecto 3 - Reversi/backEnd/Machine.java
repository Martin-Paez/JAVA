package backEnd;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import backEnd.board.Board;
import backEnd.board.Chip;
import backEnd.board.MoveIterator;

public class Machine {
	private boolean prune, depth, drawTree;
	private double difficulty;
	/* Inicializo gameTree para que cominece a armar el arbol por las negras */
	private Node gameTree = new Node( null );
	private Board board;
	private final Node OUT_OF_TIME = null;
	private Timer withoutTimeControl = new Timer() {
											@Override
											public boolean outOfTime() {
												return false;
											}};
	
	public Machine(Board board, boolean prune, boolean drawTree, boolean depth, double difficulty) {
		this.board = board;
		this.prune = prune;
		this.difficulty = difficulty;
		this.depth = depth;
		this.drawTree = drawTree;
	}
	
	/* Retorna false si no puede realizar ninguna jugada */
	public boolean play(Chip lastOpponentsMove) {
		boolean withoutChanges = true;
		
		if ( lastOpponentsMove != null ) {	
			if (gameTree.children.size() > 0 ) {			
				for (Node child : gameTree.children) {
					if ( child.move.equals(lastOpponentsMove) ) {
						gameTree = child;
						withoutChanges = false;
					}
				}
				if ( withoutChanges )
					throw new RuntimeException("Invalid opponent move");
			} else {
				/* La primer jugada del partdo */
				gameTree.move = lastOpponentsMove;
				gameTree.board = board.clone();
			}
		} 
		
		Node best = play();
		if ( best.move == gameTree.move ) 	 /* Si gameTree no tiene hijos */
			/* Puede ser que termino la partida o que no tengo movimientos */
			return false;
		board.play( best.move );

/* TODO PARA DEBUGGER */ 
		gameTree.boardHeuristic= best.boardHeuristic;
		gameTree.best = best;
		try {
			drawTree();
		} catch (IOException e) {
			throw new RuntimeException("Error al crear e arbol");
		}
 /*+++++++++++++++++ */
		
		gameTree = best;
		return true;

	}
	
	public String quickPlay(boolean playWhites) throws IOException {
		gameTree.board = board.clone();
		gameTree.move = new Chip(-1, -1, playWhites);
		Node best = play();
		gameTree.boardHeuristic= best.boardHeuristic;
		gameTree.best = best;
		
		String out;
		if ( best.move == gameTree.move )	/* Si gameTree no tiene hijos */
			/* Puede ser que termino la partida o que no tengo movimientos */
			out = "PASS";
		else
			out = "(" + best.move.getFil() + "," + best.move.getCol() + ")";
		
		if ( drawTree )
			drawTree();
		
		return out;
	}
	
	public Node play() {
		final int LEVEL = 0;
		Node best = gameTree;
		if ( depth )
			best = play(gameTree, LEVEL, null, difficulty, withoutTimeControl);
		else {
			int lowerLevel = 0;
			Node result = best;
			do {
				best = result;
				result = play(gameTree, LEVEL, null, ++lowerLevel, new BaseTimer(difficulty));
			} while ( result != OUT_OF_TIME );
			if ( best.move == gameTree.move ) {
				switch ( gameTree.children.size() ) {
				case 0:
					if ( ! gameTree.completeTree )
						throw new RuntimeException("El tiempo no fue suficiente para encontrar una jugada");
				case 1:
					best = gameTree.children.get(0);
					break;
				default:
					best = gameTree.best;
				}
			}
		}
		return best;
	}

	public Node play(Node tree, int level, Double alphaBeta, double lowerLevel, Timer timer) {
		boolean calcHeuristic = true, outOfTime;
		Node best = null, node = null;			 
		
		if ( level == lowerLevel ) {
			tree.boardHeuristic = tree.board.calcHeuristic() * ( (tree.move.isWhite()) ? 1:-1 );
			return (Node) tree.clone();
		}
		Chip next = null;
		boolean flag = false;
		Iterator<Node> childs = tree.children.iterator();
		MoveIterator moves = tree.board.moves( ! tree.move.isWhite() );
		
		if ( childs.hasNext() )
			flag = true;
		else
			next = moves.next();
			
		Node child;
		while ( !(outOfTime = timer.outOfTime()) ) {
			if ( flag ) {
				child = childs.next();
				if ( !childs.hasNext() ) {
					flag = false;
					next = moves.next();
				}
			} else if ( ! tree.completeTree && next != null) {		
				child = new Node(next);
				Chip nextCpy = next;
				if ( (next = moves.next()) == null ) 
					child.board = tree.board;
				else
					child.board = tree.board.clone();
				child.board.play(nextCpy);
				tree.children.add(child);
			} else 
				break;
			
			if ( calcHeuristic ) {
				child.best = play(child, level + 1, (best == null) ? alphaBeta : best.boardHeuristic, lowerLevel, timer);
				if ( child.best == OUT_OF_TIME )
					return OUT_OF_TIME;
				if ( best == null || child.best.boardHeuristic < best.boardHeuristic ) {
					best = child.best;
					node = child;
				}
				/* No poner antes del condicional la siguiente instruccions, ya que
				 * para las hojas se cumple que child = child.best */
				child.boardHeuristic = child.best.boardHeuristic * -1;
				if ( prune && alphaBeta != null && child.boardHeuristic >= alphaBeta )
					calcHeuristic = false;	
			} else
				child.isPruned = true;
		}
		
		if ( outOfTime )
			return OUT_OF_TIME;
	
		tree.completeTree = true;
		if ( tree.children.size() == 0 ) {
			tree.boardHeuristic = tree.board.calcHeuristic() * ( (tree.move.isWhite()) ? -1:1 );
			return tree;
		}

		return node;
	}
	
	private void drawTree() throws IOException {
		BufferedWriter file = new BufferedWriter(new FileWriter("tree.dot"));
		file.write("digraph { \n");
		final int NAME = 0;
		file.write(NAME + " [label=\"START " + String.format("%.3g%n", gameTree.boardHeuristic)  + "\"];\n");
		drawTree(gameTree, file, NAME);
		file.write("}");
		file.close();
	}

	private int drawTree(Node father, BufferedWriter file, int fatherName) throws IOException {
		int childName = fatherName;
		for (Node child : father.children) {
			file.write("" + ++childName + " [" );
			if ( father.move.isWhite() ) {
				file.write("shape=box, height=0.18, fontsize=12, ");
			}
			file.write("label=\"(" + child.move.getFil() + "," + child.move.getCol() + ")");
			if ( child.isPruned ) {
				file.write( "\" style=filled color=\"dodgerblue\" fillcolor=\"azure3\"" );
				child.isPruned = false;
			} else {
				file.write(" " + String.format("%.3g%n", child.boardHeuristic) + "\"");
				if  ( child.equals(father.best) )
					file.write( "style=filled color=\"dodgerblue\" fillcolor=\"brown3\"" );
				else
					child.best = null;
			}
			file.write("];\n");
			file.write(fatherName + " -> " + childName + ";\n");
			childName = drawTree(child, file, childName);
		}
		return childName + 1;
	}

	private interface Timer {
		public boolean outOfTime();
	}
	
	private class BaseTimer implements Timer{
		double initial, milliSeconds;
		
		public BaseTimer(double seconds) {
			initial = System.currentTimeMillis();
			this.milliSeconds = seconds * 1000;
		}
		
		@Override
		public boolean outOfTime() {
			return System.currentTimeMillis() - initial >= milliSeconds;
		}
		
	}
	
	private class Node implements Cloneable {
		ArrayList<Node> children = new ArrayList<Node>();
		Board board;
		Chip move;
		Double boardHeuristic = null;
		boolean completeTree = false, isPruned = false;
		Node best;
		
		public Node(Chip move) {
			this.move = move;
		}
		
		@Override
		protected Object clone() {
			Object obj = null;
			try {
				obj = super.clone();	
			} catch (Exception e) {}
			return obj;
		}
	}

}