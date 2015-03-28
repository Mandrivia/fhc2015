package main;

public class ProblemNaive extends Problem {
	
	public ProblemNaive(ProblemData data) {
		super(data);
	}

	public int columnDist(int x, int u) {
		return Math.min(Math.abs(x - u), Math.abs((x + this.data.getnX()) - u));
	}
	
	/**
	 * Return coverage score for the case (x, y)
	 */
	public int getScoreBalloon(int x, int y) {
		int score = 0;
		
		// If the balloon is not in the map
		if(y >= this.data.getnY() || y < 0)
			return -1;
		
		// (x - u)^2 + (columndist(y, v))^2 < V^2 => score + 1
		for(int i = (x - this.data.getCoverageRadius()) % this.data.getnX(); 
				i <= (x + this.data.getCoverageRadius()) % this.data.getnX(); i++) {
			for(int j = y - (this.data.getCoverageRadius() - columnDist(x, i));
					j <= y + (this.data.getCoverageRadius() - columnDist(x, i)); j++) {
				if(this.data.isTarget(i,j))
					score++;
			}
		}
		return score;
	}
	

	public void updateTarget(Coord3 balloonsCoord, Coord3 oldBalloonsCoord) {
		
		for(int i = oldBalloonsCoord.x - this.data.getCoverageRadius(); i <= oldBalloonsCoord.x + this.data.getCoverageRadius(); i++) {
			for(int j = oldBalloonsCoord.y - (this.data.getCoverageRadius() - columnDist(oldBalloonsCoord.x, i));
					j <= oldBalloonsCoord.y + (this.data.getCoverageRadius() - columnDist(oldBalloonsCoord.x, i)); j++) {
				if(this.data.isTarget(i,j))
					this.data.setTargetCovered(this.data.getTargetIndex(i, j), 
							this.data.isCovered(this.data.getTargetIndex(i, j)) - 1);
			}
		}
		
		// If the balloon is not in the map
		if(balloonsCoord.y >= this.data.getnY() || balloonsCoord.y < 0)
			return;
		
		for(int i = balloonsCoord.x - this.data.getCoverageRadius(); i <= balloonsCoord.x + this.data.getCoverageRadius(); i++) {
			for(int j = balloonsCoord.y - (this.data.getCoverageRadius() - columnDist(balloonsCoord.x, i));
					j <= balloonsCoord.y + (this.data.getCoverageRadius() - columnDist(balloonsCoord.x, i)); j++) {
				if(this.data.isTarget(i,j))
					this.data.setTargetCovered(this.data.getTargetIndex(i, j), 
							this.data.isCovered(this.data.getTargetIndex(i, j)) + 1);
			}
		}
	}
	
	public int nextTurnResult(Coord3 balloonCoord, int altitudeDeviation) {
		
		int score = 0;
		
		if(balloonCoord.z + altitudeDeviation < 0 || 
				balloonCoord.z + altitudeDeviation >= 8)
			return -1;

		/*if(nbTurns > 1) {
			int bestAltitude = 0;
			int bestAltitudeValue = 0;
			for(int i = - 1; i <= 1; i++) {
				if(nextTurnResult(balloonCoord, i, nbTurns-1) >= bestAltitudeValue) {
					bestAltitude = i;
					bestAltitudeValue = nextTurnResult(balloonCoord, i, nbTurns-1);
				}
			}	
			score += bestAltitudeValue;
		}*/
		
		Coord2 windVector = this.data.getWindVector(balloonCoord.x, balloonCoord.y, balloonCoord.z + altitudeDeviation);
		score += getScoreBalloon(balloonCoord.x + windVector.x, 
				balloonCoord.y + windVector.y);
		return score;
	}
	
	public void resolve() {
		for(int t = 0; t < this.data.getNbTurn(); t++) {
			System.out.println("Tour " + t);
			for(int b = 0; b < this.data.getNbBalloon(); b++) {
				if(t == b) {
					this.move[t][b] = 1;
					this.data.setBalloonsCoord(b, 
							this.data.newBalloonCoord(this.data.getBalloonsCoord(b), 1));
				} else if(t > b) {
					int bestAltitude = 0;
					int bestAltitudeValue = 0;
					for(int i = -1; i <= 1; i++) {
						if(nextTurnResult(this.data.getBalloonsCoord(b), i) >= bestAltitudeValue) {
							bestAltitude = i;
							bestAltitudeValue = nextTurnResult(this.data.getBalloonsCoord(b), i);
						}
					}
					this.move[t][b] = bestAltitude;
					this.updateTarget(this.data.newBalloonCoord(this.data.getBalloonsCoord(b), bestAltitude), 
							this.data.getBalloonsCoord(b));
					this.data.setBalloonsCoord(b, 
							this.data.newBalloonCoord(this.data.getBalloonsCoord(b), bestAltitude));
				}
			}
		}
	}

}
