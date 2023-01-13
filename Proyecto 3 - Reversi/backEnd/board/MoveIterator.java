package backEnd.board;

/*
 * Clase que itera sobre las posiciones donde puede jugarse una ficha. No
 * se asegura que entre el conjunto de posiciones se encuentren aquellas
 * que son simetricas. Tampoco se asegura el resultado si se realizan 
 * cambios en el tablero.
 */
public abstract class MoveIterator {

	/* 
	 * Determina el color de ficha a jugar 
	 */
	boolean playWhites;
	
	public MoveIterator(boolean playWhites) {
		this.playWhites = playWhites;
	}
	
	/*
	 * Retorna la siguiente posicion donde se puede jugar la fichas del
	 * color determinado por la variable playWhites.  No se asegura que
	 * entre el conjunto de posiciones se encuentren aquellas que son 
	 * simetricas.
	 */
	public abstract Chip next();
	
}
