package me.dbstudios.util.math;

import java.util.HashMap;
import java.util.Map;

public class Expression {
	private final static Map<String, Operator> operators = new HashMap<>();

	static {
		addOperator(new Operator("+", 100) {
			public double eval(double a, double b) {
				return a + b;
			}
		});

		addOperator(new Operator("-", 100) {
			public double eval(double a, double b) {
				return a - b;
			}
		});

		addOperator(new Operator("*", 200) {
			public double eval(double a, double b) {
				return a * b;
			}
		});

		addOperator(new Operator("/", 200) {
			public double eval(double a, double b) {
				return a / b;
			}
		});
	}

	public static void addOperator(Operator operator) {
		operators.put(operator.getSymbol(), operator);
	}
}