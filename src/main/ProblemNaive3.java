package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class ProblemNaive3 extends Problem {
	
	public ProblemNaive3(ProblemData data) {
		super(data);
	}
	
	public int getBalloonDistance(Coord3 balloon1, Coord3 balloon2) {
		int distanceX = Math.min(Math.abs(balloon1.x - balloon2.x), Math.abs(balloon1.x + this.data.getnX() - balloon2.x));
		int distanceY = Math.abs(balloon1.y - balloon2.y);
		return distanceX + distanceY;
	}
	
	public float commonPercentage(List<Integer> path1, List<Integer> path2) {
		float percentage = 0;
		Coord2 windVector;
		Coord3 balloon1 = new Coord3(this.data.getStartBalloon().x, this.data.getStartBalloon().y, -1);
		Coord3 balloon2 = new Coord3(this.data.getStartBalloon().x, this.data.getStartBalloon().y, -1);
		
		for(int i = 0; i < Math.min(path1.size(), path2.size()); i++) {
			if(balloon1.y >= 0 && balloon1.y < this.data.getnY()) {
				windVector = this.data.getWindVector(balloon1.x, balloon1.y, balloon1.z + path1.get(i));
				balloon1 = new Coord3((balloon1.x + windVector.x) % this.data.getnX(), balloon1.y + windVector.y, balloon1.z + path1.get(i));
			}
			if(balloon2.y >= 0 && balloon2.y < this.data.getnY()) {
				windVector = this.data.getWindVector(balloon2.x, balloon2.y, balloon2.z + path2.get(i));
				balloon2 = new Coord3((balloon2.x + windVector.x) % this.data.getnX(), balloon2.y + windVector.y, balloon2.z + path2.get(i));
			}
			if(getBalloonDistance(balloon1, balloon2) < this.data.getCoverageRadius()) {
				percentage++;
			}
		}
		
		return (percentage/Math.min(path1.size(), path2.size())) * 100;
	}
	
	public void resolve() {
		
		//load file
		BufferedReader reader = null;;
		final int scoreList[][][] = new int[this.data.getnX()][this.data.getnY()][this.data.getnZ()];
		try {
			reader = new BufferedReader(new FileReader("data/scoreList"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String line;
		try {
			while((line = reader.readLine()) != null) {
				Scanner sc = new Scanner(line);
				
				int x = sc.nextInt();
				int y = sc.nextInt();
				int z = sc.nextInt();
				int score = sc.nextInt();
				
				scoreList[x][y][z] = score;
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<Coord3> sortedIndex = new ArrayList<Coord3>();
		
		for(int i=0; i<data.getnX(); i++) {
			for(int j=0; j<data.getnY(); j++) {
				for(int k=0; k<data.getnZ(); k++) {
					if(scoreList[i][j][k] != 0)
						sortedIndex.add(new Coord3(i, j, k));
				}
			}	
		}
		
		Collections.sort(sortedIndex, new Comparator<Coord3>() {

			public int compare(Coord3 index1, Coord3 index2) {
				return scoreList[index2.x][index2.y][index2.z]-scoreList[index1.x][index1.y][index1.z];
			}
		});
		
		List<List<Integer>> listPath = new ArrayList<List<Integer>>();
		
		int decalage = 0;
		
		for(int i=0; i<data.getNbBalloon(); i++) {
			
			if(i > 0) 
				while(commonPercentage(findPathTo(sortedIndex.get(i + decalage).x, sortedIndex.get(i + decalage).y, sortedIndex.get(i + decalage).z), 
						listPath.get(listPath.size()-1)) > 72)
					decalage = (decalage + 1) % sortedIndex.size();
					
			Coord3 c = sortedIndex.get(i + decalage);
			listPath.add(findPathTo(c.x, c.y, c.z));
			System.out.println(c.x+" "+c.y+" "+c.z);
		}
		
		int tempo = 1;
		
		for(int t=0; t<data.getNbTurn(); t++) {
			for(int i=0; i<data.getNbBalloon(); i++) {
				if(t >= listPath.get(i).size() || t-tempo*i < 0)
					this.move[t][i] = 0;
				else
					this.move[t][i] = listPath.get(i).get(t-tempo*i);
				
			}
		}

		
		
		
		
		
	}

}
