package frontEnd;

/**
 * Listener para eventos ocurridos en el GamePanel.
 */
public interface GamePanelListener {

	/**
	 * Notifica que el usuario a elijido una posicion para jugar su fichas.
	 */
	public void play(int row, int column);
}
