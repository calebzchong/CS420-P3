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
        
	// Only works for non-terminal states
        private int evaluateState(State state) {
            int SIZE = State.SIZE, previousMark, counter;
            int[][] grid = state.getState();
            int[] x = {0,0,0};
            int[] o = {0,0,0};
            
            // check horizontals
            for ( int row = 0; row < SIZE; row++){
			counter = 1;
                        previousMark = grid[row][0];
			for ( int col = 1; col < SIZE; col++ ){
                            
                            if(previousMark != 0 && previousMark == grid[row][col]) {
                                counter++;
                                if(col == SIZE - 1 && previousMark == 1) 
                                    x[counter - 1]++;
                                else if(col == SIZE - 1 && previousMark == 2)    
                                    o[counter - 1]++;
                            }
                            else {
                                if(previousMark == 1) 
                                    x[counter - 1]++;
                                else if(previousMark == 2)    
                                    o[counter - 1]++;
                                counter = 1;
                            }
                            previousMark = grid[row][col];
			}
		}
		// Check verticals
		for ( int col = 0; col < SIZE; col++ ){
			counter = 0;
			previousMark = grid[0][col];
			for ( int row = 1; row < SIZE; row++ ){
                                if(previousMark == 0)
                                    break;
				if ( grid[row][col] == previousMark ){
					counter++;
				} else {
					previousMark = grid[row][col];
					counter = 1;
				}
			}
		}
                
                return (3*x[2] + 2*x[1] + x[0] - 3*o[2] + 2*o[1] + o[0]);
        }

}
