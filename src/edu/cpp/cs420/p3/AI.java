package edu.cpp.cs420.p3;

public class AI {
	public static final int MARK = 2;
	
	public State ids( State currentState, int timeOut){
		long startTime = System.currentTimeMillis();
		long timeElapsed = 0;
		int maxDepth = 1;
		State bestMove = null;
		while ( timeElapsed < timeOut*1000 ){
			minimax( currentState, maxDepth );
			timeElapsed = System.currentTimeMillis() - startTime;
		}
		return bestMove;
	}

	private void minimax(State currentState, int maxDepth) {
		
	}
	
}
