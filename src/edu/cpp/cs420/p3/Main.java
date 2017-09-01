package edu.cpp.cs420.p3;

public class Main {

	public static void main(String[] args) {
		State s = new State();
		s = s.mark("A1");
		s = s.mark("B1");
		s = s.mark("A2");
		s = s.mark("C1");
		s = s.mark("A3");
		s = s.mark("D1");
		s = s.mark("A4");
		System.out.println(s);
		System.out.println(s.terminalTest());
	}

}
