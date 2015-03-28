package main;

import java.io.FileNotFoundException;

public class Main {
	
	public static void main(String[] args) throws FileNotFoundException {
		
		ProblemData data = Input.parse("data/in");
		
		Problem problem = new ProblemNaive(data);
		
		problem.resolve();
		
		problem.output("data/out");
	}

}
