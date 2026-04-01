package com.infinisweeper.core;

public class Board 
{
	// randomizer
	final private static long primeX = 80837813181374309L;
	final private static long primeY = 4879324427L;
	final private static long start = 37341137515395871L;
	final private static long scramble = 0x9E3779B97F4A7C15L;
	
	// attributes
	final public long seed;
	final public double density;
	
	// constructor
	public Board(double density, long seed) {
		this.density = density;
		this.seed = seed;		
	}
	
	// methods
	
	// determines if there is a bomb at position xGlobal yGlobal
	public boolean isBomb(int globalX, int globalY) {
		long hash = start;
		hash ^= seed;
		hash ^= primeX * globalX;
		hash ^= primeY * globalY;
		hash *= scramble;
		//System.out.println("(" + globalX + ", " + globalY + ")");
		//System.out.println(Math.abs(hash) / (double) Long.MAX_VALUE);
		
		// making the hash a positive double in the range [0, 1)
	    double value = (hash & Long.MAX_VALUE) / (double) Long.MAX_VALUE;
		
		return value < density;
	}
	
	// values the tile by number
	public char valueAt(int globalX, int globalY) {
		if(isBomb(globalX, globalY)) {return Tile.Bomb;}
		int count = 0;
			
		for(int[] pos : Tile.surroundingOf(globalX, globalY)) {
			if(isBomb(pos[0], pos[1])) {count++;}
		}
			
		if(count == 0) {return Tile.Blank;}
		
		// converting the count to a char
		return (char) (count + '0');
	}
}