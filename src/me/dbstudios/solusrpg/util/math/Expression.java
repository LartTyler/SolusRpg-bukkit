package me.dbstudios.solusrpg.util.math;

import java.util.HashMap;
import java.util.Map;

/**
 * A math expression evaluator class, inspired by EvalEx by uklimaschewski (https://github.com/uklimaschewski/EvalEx).
 */
public class Expression {
	private final static Map<String, Operator> operators = new HashMap<>();

	private final Map<String, Double> parameters = new HashMap<>();
	private final String expr;

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

		addOperator(new Operator("^", 250) {
			public double eval(double a, double b) {
				return Math.pow(a, b);
			}
		});
	}

	public Expression(String expr) {
		this.expr = expr;
	}

	public double eval(String expr) {
		Tokenizer tokenizer = new Tokenizer(expr);
		Double res = null;

		while (tokenizer.hasNext()) {
			String token = tokenizer.next();

			if (token.equals("(")) {
				StringBuilder nextExpr = new StringBuilder();
				int parenDepth = 0;

				while (tokenizer.hasNext()) {
					String t = tokenizer.next();

					if (t.equals(")") && parenDepth == 0)
						break;

					if (t.equals(")"))
						parenDepth--;
					else if (t.equals("("))
						parenDepth++;

					nextExpr.append(t);
				}

				if (res == null)
					res = this.eval(nextExpr.toString());
				else
					res += this.eval(nextExpr.toString());
			} else if (Expression.isNumber(token) && res == null) {
				try {
					res = Double.valueOf(token);
				} catch (NumberFormatException e) {
					throw new RuntimeException("Invalid token " + token);
				}
			} else if (this.hasParameter(token) && res == null) {
				res = this.getParameter(token);
			} else if (Expression.isOperator(token) && res != null && tokenizer.hasNext()) {
				String nextToken = tokenizer.next();
				Double nextValue = null;

				if (nextToken.equals("(")) {
					StringBuilder nextExpr = new StringBuilder();
					int parenDepth = 0;

					while (tokenizer.hasNext()) {
						String t = tokenizer.next();

						if (t.equals(")") && parenDepth == 0)
							break;

						if (t.equals(")"))
							parenDepth--;
						else if (t.equals("("))
							parenDepth++;

						nextExpr.append(t);
					}

					nextValue = this.eval(nextExpr.toString());
				} else if (this.hasParameter(nextToken)) {
					nextValue = this.getParameter(nextToken);
				} else if (Expression.isNumber(nextToken))
					try {
						nextValue = Double.valueOf(nextToken);
					} catch (NumberFormatException e) {
						throw new RuntimeException("Invalid token " + nextToken);
					}

				if (nextValue == null)
					throw new RuntimeException("Invalid token " + nextToken);

				if (tokenizer.hasNext()) {
					String peekedToken = tokenizer.peek();

					if (Expression.isOperator(peekedToken) && Expression.getOperator(peekedToken).getLevel() > Expression.getOperator(token).getLevel()) {
						StringBuilder nextEval = new StringBuilder(nextValue.toString());

						while (tokenizer.hasNext())
							nextEval.append(tokenizer.next());

						return Expression.getOperator(token).eval(res, this.eval(nextEval.toString()));
					}
				}

				res = Expression.getOperator(token).eval(res, nextValue);
			} else {
				throw new RuntimeException("Invalid token " + token);
			}
		}

		if (res == null)
			throw new RuntimeException("Invalid expression " + expr);

		return res;
	}

	public double eval() {
		return this.eval(this.expr);
	}

	public Double getParameter(String param) {
		return this.getParameter(param, 0.0);
	}

	public Double getParameter(String param, Integer def) {
		return this.getParameter(param, (double)def);
	}

	public Double getParameter(String param, Float def) {
		return this.getParameter(param, (double)def);
	}

	public Double getParameter(String param, Double def) {
		if (this.hasParameter(param))
			return parameters.get(param);

		return def;
	}

	public Expression setParameter(String param, double value) {
		parameters.put(param, value);

		return this;
	}

	public Expression removeParameter(String param) {
		parameters.remove(param);

		return this;
	}

	public boolean hasParameter(String param) {
		return parameters.containsKey(param);
	}

	public Expression clearParameters() {
		parameters.clear();

		return this;
	}
	public static boolean isOperator(String symbol) {
		return operators.containsKey(symbol);
	}

	public static Operator getOperator(String symbol) {
		return operators.get(symbol);
	}

	public static boolean isNumber(String str) {
		if (!Character.isDigit(str.charAt(0)) && str.length() == 1)
			return false;

		for (int i = 0; i < str.length(); i++)
			if (!Character.isDigit(str.charAt(i)) && !(str.charAt(i) == '-' && i == 0) && str.charAt(i) != '.')
				return false;

		return true;
	}

	public static void addOperator(Operator operator) {
		operators.put(operator.getSymbol(), operator);
	}
}