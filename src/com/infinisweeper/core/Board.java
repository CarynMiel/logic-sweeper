package com.infinisweeper.core;

import java.util.*;

public class Board 
{
	// randomizer
	final private static long xPrime = 73856093L;
	final private static long yPrime = 19349663L;
	final private static long start = 12345678;
	
	// attributes
	final private long seed;
	private Map<Long, Chunk> save = new HashMap<>();
	private Map<Long, Tile> board = new HashMap<>();
	private double density;
	private int xChunkMin = 0, xChunkMax = 0;
	private int yChunkMin = 0, yChunkMax = 0;
	private int xMin = 0, xMax = 0;
	private int yMin = 0, yMax = 0;
	
	// constructor
	public Board(double density, long seed) {
		this.density = density;
		this.seed = seed;
	}
	
	// methods
	
	// getter methods
	public long getSeed() {return seed;}
	public Map<Long, Chunk> getSave() {return save;}
	public Map<Long, Tile> getBoard() {return board;}
	public double getDensity() {return density;}
	public int getXChunkMin() {return xChunkMin;}
	public int getXChunkMax() {return xChunkMax;}
	public int getYChunkMin() {return yChunkMin;}
	public int getYChunkMax() {return yChunkMax;}
   	public int getXMin() {return xMin;}
	public int getXMax() {return xMax;}
	public int getYMin() {return yMin;}
	public int getYMax() {return yMax;}
	
	// updates the global minimum and maximums
	public void updateNewTile(Tile tile) {
		int x = tile.getXGlobal();
		int y = tile.getYGlobal();
		long key = tile.getKey();
		
		board.put(key, tile);
		
		// updating minimum and maximums
		if(x < xMin) {xMin = x;}
		else if(x > xMax) {xMax = x;}
		
		if(y < yMin) {yMin = y;}
		else if(y > yMax) {yMax = y;}
		
		ArrayList<int[]> around = tile.globalSurrounding();
		for(int[] pos : around) {
			x = pos[0];
			y = pos[1];
			key = Tile.getKey(x, y);
			if(board.get(key) == null) {board.put(key, new Tile(x, y));}
		}
	}
	
	// opens tiles using queues
	public void open(int x, int y) {
		Queue<Tile> queue =  new ArrayDeque<>();
		Tile tile = new Tile(x, y);
		queue.add(tile);
		
		while (!queue.isEmpty()) {
			System.out.println("Queue: " + queue);
			Tile current = queue.poll();
			int xCur = current.getXGlobal();
			int yCur = current.getYGlobal();
			long key = current.getKey();
			int value = getTileVal(xCur, yCur);
			updateNewTile(current);
			
			if(value > 0) {
				current.placeNumber(value);
				continue;}
			else if(value == -1) {
				current.placeBomb();
				continue;}
			else {
				board.put(key, current);
				ArrayList<int[]> around = current.globalSurrounding();
				for(int[] pos : around) {
					Tile next = new Tile(pos[0], pos[1]);
					key = next.getKey();
					if(board.get(key) == null) {queue.add(next);}
				}
			}
		}
	}
	
	// determines the number at position xGlobal yGlobal
	public int getTileVal(int x, int y) {
		if(isBomb(x, y)) {
			return -1;
		} else {
			int count = 0;
			
			ArrayList<int[]> around = Tile.getSurrounding(x, y);
			// iterates through positions around and adds 1 if there was a bomb there
			for (int[] pos : around) {if(isBomb(pos[0], pos[1])) {count++;}}
			
			return count;
		}
		
	}
	
	// determines if there is a bomb at position xGlobal yGlobal
	public boolean isBomb(int x, int y) {
		long hash = start;
		hash ^= x * xPrime;
		hash ^= y* yPrime;
		hash ^= seed;
		
		hash = hash >> 1;
		
		return (((double)hash / (double)Long.MAX_VALUE)) < density;		
	}
	
	// making outputs more readable
	public String hiddenTiles() {
		String text = "Hidden Board\n  ";
		
		// adding markers to the x axis
		for(int i=xMin; i<=xMax; i++) {
			if(Math.abs(i) % 10 == 0) {text += Math.abs(i)/10 + " ";}
			else {text += Math.abs(i)%10 + " ";}
		} text += "\n";
		
		for(int i=yMin; i<=yMax; i++) {
			// adding markers to the y axis
			if(Math.abs(i)%10 == 0) {text += Math.abs(i)/10 + " ";}
			else {text += Math.abs(i)%10 + " ";}
			// printing the row chunk by chunk
			for(int j=xMin; j<=xMax; j++) {
				long key = Tile.getKey(j, i);
				Tile tile = board.get(key);
				if(tile == null) {text += "  ";}
				else {text += tile.seeHidden() + " ";}
			} text += "\n";
		}
				
		return text;
	}
	public String shownTiles() {
		String text = "Shown Board\n  ";
		
		// adding markers to the x axis
		for(int i=xMin; i<=xMax; i++) {
			if(Math.abs(i) % 10 == 0) {text += Math.abs(i)/10 + " ";}
			else {text += Math.abs(i)%10 + " ";}
		} text += "\n";
		
		for(int i=yMin; i<=yMax; i++) {
			// adding markers to the y axis
			if(Math.abs(i)%10 == 0) {text += Math.abs(i)/10 + " ";}
			else {text += Math.abs(i)%10 + " ";}
			// printing the row chunk by chunk
			for(int j=xMin; j<=xMax; j++) {
				long key = Tile.getKey(j, i);
				Tile tile = board.get(key);
				if(tile == null) {text += "  ";}
				else {text += tile.seeShown() + " ";}
			} text += "\n";
		}
				
		return text;
	}
	public String hiddenChunks() {
		String text = "Hidden Board\n  ";
		
		// adding markers to the x axis
		for(int i=xMin; i<=xMax; i++) {
			if(Math.abs(i) % 10 == 0) {text += Math.abs(i)/10 + " ";}
			else {text += Math.abs(i)%10 + " ";}
		} text += "\n";
		
		for(int i=yMin; i<=yMax; i++) {
			// adding markers to the y axis
			if(Math.abs(i)%10 == 0) {text += Math.abs(i)/10 + " ";}
			else {text += Math.abs(i)%10 + " ";}
			// printing the row chunk by chunk
			for(int j=xChunkMin; j<=xChunkMax; j++) {
				long key = Chunk.getKey(j, Chunk.getYChunk(i));
				Chunk chunk = save.get(key);
				if(chunk == null) {text += "  ";}
				else {text += chunk.seeHiddenRow(i%Chunk.ChunkSize) + " ";}
			} text += "\n";
		}
		
		return text;
	}
	public String shownChunks() {
		String text = "Shown Board\n  ";
		
		for(int i=yMin; i<=yMax; i++) {
			// adding markers to the y axis
			if(Math.abs(i)%10 == 0) {text += Math.abs(i)/10 + " ";}
			else {text += Math.abs(i)%10 + " ";}
			// printing the row chunk by chunk
			for(int j=xChunkMin; j<=xChunkMax; j++) {
				long key = Chunk.getKey(j, Chunk.getYChunk(i));
				Chunk chunk = save.get(key);
				if(chunk == null) {text += "  ";}
				else {text += chunk.seeShownRow(i%Chunk.ChunkSize) + " ";}
			} text += "\n";
		}
		
		return text;
	}
	// override methods
	@Override
	public String toString() {
		String text = "";
		
		text += hiddenTiles();
		text += "\n\n";
		text += shownTiles();
		
		return text;
	}
}