package com.infinisweeper.core;

public abstract class Tile {
	
	public static final char Blank = ' ';
	public static final char Bomb = 'X';
	public static final char Flag = 'F';
	public static final char Unknown = '?';
	
	public static int[][] surroundingOf(int globalX, int globalY) {
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
	
	public static Long[] surroundingOf(long key) {
		Long[] around = new Long[8];
		int x = x(key), y = y(key);
		
		around[0] = key(x-1, y-1);
		around[1] = key(x-1, y);
		around[2] = key(x-1, y+1);
		around[3] = key(x, y-1);
		around[4] = key(x, y+1);
		around[5] = key(x+1, y-1);
		around[6] = key(x+1, y);
		around[7] = key(x+1, y+1);
		
		return around;
}
	
	public static long key(int globalX, int globalY) {
		long x = (long) globalX << 32;
		long y = (long) globalY & 0xffffffffL;
		return x | y;
	}
	
	public static int x(long key) {
		return (int) (key >> 32);
	}
	
	public static int y(long key) {
		return (int) (key & 0x00000000ffffffffL);
	}
}