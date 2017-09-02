package edu.cpp.cs420.p3;

import java.util.List;

public class AI {

	private static final int[][] evalValues = {
		{ 2, 3,  4,  5,  5, 4, 3, 2}, 
		{ 3, 4,  5,  6,  6, 5, 4, 3},
		{ 4, 5,  6,  7,  7, 6, 5, 4}, 
		{ 5, 6,  7,  8,  8, 7, 6, 5},
		{ 5, 6,  7,  8,  8, 7, 6, 5},
		{ 4, 5,  6,  7,  7, 6, 5, 4},
		{ 3, 4,  5,  6,  6, 5, 4, 3},
		{ 2, 3,  4,  5,  5, 4, 3, 2}};

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
//		System.out.println("X SCORES");
//		System.out.println("Ones: " + scores[0][0]);
//		System.out.println("twos: " + scores[0][1]);
//		System.out.println("Threes: " + scores[0][2]);
//		System.out.println("fours: " + scores[0][3]);
//		System.out.println("O SCORES");
//		System.out.println("Ones: " + scores[1][0]);
//		System.out.println("twos: " + scores[1][1]);
//		System.out.println("Threes: " + scores[1][2]);
//		System.out.println("fours: " + scores[1][3]);
		int evaluationScore = ( 1*scores[0][0] + 2*scores[0][1] + 5*scores[0][2] + 10*scores[0][3]
							  - 1*scores[1][0] - 2*scores[1][1] - 5*scores[1][2] - 10*scores[1][3] );
		return evaluationScore;
	}

}
