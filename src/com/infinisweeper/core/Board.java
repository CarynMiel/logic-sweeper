package com.infinisweeper.core;

public class Board 
{
	// randomizer
	final private static long primeX = 80837813181374309L;
	final private static long primeY = 4879324427L;
	final private static long start = 37341137515395871L;
	
	// attributes
	final private long seed;
	private double density;
	
	// constructor
	public Board(double density, long seed) {
		this.density = density;
		this.seed = seed;
	}
	
	// methods
	
	// getter methods
	public long getSeed() {return seed;}
	public double getDensity() {return density;}
	
	// determines if there is a bomb at position xGlobal yGlobal
	public boolean isBomb(int globalX, int globalY) {
		long hash = start;
		hash ^= seed;
		hash ^= primeX * globalX;
		hash ^= primeY * globalY;
		hash *= 0x9E3779B97F4A7C15L;
		//System.out.println("(" + globalX + ", " + globalY + ")");
		//System.out.println(Math.abs(hash) / (double) Long.MAX_VALUE);
		
		// making the hash a positive double in the range [0, 1)
	    double value = (hash & Long.MAX_VALUE) / (double) Long.MAX_VALUE;

		
		return value < density;
	}
	
	// values the tile by number
	public char valueAt(int globalX, int globalY) {
		if(isBomb(globalX, globalY)) {return 'X';}
		int count = 0;
			
		for(int[] pos : surroundingOf(globalX, globalY)) {
			if(isBomb(pos[0], pos[1])) {count++;}
		}
			
		if(count == 0) {return ' ';}
		
		// converting the count to a char
		return (char) (count + '0');
	}
	
	// returns a list of the surrounding coordinates
	public int[][] surroundingOf(int globalX, int globalY) {
		int[][] around = new int[8][2];
		
		around[0] = new int[] {globalX-1, globalY-1};
		around[1] = new int[] {globalX-1, globalY};
		around[2] = new int[] {globalX-1, globalY+1};
		around[3] = new int[] {globalX, globalY-1};
		around[4] = new int[] {globalX, globalY+1};
		around[5] = new int[] {globalX+1, globalY-1};
		around[6] = new int[] {globalX+1, globalY};
		around[7] = new int[] {globalX+1, globalY+1};

		return around;
	}
}