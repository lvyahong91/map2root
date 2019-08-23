package com.lenovo.map2root;
/**
 * 从阶乘的递归看入栈情况
 * @author Administrator
 *
 */
public class Factorial {
	public static int getFactorial(int n) {
		if (n>0) {
			int sum=n+getFactorial(n-1);
			return sum;
		} else {
			return 0;
		}
	}
	
	public static void main(String[] args) {
		System.out.println(getFactorial(10));
	}
}
