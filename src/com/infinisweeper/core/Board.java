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
}