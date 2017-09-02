package edu.cpp.cs420.p3;

import java.util.List;

public class AI {

	/**
	 * This method takes a state, find the best move within the given amount
	 * of time and returns the state resulting from that move. The algorithm
	 * uses a iterative deepening depth-limited minimax search, with alpha-
	 * beta pruning.
	 * 
	 * @param intialState The initial state
	 * @param timeOut 	the amount of time to decide the best move
	 * @return			the state resulting from the best move.
	 */
	public State makeMove( State intialState, int timeOut){
		long startTime = System.currentTimeMillis();
		long timeElapsed = 0;
		int maxDepth = 1;
		State bestMove = null;
		while ( timeElapsed < timeOut*1000 ){
			bestMove = minimax( intialState, maxDepth, startTime, timeOut );
			timeElapsed = System.currentTimeMillis() - startTime;
			maxDepth++;
		}
		System.out.println("Look Ahead Depth: " + (maxDepth-1));
		return bestMove;
	}
	
	/**
	 * Minimax algorithm, with alpha-beta pruning.
	 * @param initialState		The initial state to search from.
	 * @param maxDepth			The maximum depth to search.
	 * @param startTime			The time at which the search began.
	 * @param timeOut			The time in milliseconds to search before timing out.
	 * @return					The best state found.
	 */
	private State minimax(State initialState, int maxDepth, long startTime, int timeOut) {
		System.out.println("Searching depth of " + maxDepth);
		int maxValue = Integer.MIN_VALUE;
		State mostestBestestState = null;
		List<State> successors = initialState.getSuccessors();
		for ( int i = 0; i < successors.size(); i++){
			State s = successors.get(i);
			int value = max( s, Integer.MIN_VALUE, Integer.MAX_VALUE, maxDepth-1, startTime, timeOut);
			if ( value > maxValue ){
				maxValue = value;
				mostestBestestState = s;
			}
		}
		return mostestBestestState;
	}
	
	/**
	 * Max function for minimax algorithm. Uses alpha-beta pruning.
	 */
	private int max(State state, int alpha, int beta, int depthRemaining, long startTime, int timeOut){
		boolean cutOff = System.currentTimeMillis()-startTime > timeOut; 
		if ( depthRemaining == 0 || cutOff || state.terminalTest() ){
			return evaluateState(state);
		} else {
			int maxValue = Integer.MIN_VALUE;
			List<State> successors = state.getSuccessors();
			for ( int i = 0; i < successors.size(); i++){
				int value = min(state, alpha, beta, depthRemaining-1, startTime, timeOut);
				maxValue = maxValue >= value ? maxValue : value;
				if ( maxValue >= beta ){
					return maxValue;
				}
				alpha = alpha >= maxValue ? alpha : maxValue;
			}
			return maxValue;
		}
	}

	/**
	 * Min function for minimax algorithm. Uses alpha-beta pruning.
	 */
	private int min(State state, int alpha, int beta, int depthRemaining, long startTime, int timeOut ){
		boolean cutOff = System.currentTimeMillis()-startTime > timeOut; 
		if ( depthRemaining == 0 || cutOff || state.terminalTest() ){
			return evaluateState(state);
		} else {
			int minValue = Integer.MAX_VALUE;
			List<State> successors = state.getSuccessors();
			for ( int i = 0; i < successors.size(); i++){
				int value = max(state, alpha, beta, depthRemaining-1, startTime, timeOut);
				minValue = minValue <= value ? minValue : value;
				if ( minValue <= alpha ){
					return minValue;
				}
				beta = beta <= minValue ? beta : minValue;
			}
			return minValue;
		}
	}

