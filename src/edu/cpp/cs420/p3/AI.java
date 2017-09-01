package edu.cpp.cs420.p3;

public class AI {

	public State ids( State intialState, int timeOut){
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
		return max(initialState, 0, maxDepth);
	}
	
	private void max(State state, int currentDepth, int maxDepth ){
		if ( state.terminalTest() ){
			
		}
	}
	
	private void min(State state, int currentDepth, int maxDepth ){
		
	}
        
	// Only works for non-terminal states
        private int evaluateState(State state) {
            int SIZE = state.getSize(), previousMark, counter;
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
