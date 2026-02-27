package com.infinisweeper.core;

import java.util.*;


public class Tile 
{
	// attributes	
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
		
		// initializing the local coordinates
		if(xGlobal >= 0) {xLocal = xGlobal % Chunk.ChunkSize;} 
		else {xLocal = Chunk.ChunkSize - (Math.abs(xGlobal) % Chunk.ChunkSize);}
		
		if(yGlobal >= 0) {yLocal = yGlobal % Chunk.ChunkSize;} 
		else {yLocal = Chunk.ChunkSize - (Math.abs(yGlobal) % Chunk.ChunkSize);}
		
		// getting the chunkKey
		int[] chunk = Chunk.getChunk(xGlobal, yGlobal);
		chunkKey = Chunk.getKey(chunk[0], chunk[1]);
	}
	
	// methods
	
	// static methods
	// getting the key of the tile
	public static long getKey(int x, int y) {
		// making x occupy the upper 32 bits
		long xLong = (long) x << 32;
				
		// making y occupy the lower 32 bits
		long yLong = (long) y & 0xffffffffL;
				
		// combining the two to create the key
		return xLong | yLong;
	}
	
	// get the tile pos from the key
	public static int[] getPos(long key) {
		int[] chunk = new int[2];
		
		chunk[0] = (int) (key >> 32);
		chunk[1] = (int) (key & 0x00000000ffffffffL);
		
		return chunk;
	}
	
	// getting surrounding tiles
	public static ArrayList<int[]> getSurrounding(int x, int y) {
		// format: [[x,y], [x,y], ...]
		ArrayList<int[]> tiles = new ArrayList<>();
		
		tiles.add(new int[] {x-1, y-1});
		tiles.add(new int[] {x-1, y});
		tiles.add(new int[] {x-1, y+1});
		tiles.add(new int[] {x, y-1});
		tiles.add(new int[] {x, y+1});
		tiles.add(new int[] {x+1, y-1});
		tiles.add(new int[] {x+1, y});
		tiles.add(new int[] {x+1, y+1});
		
		return tiles;		
	}
		
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
	
	// getting surrounding tiles
	public ArrayList<int[]> globalSurrounding() {
		// format: [[x,y], [x,y], ...]
		ArrayList<int[]> tiles = new ArrayList<>();
		
		tiles.add(new int[] {xGlobal-1, yGlobal-1});
		tiles.add(new int[] {xGlobal-1, yGlobal});
		tiles.add(new int[] {xGlobal-1, yGlobal+1});
		tiles.add(new int[] {xGlobal, yGlobal-1});
		tiles.add(new int[] {xGlobal, yGlobal+1});
		tiles.add(new int[] {xGlobal+1, yGlobal-1});
		tiles.add(new int[] {xGlobal+1, yGlobal});
		tiles.add(new int[] {xGlobal+1, yGlobal+1});
		
		return tiles;		
	}
	public ArrayList<int[]> localSurrounding() {
		// format: [[x,y], [x,y], ...]
		ArrayList<int[]> tiles = new ArrayList<>();
		
		tiles.add(new int[] {xLocal-1, yLocal-1});
		tiles.add(new int[] {xLocal-1, yLocal});
		tiles.add(new int[] {xLocal-1, yLocal+1});
		tiles.add(new int[] {xLocal, yLocal-1});
		tiles.add(new int[] {xLocal, yLocal+1});
		tiles.add(new int[] {xLocal+1, yLocal-1});
		tiles.add(new int[] {xLocal+1, yLocal});
		tiles.add(new int[] {xLocal+1, yLocal+1});
		
		return tiles;
	}
	
	// getting the key of the tile 
	public long getKey() {
		// making x occupy the upper 32 bits
		long xLong = (long) xGlobal << 32;
				
		// making y occupy the lower 32 bits
		long yLong = (long) yGlobal & 0xffffffffL;
				
		// combining the two to create the key
		return xLong | yLong;
	}
	
	// update tile number
	public void placeNumber(int num) {
		hidden = Role.Number;
		number = num;
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