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
		String s = "if(orderPrice > 5 && orderPrice < 10) "
				+ "return orderPrice-1.5;"
				+ "else if(orderPrice >= 10 && orderPrice < 20)"
				+ "return orderPrice-2;"
				+ "else return orderPrice-3";
		System.err.println(MathUtil.calculateByOrderPrice(765, s));
		
	}
}
