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
}
