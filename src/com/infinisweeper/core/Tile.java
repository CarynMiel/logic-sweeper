package com.infinisweeper.core;

import java.util.*;


public class Tile 
{
	// attributes
	final private static long xPrime = 73856093L;
	final private static long yPrime = 19349663L;
	final private static long start = 12345678;
	
	private Role hidden = Role.Blank;
	private Role shown =  Role.Unknown;
	private final long chunkKey;
	private final int xGlobal;
	private final int yGlobal;
	private final int xLocal;
	private final int yLocal;
	private int number = -1;
	
	// constructor
	public Tile(int xGlobal, int yGlobal) {
		this.xGlobal = xGlobal;
		this.yGlobal = yGlobal;
		
		// initializing the key of the chunk the tile belongs to
		// finding the chunk it belongs to 
		int xChunk = xGlobal / Chunk.ChunkSize;
		int yChunk = yGlobal / Chunk.ChunkSize;
		
		// initializing the local coordinates
		if(xGlobal >= 0) {xLocal = xGlobal % Chunk.ChunkSize;} else {
			xLocal = Chunk.ChunkSize - (Math.abs(xGlobal) % Chunk.ChunkSize);
			xChunk--;}
		
		if(yGlobal >= 0) {yLocal = yGlobal % Chunk.ChunkSize;} else {
			yLocal = Chunk.ChunkSize - (Math.abs(yGlobal) % Chunk.ChunkSize);
			yChunk--;}
		
		// getting the chunkKey
		long xLong = (long) xChunk << 32;
		long yLong = (long) yChunk & 0xffffffffL;
		chunkKey = xLong | yLong;
	}
	
	// methods
	
	// getter methods
	public long getChunkKey() {return chunkKey;}
	public int getXGlobal() {return xGlobal;}
	public int getYGlobal() {return yGlobal;}
	public int getXLocal() {return xLocal;}
	public int getYLocal() {return yLocal;}
	public int getNumber() {return number;}
	public Role getHidden() {return hidden;}
	public Role getShown() {return shown;}
	
	// tile interaction methods
	public void placeBomb() {hidden = Role.Bomb;}
	public void placeFlag()	{shown = Role.Flag;}
	public void removeFlag() {shown = Role.Unknown;}	
	public void showTile() {hidden = shown;}
	
	// update tile number
	public void placeNumber(int num) {
		hidden = Role.Number;
		number = num;
	}
	
	// is a bomb
	public boolean isBomb(long seed, double density) {
		long hash = start;
		hash ^= xGlobal * xPrime;
		hash ^= yGlobal * yPrime;
		hash ^= seed;
		
		hash = hash & Long.MAX_VALUE;
		
		boolean bomb = ((double) (hash / Long.MAX_VALUE)) < density;
		if(bomb) {hidden = Role.Bomb;}
		
		return bomb;
	}
	public boolean isBomb() {
		// should only be used after the isBomb() with parameters is called
		if(hidden == Role.Bomb) {
			return true;
		} else {
			return false;
		}
	}
	
	// getting surrounding tiles
	public ArrayList<Long> globalSurrounding() {
		// format: [[x,y], [x,y], ...]
		ArrayList<Long> tiles = new ArrayList<>();
		
		tiles.add(Chunk.getKey(xGlobal-1, yGlobal-1));
		tiles.add(Chunk.getKey(xGlobal-1, yGlobal));
		tiles.add(Chunk.getKey(xGlobal-1, yGlobal+1));
		tiles.add(Chunk.getKey(xGlobal, yGlobal-1));
		tiles.add(Chunk.getKey(xGlobal, yGlobal+1));
		tiles.add(Chunk.getKey(xGlobal+1, yGlobal-1));
		tiles.add(Chunk.getKey(xGlobal+1, yGlobal));
		tiles.add(Chunk.getKey(xGlobal+1, yGlobal+1));
		
		return tiles;		
	}
	public ArrayList<Long> localSurrounding() {
		// format: [[x,y], [x,y], ...]
		ArrayList<Long> tiles = new ArrayList<>();
		
		tiles.add(Chunk.getKey(xLocal-1, yLocal-1));
		tiles.add(Chunk.getKey(xLocal-1, yLocal));
		tiles.add(Chunk.getKey(xLocal-1, yLocal+1));
		tiles.add(Chunk.getKey(xLocal, yLocal-1));
		tiles.add(Chunk.getKey(xLocal, yLocal+1));
		tiles.add(Chunk.getKey(xLocal+1, yLocal-1));
		tiles.add(Chunk.getKey(xLocal+1, yLocal));
		tiles.add(Chunk.getKey(xLocal+1, yLocal+1));
		
		return tiles;
	}
	
	// making outputs more readable
	public String seeShown() {
		if (shown == Role.Unknown) {
			return "?";
		} else if (shown == Role.Flag) {
			return "F";
		} else {return seeHidden();}
	}
	public String seeHidden() {
		if (hidden == Role.Blank) {
			return "+";
		} else if (hidden == Role.Bomb) {
			return "X";
		} else if (hidden == Role.Number) {
			return Integer.toString(number);
		} else {return "in valid tile role";}
	}
}