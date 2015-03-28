package main;

public class ProblemNaive2 extends Problem {

	public ProblemNaive2(ProblemData data) {
		super(data);
	}

	public void resolve() {
		System.out.println("Launch problem naive 2");
		
		System.out.println(">"+hasCycle(0, 0, 0));
		
		for(int k=0; k<data.getnZ(); k++) {
		
			int n = 0;
			
			for(int i=0; i<data.getnX(); i++) {
				for(int j=0; j<data.getnY(); j++) {
					if(hasCycle(i, j, k)) {
						if(findPathTo(i, j, k)) {
							n++;
						}
						
					}
				}
			}
			
			System.out.println(k+" => "+n);
		}
	}

	public boolean hasCycle(int x, int y, int z) {
		int currentX = x, currentY = y, currentZ = z;
		
		int nTurn = 0;
		
		while(true) {
			
			if(currentY < 0 || currentY >= data.getnY()) {
				return false;
			}
			
			//System.out.println(currentX+" "+currentY+" "+currentZ);
			
			currentX += data.getWindVector(currentX, currentY, currentZ).x;
			
			if(currentX < 0) {
				currentX = data.getnX()+currentX;
			}
			else if(currentX >= data.getnX()) {
				currentX = currentX-data.getnX();
			}
			
			//System.out.println(currentX+" "+currentY+" "+currentZ);
			
			currentY += data.getWindVector(currentX, currentY, currentZ).y;
			
			if(currentX == x && currentY == y && currentZ == z) {
				return true;
			}
			
			nTurn++;
			
			if(nTurn >= this.data.getNbTurn())
				return true;
		}
	}
	
	
	public boolean findPathTo(int x, int y, int z) {
		
		
		int currentX = this.data.getStartBalloon().x, currentY = this.data.getStartBalloon().y, currentZ = 0;
		
		int nTurn = 0;
		
		Coord2 target = new Coord2(x, y);
		
		while(true) {
			
			if(currentY < 0 || currentY >= data.getnY()) {
				return false;
			}
			
			//Compute with current 
			int dCurr = 0;
			Coord2 cZcurr = computeCoord(currentX, currentY, currentZ);
			if(cZcurr != null)
				dCurr = getDistance(cZcurr, target);
			else
				dCurr = Integer.MAX_VALUE;
			
			int dUp = 0;
			Coord2 cZup = null;
			if(currentZ+1 >= data.getnZ())
				dUp = Integer.MAX_VALUE;
			else {
				cZup = computeCoord(currentX, currentY, currentZ+1);
				if(cZup != null)
					dUp = getDistance(cZup, target);
				else
					dUp = Integer.MAX_VALUE;
			}
			
			int dDown = 0;
			Coord2 cZdown = null;
			
			if(currentZ-1 < 0)
				dDown = Integer.MAX_VALUE;
			else {
				cZdown = computeCoord(currentX, currentY, currentZ-1);
				if(cZdown != null)
					dDown = getDistance(cZdown, target);
				else
					dDown = Integer.MAX_VALUE;
			}
			
			//best z
			if(dCurr <= dUp && dCurr <= dDown) {
				currentX = 	cZcurr.x;
				currentY = cZcurr.y;
			}
			else if(dUp < dDown) {
				currentX = 	cZup.x;
				currentY = cZup.y;
			}
			else {
				currentX = 	cZdown.x;
				currentY = cZdown.y;
			}
			
			nTurn++;
			
			if(nTurn >= this.data.getNbTurn())
				return true;
		}
	}

	public Coord2 computeCoord(int currentX, int currentY, int currentZ) {
		
		currentX += data.getWindVector(currentX, currentY, currentZ).x;
		
		if(currentX < 0) {
			currentX = data.getnX()+currentX;
		}
		else if(currentX >= data.getnX()) {
			currentX = currentX-data.getnX();
		}
		
		currentY += data.getWindVector(currentX, currentY, currentZ).y;
		
		if(currentY < 0 || currentY >= data.getnY()) {
			return null;
		}
		else {
			return new Coord2(currentX, currentY);
		}
	}
	
	public int getDistance(Coord2 c1, Coord2 c2) {
		return (c1.x-c2.x)*(c1.x-c2.x)+(c1.y-c2.y)*(c1.y-c2.y);
	}
	

}
