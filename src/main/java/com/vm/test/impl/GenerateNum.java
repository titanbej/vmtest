package com.vm.test.impl;

public class GenerateNum {

	String number="";

	public String generateNumber(int goal, int step) {
		
		number=number+goal+",";
		if(goal>= step) {
			goal= goal-step;
			generateNumber(goal, step);
		}
		return number;
	}
	
	public static void main(String[] args) {
		System.out.println(new GenerateNum().generateNumber(10,2));
	}
}
