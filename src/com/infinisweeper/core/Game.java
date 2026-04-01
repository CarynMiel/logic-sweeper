package com.infinisweeper.core;

import java.util.*;

public class Game {
	private Board hidden;
	private Map<Long, Character> shown = new HashMap<>();
	private int xMax = 1, xMin = -1, yMax = 1, yMin = -1;
	
	public Game(double density, long seed) {
		hidden = new Board(density, seed);
	}
	
	public int flagCount(long key) {
		int count = 0;
		Long[] around = Tile.surroundingOf(key);
		for(long coord : around) {
			if(isFlagged(coord)) {count++;}
		}
		return count;
	}
	public int flagCount(int x, int y) {
		return flagCount(Tile.key(x, y));
	}
	
	public void flag(long key) {
		if(shown.get(key) == null || shown.get(key) == Tile.Unknown) {
			shown.put(key, Tile.Flag);
		} else if(shown.get(key) == Tile.Flag){
			shown.put(key, Tile.Unknown);
		}
	}
	public void flag(int x, int y) {
		flag(Tile.key(x, y));
	}
	
	public boolean isFlagged(long key) {
		if(shown.get(key) == null) {return false;}
		return shown.get(key) == Tile.Flag;
	}
	public boolean isFlagged(int globalX, int globalY) {
		return isFlagged(Tile.key(globalX, globalY));
	}
	
	public void open(int globalX, int globalY) {
		Queue<Long> queue = new ArrayDeque<>();
		queue.add(Tile.key(globalX, globalY));
		
		// surrounding open if number matches flags around
		if(shown.get(queue.peek()) != null) {
			boolean digit = Character.isDigit(shown.get(queue.peek()));
			boolean match = (char) (flagCount(globalX, globalY) + '0') == hidden.valueAt(globalX, globalY);
			if(digit && match) {
				Long[] around = Tile.surroundingOf(queue.peek());
				for(long coord : around) {
					queue.add(coord);
				}
			}
		}
		
		// flood open for blank tiles
		while(!queue.isEmpty()) {
			long key = queue.remove();
			
			if(shown.containsKey(key) && shown.get(key) != Tile.Unknown) {continue;}
			shown.put(key, hidden.valueAt(key));
			
			// updates the minimum and maximum
			if(Tile.x(key) < xMin) {xMin = Tile.x(key);}
			else if(Tile.x(key) > xMax) {xMax = Tile.x(key);}
			
			if(Tile.y(key) < yMin) {yMin = Tile.y(key);}
			else if(Tile.y(key) > yMax) {yMax = Tile.y(key);}
			
			// Opening a blank tile
			if(hidden.valueAt(key) == Tile.Blank) {
				Long[] around = Tile.surroundingOf(key);
				for(long coord : around) {
					queue.add(coord);
				}
			}
		}
	}
	public void open(long key) {
		open(Tile.x(key), Tile.y(key));
	}
	
	public String seeHidden() {
		String text = "  ";
		for(int x=xMin; x<=xMax; x++) {
			if(x % 10 == 0) {
				text += Math.abs(x/10) + " ";
			} else {
				text += Math.abs(x%10) + " ";
			}
		} text += "\n";
		for(int y=yMin; y<=yMax; y++) {
			if(y % 10 == 0) {
				text += Math.abs(y/10) + " ";
			} else {
				text += Math.abs(y%10) + " ";
			}
			for(int x=xMin; x<=xMax; x++) {
				text += hidden.valueAt(x, y) + " ";
			} text += "\n";
		} return text;
	}
	
	public String seeShown() {
		String text = "  ";
		for(int x=xMin; x<=xMax; x++) {
			if(x % 10 == 0) {
				text += Math.abs(x/10) + " ";
			} else {
				text += Math.abs(x%10) + " ";
			} 
		} text += "\n";
		for(int y=yMin; y<=yMax; y++) {
			if(y % 10 == 0) {
				text += Math.abs(y/10) + " ";
			} else {
				text += Math.abs(y%10) + " ";
			}
			for(int x=xMin; x<=xMax; x++) {
				if(shown.get(Tile.key(x, y)) == null) {
					text += Tile.Unknown + " ";
				} else {
					text += shown.get(Tile.key(x, y)) + " ";
				}
			} text += "\n";
		} return text;
	}
	
	public Board hidden() {return hidden;}
	public Map<Long, Character> shown() {return shown;}
	public int xMin() {return xMin;}
	public int xMax() {return xMax;}
	public int yMin() {return yMin;}
	public int yMax() {return yMax;}
}