package com.thuckate.cocaro;
import java.util.HashSet;
public class Board {
	static final int BOARD_WIDTH = 18;

    public enum State {Blank, X, O}
    private State[][] board;
    private State playersTurn;
    private State winner;
    private HashSet<Integer> movesAvailable;
    public TanCongPhongThu[] tcpt ;
    private int MangTC[] = {0, 2, 18, 162, 1458, 13122, 118098};
    private int MangPT[] = {0, 1, 9, 81, 729, 6561, 59049};
//    private int MangTC[] = {0, 3, 24, 192, 1536};
//    private int MangPT[] = {0, 1, 9, 81, 729};
    
    private int moveCount;
    private boolean gameOver;

	public Board() {
		 board = new State[BOARD_WIDTH][BOARD_WIDTH];
	     movesAvailable = new HashSet<>();
	     tcpt = new TanCongPhongThu[BOARD_WIDTH*BOARD_WIDTH];
	     reset();
	}
  private void initialize () {
        for (int row = 0; row < BOARD_WIDTH; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                board[row][col] = State.Blank;
            }
        }

        movesAvailable.clear();
        
        for (int i = 0; i < BOARD_WIDTH*BOARD_WIDTH; i++) {
            movesAvailable.add(i);
        } 
    }
  void reset () {
      moveCount = 0;
      gameOver = false;
      playersTurn = State.X;
      winner = State.Blank;
      initialize();
  }
  public boolean move (int index) {
      return move(index% BOARD_WIDTH, index/ BOARD_WIDTH);
  }

  /**
   * Places an X or an O on the specified location depending on who turn it is.
   * @param x         the x coordinate of the location
   * @param y         the y coordinate of the location
   * @return          true if the move has not already been played
   */
  private boolean move (int x, int y) {

      if (gameOver) {
          throw new IllegalStateException("TicTacToe is over. No moves can be played.");
      }

      if (board[y][x] == State.Blank) {
          board[y][x] = playersTurn;
      } else {
          return false;
      }

      moveCount++;
      movesAvailable.remove(y * BOARD_WIDTH + x);

      // The game is a draw.
      if (moveCount == BOARD_WIDTH * BOARD_WIDTH) {
          winner = State.Blank;
          gameOver = true;
      }

      // Check for a winner.
      checkRow(y);
      checkColumn(x);
      checkDiagonalFromTopLeft(x, y); 
      checkDiagonalFromTopRight(x, y);
      
      //---Kiem tra tan cong
      
      
      
      
      //---------------------------------
      
      playersTurn = (playersTurn == State.X) ? State.O : State.X;
      return true;
  }
  public boolean isGameOver () {
      return gameOver;
  }
  public int checkBestMove(){
	   
   
//	    int[] moves = new int[getAvailableMoves().size()];
//	    int index = 0;
//	
//	    for (Integer item : getAvailableMoves()) {
//	        moves[index++] = item; 
//	        
//	    }
//	     
//	    
//	  int randomMove = moves[new java.util.Random().nextInt(moves.length)];
      int bestMove = 0, Tam = 0, max = 0, bestMoveLast ; 
      bestMoveLast = bestMove;
      for(int i = 0; i < BOARD_WIDTH; i++){
    	  for(int j = 0; j < BOARD_WIDTH; j++){
    		  if(board[i][j] == State.Blank){
    			  int diemPT = ktraPT(i, j);
    			  int diemTC = ktraTC(i, j);
//        		  System.out.println("Diem tc["+(j*BOARD_WIDTH+i)+"] = "+diemTC+", DiemPT = "+diemPT);  
        		  Tam = (diemTC >= diemPT) ? diemTC : diemPT;
        		  if(max < Tam && ktraTrungMoveAvai(i, j)) {
        			  max = Tam;
        			  bestMove = j*BOARD_WIDTH+i; 
        		  }
    		  }
    		  else 
    		  System.out.println("Board ["+i+"]["+j+"] = "+board[i][j]);
    		  
    	  }
      } 
      System.out.println("bestMoveLast:--------------------------------- "+bestMoveLast+"   (x, y) = "+bestMoveLast%BOARD_WIDTH+", "+bestMoveLast/BOARD_WIDTH);
      System.out.println("bestMove:--------------------------------- "+bestMove+"   (x, y) = "+bestMove%BOARD_WIDTH+", "+bestMove/BOARD_WIDTH);
      return bestMove; 
  }
  private boolean ktraTrungMoveAvai(int x, int y){
	  for(int item : getAvailableMoves())
		  if(item == (y*BOARD_WIDTH + x)) return true; 
	  return false;
  }
  private int ktraTC(int x, int y){
	  //tcpt[y*BOARD_WIDTH+ x].tc = ktraTC_Hang(y) +ktraTC_Doc(x) +ktraTC_CheoTrai(x, y)+ ktraTC_CheoPhai(x, y);
	  int dtc =  ktraTC_Doc(x, y) + ktraTC_Hang(x, y) + ktraTC_CheoTrai(x, y)+ktraTC_CheoPhai(x, y);
	  return dtc;
  }
  private int ktraTC_Hang(int x, int y){
	  int diemTC = 0;
	  int soQTa = 0;
	  int soQDich = 0; 
	  
	  for(int i = 1; (i < 5) && (i + x) < BOARD_WIDTH; i++){
		  
		  if(board[y][x+i] == State.O) {
//			  System.out.println("Day roi -----------------: "+board[y][x+i]);
			  soQTa++;
		  }
		  else if(board[y][x+i] == State.X) {
			  soQDich++;
			  break;
		  }else {
			  //Tai day nen xet cac truong hop nuoc doi nuoc ba cach mot O
			  break;
		  }
	  }
	  for(int i = 1; (i < 5) && (x-i >= 0); i++){
		  if(board[y][x-i] == State.O) {
//			  System.out.println("Day roi -----------------: "+board[y][x+i]);
			  soQTa++;
		  }
		  else if(board[y][x-i] == State.X) {
			  soQDich ++;
			  break;
		  }else break;
	  }
	  if(soQDich == 2) return 0;
	  diemTC -= MangPT[soQDich];
	  diemTC += MangTC[soQTa];
//	  System.out.println("So quan            ta:----- "+soQTa);
	  return diemTC;
  }
  private int ktraTC_Doc(int x, int y){
	  
	  int diemTC = 0;
	  int soQTa = 0;
	  int soQDich = 0;
	  for(int i = 1; (i < 5) && (y - i) > 0; i++){
		  if(board[y-i][x] == State.O) soQTa++;
		  else if(board[y-i][x] == State.X) {
			  soQDich++;
			  break;
		  }else {
			  //Tai day nen xet cac truong hop nuoc doi nuoc ba cach mot O
			  break;
		  }
	  }
	  for(int i = 1; (i < 5) && (y+i < BOARD_WIDTH); i++){
		  if(board[y+i][x] == State.O) soQTa++;
		  else if(board[y+i][x] == State.X) {
			  soQDich ++;
			  break;
		  }else break;
	  }
	  if(soQDich == 2) return 0;
	  diemTC -= MangPT[soQDich];
	  diemTC += MangTC[soQTa];
	  return diemTC;
  }
  private int ktraTC_CheoTrai(int x, int y){
	  int diemTC = 0;
	  int soQTa = 0;
	  int soQDich = 0;
	  for(int i = 1; (i < 5) && (y + i) < BOARD_WIDTH && (x+i) < BOARD_WIDTH; i++){
		  if(board[y+i][x+i] == State.O) soQTa++;
		  else if(board[y+i][x+i] == State.X) {
			  soQDich++;
			  break;
		  }else {
			  //Tai day nen xet cac truong hop nuoc doi nuoc ba cach mot O
			  break;
		  }
	  }
	  for(int i = 1; (i < 5) && (y-i > 0) && (x - i > 0); i++){
		  if(board[y-i][x-i] == State.O) soQTa++;
		  else if(board[y-i][x-i] == State.X) {
			  soQDich ++;
			  break;
		  }else break;
	  }
	  if(soQDich == 2) return 0;
	  diemTC -= MangPT[soQDich];
	  diemTC += MangTC[soQTa];
	  return diemTC;
  }
  private int ktraTC_CheoPhai(int x, int y){
	  
	  int diemTC = 0;
	  int soQTa = 0;
	  int soQDich = 0;
	  for(int i = 1; (i < 5) && (y + i) < BOARD_WIDTH && (x-i) > 0; i++){
		  if(board[y+i][x-i] == State.O) soQTa++;
		  else if(board[y+i][x-i] == State.X) {
			  soQDich++;
			  break;
		  }else {
			  //Tai day nen xet cac truong hop nuoc doi nuoc ba cach mot O
			  break;
		  }
	  }
	  for(int i = 1; (i < 5) && (y-i > 0) && (x + i <  BOARD_WIDTH); i++){
		  if(board[y-i][x+i] == State.O) soQTa++;
		  else if(board[y-i][x+i] == State.X) {
			  soQDich ++;
			  break;
		  }else break;
	  }
	  if(soQDich == 2) return 0;
	  diemTC -= MangPT[soQDich];
	  diemTC += MangTC[soQTa];
	  return diemTC;
  }
   
  
  private int ktraPT(int x, int y){
	  int pt = ktraPT_Hang(x, y) + //
	  ktraPT_Doc(x, y) +
	  ktraPT_CheoTrai(x, y) +
	  ktraPT_CheoPhai(x, y);
	   
	  return pt;
  }
  private int ktraPT_Hang(int x, int y){
	  
	  int diemPT = 0;
	  int soQTa = 0;
	  int soQDich = 0; 
	  
	  for(int i = 1; (i < 5) && (i + x) < BOARD_WIDTH; i++){
		  
		  if(board[y][x+i] == State.O) {
//			  System.out.println("Day roi -----------------: "+board[y][x+i]);
			  soQTa++;
			  break;
		  }
		  else if(board[y][x+i] == State.X) {
			  soQDich++; 
		  }else {
			  //Tai day nen xet cac truong hop nuoc doi nuoc ba cach mot O
			  break;
		  }
	  }
	  for(int i = 1; (i < 5) && (x-i > 0); i++){
		  if(board[y][x-i] == State.O) {
//			  System.out.println("Day roi -----------------: "+board[y][x+i]);
			  soQTa++;
			  break;
		  }
		  else if(board[y][x-i] == State.X) {
			  soQDich ++; 
		  }else break;
	  }
	  if(soQTa == 2) return 0;
	  diemPT += MangPT[soQDich]; 
	  
//	  System.out.println("So quan            ta:----- "+soQDich);
	  return diemPT;
  }
  private int ktraPT_Doc(int x, int y){
	  int diemPT = 0;
	  int soQTa = 0;
	  int soQDich = 0;
	  for(int i = 1; (i < 5) && (y - i) > 0; i++){
		  if(board[y-i][x] == State.O) {
			  soQTa++;
			  break;
		  }
		  else if(board[y-i][x] == State.X) {
			  soQDich++; 
		  }else {
			  //Tai day nen xet cac truong hop nuoc doi nuoc ba cach mot O
			  break;
		  }
	  }
	  for(int i = 1; (i < 5) && (y+i < BOARD_WIDTH); i++){
		  if(board[y+i][x] == State.O){
			  soQTa++;
			  break;
		  }
		  else if(board[y+i][x] == State.X) {
			  soQDich ++; 
		  }else break;
	  }
	  if(soQTa == 2) return 0;
	  diemPT += MangPT[soQDich]; 
	  return diemPT;
  }
  private int ktraPT_CheoTrai(int x, int y){
	  
	  int diemPT = 0;
	  int soQTa = 0;
	  int soQDich = 0;
	  for(int i = 1; (i < 5) && (y + i) < BOARD_WIDTH && (x+i) < BOARD_WIDTH; i++){
		  if(board[y+i][x+i] == State.O) {
			  soQTa++;
			  break;
		  }
		  else if(board[y+i][x+i] == State.X) {
			  soQDich++; 
		  }else {
			  //Tai day nen xet cac truong hop nuoc doi nuoc ba cach mot O
			  break;
		  }
	  }
	  for(int i = 1; (i < 5) && (y-i > 0) && (x - i > 0); i++){
		  if(board[y-i][x-i] == State.O) {
			  soQTa++;
			  break;
		  }
		  else if(board[y-i][x-i] == State.X) {
			  soQDich ++; 
		  }else break;
	  }
	  if(soQTa == 2) return 0;
	  diemPT += MangPT[soQDich]; 
	  return diemPT;
  }
  private int ktraPT_CheoPhai(int x, int y){
	  
	  int diemPT = 0;
	  int soQTa = 0;
	  int soQDich = 0;
	  for(int i = 1; (i < 5) && (y + i) < BOARD_WIDTH && (x-i) > 0; i++){
		  if(board[y+i][x-i] == State.O) {
			  soQTa++;
			  break;
		  }
		  else if(board[y+i][x-i] == State.X) {
			  soQDich++; 
		  }else {
			  //Tai day nen xet cac truong hop nuoc doi nuoc ba cach mot O
			  break;
		  }
	  }
	  for(int i = 1; (i < 5) && (y-i > 0) && (x + i <  BOARD_WIDTH); i++){
		  if(board[y-i][x+i] == State.O) {
			  soQTa++;
			  break;
		  }
		  else if(board[y-i][x+i] == State.X) {
			  soQDich ++; 
		  }else break;
	  }
	  if(soQTa == 2) return 0;
	  diemPT += MangPT[soQDich]; 
	  return diemPT;
  }
 
  /**
   * Get a copy of the array that represents the board.
   * @return          the board array
   */
  State[][] toArray () {
      return board.clone();
  }

  /**
   * Check to see who's turn it is.
   * @return          the player who's turn it is
   */
  public State getTurn () {
      return playersTurn;
  }

  /**
   * Check to see who won.
   * @return          the player who won (or Blank if the game is a draw)
   */
  public State getWinner () {
      if (!gameOver) {
          throw new IllegalStateException("TicTacToe is not over yet.");
      }
      return winner;
  }
  public TanCongPhongThu[] getTCPT(){
	  return tcpt;
  }
  /**
   * Get the indexes of all the positions on the board that are empty.
   * @return          the empty cells
   */
  public HashSet<Integer> getAvailableMoves () {
      return movesAvailable;
  }

  /**
   * Checks the specified row to see if there is a winner.
   * @param row       the row to check
   */
  private void checkRow (int row) {
	  int count = 0;
      for (int i = 1; i < BOARD_WIDTH; i++) {
 
    	  if(board[row][i] == State.Blank){
    		  count = 0;
    	  }
    	 if(board[row][i] == board[row][i-1]){
    		 count++;
    		 
    	 }else count = 0;
    	 if(count == 4){
			 winner = playersTurn;
    		 gameOver = true;
//    		 System.out.println("win in row: "+row);
		 }
    	 
      }
  }

  /**
   * Checks the specified column to see if there is a winner.
   * @param column    the column to check
   */
  private void checkColumn (int column) {
	  int count = 0;
      for (int i = 1; i < BOARD_WIDTH; i++) {
 
    	  if(board[i][column] == State.Blank) count = 0;
    	  if(board[i][column] == board[i-1][column]){
    		  count++;
    	  }else count = 0;
    	  if(count == 4){
    		  winner = playersTurn;
    		  gameOver = true;
//    		  System.out.println("win on col: "+column);
    	  }
      }
  }

  /**
   * Check the left diagonal to see if there is a winner.
   * @param x         the x coordinate of the most recently played move
   * @param y         the y coordinate of the most recently played move
   */
  private void checkDiagonalFromTopLeft (int x, int y) {
 
	  
	  if(x == y){
		  int count = 0;
		  for(int i = 1; i < BOARD_WIDTH; i++){
//			  System.out.println("board["+i+"]"+"["+(i)+"] = "+board[i][i]);
			  if(board[i][i] == State.Blank) count = 0;
			  if(board[i][i] == board[i-1][i-1]) {
				  count++;
				 
			  }
			  else count = 0;
			  if(count == 4){
				  winner = playersTurn;
				  gameOver = true;
			  }
		  }
	  } else
	  if(x < y){
		  int count = 0, sep = x - y;
		  for(int i = 1; i < BOARD_WIDTH+sep; i++){
//			  System.out.println("board["+(i-sep)+"]"+"["+(i)+"] = "+board[i-sep][i]);
			  if(board[i-sep][i] == State.Blank) count = 0;
			  if(board[i-sep][i] == board[i-sep-1][i-1]){
				  count++;
//				  System.out.println("Count top leftB: "+count);
			  }else count = 0;
			  if(count == 4){
				  winner = playersTurn;
				  gameOver = true;
//				  System.out.println("win on x, y"+x+", "+y);
			  }
		  }  
	  }
	  if(x > y){
		  int count = 0;
		  int sep = x - y;
		  for(int i = 1; i < BOARD_WIDTH - sep; i++){
//			  System.out.println("board["+i+"]"+"["+(i+sep)+"] = "+board[i][i+sep]);
			  if(board[i][i+sep] == State.Blank) count = 0;
			  if(board[i][i+sep] == board[i-1][i+sep-1]) {
				  count++;
//				  System.out.println("Count top leftA: "+count);
			  }
			  else count = 0;
			  if(count == 4){
				  winner = playersTurn;
				  gameOver = true;
//				  System.out.println("win on x, y"+x+", "+y);
			  }
		  } 
	  }
  } 
  /**
   * Check the right diagonal to see if there is a winner.
   * @param x     the x coordinate of the most recently played move
   * @param y     the y coordinate of the most recently played move
   */
  private void checkDiagonalFromTopRight (int x, int y) {
 
	  
      if (BOARD_WIDTH -1-x == y) {
    	  int count = 0;
    	  for (int i = 1; i < BOARD_WIDTH; i++) {
//        	  System.out.println("board["+(BOARD_WIDTH-1-i)+"]["+i+"] = "+board[BOARD_WIDTH-1-i][i]);
        	  if(board[BOARD_WIDTH-1-i][i] == State.Blank) count = 0;
              if (board[BOARD_WIDTH -1-i][i] == board[BOARD_WIDTH -i][i-1]) {
                  count++;
              }else count = 0;
              if (count == 4) {
                  winner = playersTurn;
                  gameOver = true;
              }
          }
      }else 
      if(BOARD_WIDTH-1-x < y){
    	  int sep = Math.abs(BOARD_WIDTH-1-x-y);
    	  int count = 0;
    	  for(int i = 1; i < BOARD_WIDTH-sep; i++){ 
//    		  System.out.println("board["+(sep+i)+"]["+(BOARD_WIDTH-1-i)+"] = "+board[i+sep][BOARD_WIDTH-1-i]);
    		  if(board[sep+i][BOARD_WIDTH-1-i]== State.Blank) count = 0;
    		  if(board[i+sep][BOARD_WIDTH-1-i] == board[i+sep-1][BOARD_WIDTH-i]){
    			  count ++; 
    		  }else count = 0;
    		  if(count == 4){
    			  winner = playersTurn;
    			  gameOver = true;
    		  }
    	  }
//    	  System.out.println("Phai "+sep);
      }
      else{
    	  int sep = x+y;
    	  int count = 0;
    	  for(int i = 1; i < sep; i++){
//    		  System.out.println("board["+i+"]["+(sep-i)+"] = "+board[i][sep-i]);
    		  if(board[i][sep-i] == State.Blank) count = 0;
    		  if(board[i][sep-i] == board[i-1][sep-i+1]) count ++;
    		  else count = 0;
    		  if(count == 4){
    			  winner = playersTurn;
    			  gameOver = true;
    		  }
    	  }
//    	  System.out.println("Trai "+sep);
      }
  }
  
  public Board getDeepCopy () {
      Board board = new Board();
      
      for (int i = 0; i < board.board.length; i++) {
          board.board[i] = this.board[i].clone();
      }
      
      board.playersTurn       = this.playersTurn;
      board.winner            = this.winner;
      board.movesAvailable    = new HashSet<>();
      board.movesAvailable.addAll(this.movesAvailable);
      board.moveCount         = this.moveCount;
      board.gameOver          = this.gameOver;
      return board;
  }
}
