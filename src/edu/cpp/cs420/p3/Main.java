package edu.cpp.cs420.p3;

public class Main {

	public static void main(String[] args) {
		State s = new State();
		s = s.mark("A1", 1);
		s = s.mark("B1", 1);
		s = s.mark("C1", 1);
		s = s.mark("D1", 1);
		System.out.println(s);
		System.out.println(s.terminalTest());
	}

}
