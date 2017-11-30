package com.thuckate.artificalIntellegence;

import com.thuckate.cocaro.Board;

public class BestMove {

	public BestMove() {
		// TODO Auto-generated constructor stub
	}
	 static void run (Board board) {
//	    int[] moves = new int[board.getAvailableMoves().size()];
//	    int index = 0;
//	
//	    for (Integer item : board.getAvailableMoves()) {
//	        moves[index++] = item; 
//	        
//	    }
//	     
//	    
//	    int randomMove = moves[new java.util.Random().nextInt(moves.length)];
//	    board.move(randomMove);
	    board.move(board.checkBestMove());
		  
	}
}