	/**
	 * The evaluation function for the minimax algorithm.
	 * @param state		The state to evaluate
	 * @return			Integer value of the state's evaluation value
	 */
	public int evaluateState(State state) {
		int SIZE = State.SIZE;
		int[][] grid = state.getState();
		
		// Used for storing the scores for evaluation.
		// It is a 2D array, where the first row stores information for X player, 2nd row for O
		// The columns are for number of one-in-a-rows, two-in-a-rows, three-in-a-row, 
		// and four-in-a-rows, respectively.
		int[][] scores = new int[2][4]; 

		// This part of the algorithm counts the number one-in-a-rows,
		// two-in-a-rows, three-in-a-row, and four-in-a-rows.
		// They are only counted if there are enough empty spaces around it 
		// to complete a four-in-a-row.
		
		// This loop check horizontals, iterates row by row.
		for ( int row = 0; row < SIZE; row++){
			int counter = 1;
			int mark = grid[row][0]; // Gets mark of first space.
			int backIndex = -1;
			
			for ( int col = 1; col < SIZE; col++ ){
				if ( grid[row][col] == mark ){
					counter++; // Increments counter if mark is same as previous space.
				} else {
					if ( mark != 0 ){ // We don't count it if it is an empty space.
						int forwardIndex = col;
						int origCounter = counter;
						int total = counter;
						int score = 0;
						
						// Counts number of empty spaces behind, so we can know if 
						// it is possible to complete the 4-in-a-row.
						while ( counter < 4 && backIndex >= 0 && grid[row][backIndex] == 0 ){
							counter++;
							total++;
							backIndex--;
						}
						score += counter/4; // Increments score if counter is atleast 4;
						counter = origCounter;
						
						// COunts number of empty spaces ahead.
						while ( counter < 4 && forwardIndex < SIZE && grid[row][forwardIndex] == 0){
							counter++;
							total++;
							forwardIndex++;
						}
						score += counter/4;
						
						// If there are not enough spaces behind or ahead to complete a four
						// in a row, we check if we can combine the spaces behind and ahead to
						// for a four-in-a-row.
						if ( score == 0 ){
							score += total/4;
						}
						
						// Adds value to scores array
						scores[mark-1][origCounter-1] += score;
					}
					mark = grid[row][col];
					counter = 1;
					backIndex = col-1;
				}
			}
		}
		
		// Check verticals the same way as previously described.
		for ( int col = 0; col < SIZE; col++ ){
			int counter = 1;
			int mark = grid[0][col];
			int backIndex = -1;
			for ( int row = 1; row < SIZE; row++ ){
				if ( grid[row][col] == mark ){
					counter++;
				} else  {
					if ( mark != 0 ){
						int origCounter = counter;
						int forwardIndex = row;
						int total = counter;
						int score = 0;
						while ( counter < 4 && backIndex >= 0 && grid[backIndex][col] == 0 ){
							counter++;
							total++;
							backIndex--;
						}
						score += counter/4;
						counter = origCounter;
						while ( counter < 4 && forwardIndex < SIZE && grid[forwardIndex][col] == 0){
							counter++;
							total++;
							forwardIndex++;
						}
						score += counter/4;
						if ( score == 0 ){
							score += total/4;
						}
						scores[mark-1][origCounter-1] += score;
					}
					mark = grid[row][col];
					counter = 1;
					backIndex = row-1;
				}
			}
		}
		
		
		//The next part checks for patterns.
		int patternValue = 0;
		for(int row = 0; row < SIZE; row++) {
			for(int col = 0; col < SIZE - 4; col ++) {
				int pos1 = grid[row][col];
				int pos2 = grid[row][col+1];
				int pos3 = grid[row][col+2];
				int pos4 = grid[row][col+3];
				patternValue += checkForPatterns( pos1, pos2, pos3, pos4);
			}
		}
		for(int col = 0; col < SIZE; col ++) {
			for(int row = 0; row < SIZE - 4; row++) {

				int pos1 = grid[row][col];
				int pos2 = grid[row+1][col];
				int pos3 = grid[row+2][col];
				int pos4 = grid[row+3][col];
				patternValue += checkForPatterns( pos1, pos2, pos3, pos4);
			}
		}
		
		// Adds up the scores from the pattern detection with the scores found previously.
		// Each one-in-a-row, two-in-a-row, etc, are weighted accordingly.
		int evaluationScore = patternValue
				+ 10*scores[0][0] + 20*scores[0][1] + 50*scores[0][2] + 150*scores[0][3]
				- 10*scores[1][0] - 20*scores[1][1] - 50*scores[1][2] - 150*scores[1][3];
		return evaluationScore;
	}
	
	/**
	 * Checks for patterns within the given positions.
	 */
	private int checkForPatterns( int pos1, int pos2, int pos3, int pos4) {
		int eval = 0;
		if(pos1 == 0 && pos4 == 1 && pos1 == 1 && pos4 == 0) // -XX-
            eval += 60;
        else if(pos1 == 0 && pos4 == 2 && pos1 == 2 && pos4 == 0) // -00-
            eval -= 60;
        else if((pos1 == 1 && pos2 == 2 && pos3 == 2 && pos4 == 0) || // XOO-
                (pos1 == 0 && pos2 == 2 && pos3 == 2 && pos4 == 1))   // -OOX
            eval += 60;
        else if((pos1 == 2 && pos2 == 1 && pos3 == 1 && pos4 == 0) || // OXX-
                (pos1 == 0 && pos2 == 1 && pos3 == 1 && pos4 == 2))   // -XXO
            eval -= 60;
		return eval;
	}

}
