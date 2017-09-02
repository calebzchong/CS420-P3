package edu.cpp.cs420.p3;

import java.util.List;

public class AI {

	public State makeMove( State intialState, int timeOut){
		long startTime = System.currentTimeMillis();
		long timeElapsed = 0;
		int maxDepth = 1;
		State bestMove = null;
		while ( timeElapsed < timeOut*1000 ){
			bestMove = minimax( intialState, maxDepth );
			timeElapsed = System.currentTimeMillis() - startTime;
		}
		return bestMove;
	}

	private State minimax(State initialState, int maxDepth) {
		int maxValue = Integer.MIN_VALUE;
		State mostestBestestState = null;
		List<State> successors = initialState.getSuccessors();
		for ( int i = 0; i < successors.size(); i++){
			State s = successors.get(i);
			int value = max( s, Integer.MIN_VALUE, Integer.MAX_VALUE, maxDepth-1);
			if ( value > maxValue ){
				maxValue = value;
				mostestBestestState = s;
			}
		}
		return mostestBestestState;
	}

	private int max(State state, int alpha, int beta, int depthRemaining ){
		if ( depthRemaining == 0 || state.terminalTest() ){
			return evaluateState(state);
		} else {
			int maxValue = Integer.MIN_VALUE;
			List<State> successors = state.getSuccessors();
			for ( int i = 0; i < successors.size(); i++){
				int value = min(state, alpha, beta, depthRemaining-1);
				maxValue = maxValue >= value ? maxValue : value;
				if ( maxValue >= beta ){
					return maxValue;
				}
				alpha = alpha >= maxValue ? alpha : maxValue;
			}
			return maxValue;
		}
	}

	private int min(State state, int alpha, int beta, int depthRemaining  ){
		if ( depthRemaining == 0 || state.terminalTest() ){
			return evaluateState(state);
		} else {
			int minValue = Integer.MAX_VALUE;
			List<State> successors = state.getSuccessors();
			for ( int i = 0; i < successors.size(); i++){
				int value = max(state, alpha, beta, depthRemaining-1);
				minValue = minValue <= value ? minValue : value;
				if ( minValue <= alpha ){
					return minValue;
				}
				beta = beta <= minValue ? beta : minValue;
			}
			return minValue;
		}
	}

	public int evaluateState(State state) {
		int SIZE = State.SIZE;
		int[][] grid = state.getState();
		int[][] scores = new int[2][4];
		int eval = 0;

		// check horizontals
		for ( int row = 0; row < SIZE; row++){
			int counter = 1;
			int mark = grid[row][0];
			int backIndex = -1;
			for ( int col = 1; col < SIZE; col++ ){
				if ( grid[row][col] == mark ){
					counter++;
				} else {
					if ( mark != 0 ){
						int forwardIndex = col;
						int origCounter = counter;
						int total = counter;
						int score = 0;
						while ( counter < 4 && backIndex >= 0 && grid[row][backIndex] == 0 ){
							counter++;
							total++;
							backIndex--;
						}
						score += counter/4;
						counter = origCounter;
						while ( counter < 4 && forwardIndex < SIZE && grid[row][forwardIndex] == 0){
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
					backIndex = col-1;
				}
			}
		}
		// Check verticals
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

		for(int row = 0; row < SIZE; row++) {
			for(int col = 0; col < SIZE - 4; col ++) {
				int pos1 = grid[row][col];
				int pos2 = grid[row][col+1];
				int pos3 = grid[row][col+2];
				int pos4 = grid[row][col+3];
				eval += evaluate( pos1, pos2, pos3, pos4);
			}
		}

		for(int col = 0; col < SIZE; col ++) {
			for(int row = 0; row < SIZE - 4; row++) {

				int pos1 = grid[row][col];
				int pos2 = grid[row+1][col];
				int pos3 = grid[row+2][col];
				int pos4 = grid[row+3][col];
				eval += evaluate( pos1, pos2, pos3, pos4);
			}
		}

		int evaluationScore = eval
				+ 10*scores[0][0] + 20*scores[0][1] + 50*scores[0][2] + 150*scores[0][3]
						- 10*scores[1][0] - 20*scores[1][1] - 50*scores[1][2] - 150*scores[1][3];
		return evaluationScore;
	}
	
	private int evaluate( int pos1, int pos2, int pos3, int pos4) {
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
