package edu.cpp.cs420.p3;

import java.util.Scanner;

public class Main {
	private static Scanner kb; 
	private static AI ai; 
	
	public static void main(String[] args) {
		System.out.println("Four-in-a-Line Game\n");
		kb = new Scanner(System.in);
		ai = new AI();
		boolean play = true;
		while ( play ){
			State currentState = new State();
			boolean playerTurn = getYesNo("Would you like to go first (y/n)? ");
			System.out.print("How long should the computer think about its moves (in seconds)? ");
			int timeOut = Integer.parseInt(kb.nextLine());
			System.out.println(currentState);
			while ( !currentState.terminalTest() ){
				if ( playerTurn ){
					currentState = playerMove(currentState);
				} else {
					currentState = ai.makeMove(currentState, timeOut);
				}
				System.out.println("\n" + currentState);
				playerTurn = !playerTurn;
			}
			System.out.print("Game Over. ");
			System.out.println( playerTurn ? "CPU Wins!" : "Player Wins!");
			play = getYesNo("Would you like to play again (y/n)? ");
		}
	}
	
	private static State playerMove(State state) {
		State newState = null;
		while ( newState == null ){
			System.out.print("Choose your next move: ");
			String input = kb.nextLine();
			newState = state.mark(input);
		}
		return newState;
	}

	private static boolean getYesNo( String prompt ){
		String input = "";
		do {
			System.out.print(prompt);
			input = kb.nextLine();
		} while ( !input.equalsIgnoreCase("y") && !input.equalsIgnoreCase("n") );
		return input.equalsIgnoreCase("y");
	}

}
