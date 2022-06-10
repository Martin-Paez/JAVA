package backEnd;

import backEnd.board.BoardListener;

public interface GameListener extends BoardListener {

	public void playerPlay();
	
	public void playerPass();
	
	public void pcPlay();
	
	public void pcPass();
	
	public void playerLose();
	
	public void playerWin();
	
	public void invalidMove();
	 
}
