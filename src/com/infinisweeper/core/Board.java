package com.infinisweeper.core;

import java.util.*;

public class Board 
{
	// randomizer
	private final Random rand = new Random();
	
	// attributes
	final private long seed;
	public Map<Long, Chunk> board = new HashMap<>();
	private double density;
	private int xChunkMin = 0, xChunkMax = 0;
	private int yChunkMin = 0, yChunkMax = 0;
	private int xMin = 0, xMax = 0;
	private int yMin = 0, yMax = 0;
	
	// constructor
	public Board(double density) {
		this.density = density;
		seed = rand.nextLong();
	}
	
	// methods
	
	// getter methods
	public long getSeed() {return seed;}
	public Map<Long, Chunk> getBoard() {return board;}
	public double getDensity() {return density;}
	public int getXChunkMin() {return xChunkMin;}
	public int getXChunkMax() {return xChunkMax;}
	public int getYChunkMin() {return yChunkMin;}
	public int getYChunkMax() {return yChunkMax;}
   	public int getXMin() {return xMin;}
	public int getXMax() {return xMax;}
	public int getYMin() {return yMin;}
	public int getYMax() {return yMax;}
}