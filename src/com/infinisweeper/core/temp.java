package com.infinisweeper.core;

import java.util.*;

public class temp
{
	public static void main(String[] args)
	{
		Random rand = new Random();
		long num = rand.nextLong();
		System.out.println(num);
		
		Board one = new Board(0.10, num);
		
		System.out.println(one);
		
		one.open(0, 0);
		
		System.out.println(one);
	}
}

// 8495