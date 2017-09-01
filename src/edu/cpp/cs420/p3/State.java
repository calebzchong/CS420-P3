package edu.cpp.cs420.p3;

import java.util.ArrayList;
import java.util.List;

public class State {
	
	public static final int SIZE = 8;
	private int[][] grid;
	private int currentMark;
	private String stringForm = null;
	
	public State( boolean playerTurn ){
		grid = new int[SIZE][SIZE];
		this.currentMark = playerTurn ? 2 : 1;
	}
	
	private State(int[][] newGrid, int mark) {
		if ( newGrid.length != SIZE || newGrid[0].length != SIZE ){
			throw new RuntimeException("Invalid grid size.");
		}
		currentMark = mark;
		grid = newGrid;
	}
	
	public State mark( String coordinate ){
		if ( coordinate.length() != 2 || !Character.isAlphabetic(coordinate.charAt(0)) 
				|| !Character.isDigit(coordinate.charAt(1)) ){
			return null;
		} else {
			return mark( coordinate.charAt(0)-65, Integer.parseInt("" + coordinate.charAt(1))-1);
		}
	}
	
	private State mark( int row, int col ){
		if ( grid[row][col] != 0 ){
			return null;
		} else {
			int[][] newGrid = grid.clone();
			for ( int i = 0; i < SIZE; i++){
				newGrid[i] = grid[i].clone();
			}
			newGrid[row][col] = currentMark;
			int newMark = currentMark%2+1;
			return new State(newGrid, newMark);
		}
	}
	
	public List<State> getSuccessors(){
		List<State> successors = new ArrayList<State>();
		for ( int row = 0; row < SIZE; row++ ){
			for ( int col = 0; col < SIZE; col++ ){
				if ( grid[row][col] == 0 ){
					successors.add(this.mark(row, col));
				}
			}
		}
		return successors;
	}
	
	public boolean terminalTest(){
		// Check horizontals
		for ( int row = 0; row < SIZE; row++){
			int counter = 1;
			int mark = grid[row][0];
			for ( int col = 1; col < SIZE; col++ ){
				if ( grid[row][col] == mark ){
					counter++;
				} else {
					mark = grid[row][col];
					counter = 1;
				}
				if ( mark != 0 && counter >= 4 ){
					return true;
				}
			}
		}
		// Check verticals
		for ( int col = 0; col < SIZE; col++ ){
			int counter = 1;
			int mark = grid[0][col];
			for ( int row = 1; row < SIZE; row++ ){
				if ( grid[row][col] == mark ){
					counter++;
				} else {
					mark = grid[row][col];
					counter = 1;
				}
				if ( mark != 0 && counter >= 4 ){
					return true;
				}
			}
		}
		return false;
	}
	
	public String toString(){
		if ( stringForm == null ){
			StringBuilder sb = new StringBuilder();
			sb.append("   ");
			for ( int i = 1; i <= SIZE; i++){
				sb.append(i + "  ");
			}
			sb.append("\n");
			for (int letter = 65, i = 0; i < SIZE; i++,letter++){
				sb.append((char)letter + " ");
				for ( int j = 0; j < SIZE; j++ ){
					if ( grid[i][j] == 0 ){
						sb.append(" - ");
					} else if ( grid[i][j] == 1 ){
						sb.append(" X " );
					} else if ( grid[i][j] == 2 ){
						sb.append(" O " );
					}
				}
				sb.append("\n");
			}
			stringForm = sb.toString();
		}
		return stringForm;
	}
	
        public int[][] getState() {
            return grid;
        }
        
}
