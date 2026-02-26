package com.infinisweeper.core;

public class Chunk
{
	// attributes
	public static final int ChunkSize = 16;
	private Tile[][] contents = new Tile[ChunkSize][ChunkSize];
	private final long key;
	private final int x, y;
	
	// constructor
	public Chunk(int x, int y)
	{
		this.x = x;
		this.y = y;
		
		// making x occupy the upper 32 bits
		long xLong = (long) x << 32;
		
		// making y occupy the lower 32 bits
		long yLong = (long) y & 0xffffffffL;
		
		// combining the two to create the key
		key = xLong | yLong;
	}
	
	// methods
	
	// getter methods
	public Tile[][] getContents() {return contents;}
	public long getKey() {return key;}
	public int getX() {return x;}
	public int getY() {return y;}
	
	// adding a new tile to the chunk
	public void newTile(Tile tile) {
		contents[tile.getYLocal()][tile.getXLocal()] = tile;
	}
	
	// getting a single tile from the chunk
	public Tile getTile(int xLocal, int yLocal) {
		return contents[yLocal][xLocal];
	}
	
	// making outputs more readable
	public String seeHiddenRow(int index) {
		String text = "";
		
		for(Tile tile : contents[index]) {
			if(tile == null) {text += "  ";}
			else {text += tile.seeHidden() + " ";}
		} text += "\n";
		
		return text;
	}
	public String seeShownRow(int index) {
		String text = "";
		
		for(Tile tile : contents[index]) {
			if(tile == null) {text += "  ";}
			else {text += tile.seeShown() + " ";}
		} text += "\n";
		
		return text;
	}
 	public String seeHiddenChunk() {
		String text = "";
		
		for(Tile[] row : contents) {
			for(Tile tile : row) {
				if(tile == null) {text += "  ";}
				else {text += tile.seeHidden() + " ";}
			} text += "\n";
		}
		
		return text;
	}
	public String seeShownChunk() {
		String text = "";
		
		for(Tile[] row : contents) {
			for(Tile tile : row) {
				if(tile == null) {text += "  ";}
				else {text += tile.seeShown() + " ";}
			} text += "\n";
		}
		return text;
	}
	
	// Override methods
	@Override
	public String toString() {
		String text = "";
		
		text += "Hidden Chunk\n";
		text += seeHiddenChunk();
		text += "\n\n";
		text += "Shown Cunk\n";
		text += seeShownChunk();
		
		return text;
	}
	
}