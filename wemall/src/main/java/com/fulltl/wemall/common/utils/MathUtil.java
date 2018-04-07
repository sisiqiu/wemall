package com.fulltl.wemall.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

import bsh.EvalError;
import bsh.Interpreter;

public class MathUtil {
	public static Integer calculateByOrderPrice(Integer orderPrice, String expression) {
		Interpreter interpreter = new Interpreter();
		try {
			interpreter.set("orderPrice", Double.parseDouble(orderPrice.toString())/100);
			Object object = interpreter.eval(expression);
			return new BigDecimal(Double.parseDouble(object.toString()))
								.multiply(new BigDecimal(100))
								.setScale(0, RoundingMode.HALF_UP)
								.intValue();
		} catch (EvalError e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		String s = "if(orderPrice > 50) return orderPrice-30;"
				+ "else return 1";
		System.err.println(MathUtil.calculateByOrderPrice(100, s));
		
	}
}
