package me.dbstudios.solusrpg.util.math;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A math expression evaluator class, inspired by EvalEx by uklimaschewski (https://github.com/uklimaschewski/EvalEx).
 */
public class Expression {
	private static final Map<String, Operator> operators = new HashMap<>();
	private static final Map<String, Function> functions = new HashMap<>();
	private static final Map<String, FlowControl> controllers = new HashMap<>();

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

		// Boolean operators - a return value of 0.0 signifies false; anything else signifies true

		addOperator(new Operator(">", 50) {
			public double eval(double a, double b) {
				return a > b ? 1.0 : 0.0;
			}
		});

		addOperator(new Operator("<", 50) {
			public double eval(double a, double b) {
				return a < b ? 1.0 : 0.0;
			}
		});

		addOperator(new Operator("==", 50) {
			public double eval(double a, double b) {
				return a == b ? 1.0 : 0.0;
			}
		});

		addOperator(new Operator(">=", 50) {
			public double eval(double a, double b) {
				return a >= b ? 1.0 : 0.0;
			}
		});

		addOperator(new Operator("<=", 50) {
			public double eval(double a, double b) {
				return a <= b ? 1.0 : 0.0;
			}
		});

		addOperator(new Operator("!=", 50) {
			public double eval(double a, double b) {
				return a != b ? 1.0 : 0.0;
			}
		});

		addOperator(new Operator("&&", 75) {
			public double eval(double a, double b) {
				return a != 0.0 && b != 0.0 ? 1.0 : 0.0;
			}
		});

		addOperator(new Operator("||", 70) {
			public double eval(double a, double b) {
				return a != 0.0 || b != 0.0 ? 1.0 : 0.0;
			}
		});

		addFunction(new Function("abs") {
			public double eval(double arg) {
				return Math.abs(arg);
			}
		});

		addFunction(new Function("sin") {
			public double eval(double arg) {
				return Math.sin(arg);
			}
		});

		addFunction(new Function("cos") {
			public double eval(double arg) {
				return Math.cos(arg);
			}
		});

		addFunction(new Function("tan") {
			public double eval(double arg) {
				return Math.tan(arg);
			}
		});

		addFunction(new Function("random", 1.0) {
			public double eval(double arg) {
				return Math.random() * arg;
			}
		});

		addFunction(new Function("floor") {
			public double eval(double arg) {
				return Math.floor(arg);
			}
		});

		addFunction(new Function("ceil") {
			public double eval(double arg) {
				return Math.ceil(arg);
			}
		});

		addFunction(new Function("round") {
			public double eval(double arg) {
				return (double)Math.round(arg);
			}
		});

		addFunction(new Function("ln") {
			public double eval(double arg) {
				return Math.log(arg);
			}
		});

		addFunction(new Function("log") {
			public double eval(double arg) {
				return Math.log10(arg);
			}
		});

		addFunction(new Function("sqrt") {
			public double eval(double arg) {
				return Math.sqrt(arg);
			}
		});

		addFlowControl(new FlowControl("if") {
			public String eval(String... args) {
				if (args.length != 3)
					throw new RuntimeException("Invalid number of arguments sent to if(expr, trueVal, falseVal)");

				Expression expr = new Expression(args[0]);

				if (expr.eval() != 0.0)
					return args[1];
				else
					return args[2];
			}
		});
	}

	public Expression(String expr) {
		this.expr = expr;
	}

	public double eval(String expr) {
		Tokenizer tokenizer = new Tokenizer(expr.toLowerCase());
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
			} else if (Expression.isFlowControl(token) && tokenizer.hasNext()) {
				String nextToken = tokenizer.next();

				if (!nextToken.equals("("))
					throw new RuntimeException("Invalid token " + nextToken);

				List<String> args = new ArrayList<>();
				StringBuilder nextExpr = new StringBuilder();
				int parenDepth = 0;

				while (tokenizer.hasNext()) {
					String t = tokenizer.next();

					if ((t.equals(")") || t.equals(",")) && parenDepth == 0) {
						args.add(nextExpr.toString());

						nextExpr = new StringBuilder();

						if (t.equals(")"))
							break;
						else
							continue;
					}

					if (t.equals(")"))
						parenDepth--;
					else if (t.equals("("))
						parenDepth++;

					nextExpr.append(t);
				}

				if (res != null)
					res += this.eval(Expression.getFlowControl(token).eval(args.toArray(new String[args.size()])));
				else
					res = this.eval(Expression.getFlowControl(token).eval(args.toArray(new String[args.size()])));
			} else if (Expression.isFunction(token) && tokenizer.hasNext()) {
				String nextToken = tokenizer.next();
				Double nextValue = null;

				if (!nextToken.equals("("))
					throw new RuntimeException("Invalid token " + nextToken);

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

				if (nextExpr.length() == 0)
					nextValue = Expression.getFunction(token).getArgumentDefault();
				else
					nextValue = this.eval(nextExpr.toString());

				if (res == null)
					res = Expression.getFunction(token).eval(nextValue);
				else
					res += Expression.getFunction(token).eval(nextValue);
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
				} else if (Expression.isFlowControl(nextToken)) {
					String next = tokenizer.next();

					if (!next.equals("("))
						throw new RuntimeException("Invalid token " + nextToken);

					List<String> args = new ArrayList<>();
					StringBuilder nextExpr = new StringBuilder();
					int parenDepth = 0;

					while (tokenizer.hasNext()) {
						String t = tokenizer.next();

						if ((t.equals(")") || t.equals(",")) && parenDepth == 0) {
							args.add(nextExpr.toString());

							nextExpr = new StringBuilder();

							if (t.equals(")"))
								break;
							else
								continue;
						}

						if (t.equals(")"))
							parenDepth--;
						else if (t.equals("("))
							parenDepth++;

						nextExpr.append(t);
					}

					nextValue = this.eval(Expression.getFlowControl(nextToken).eval(args.toArray(new String[args.size()])));
				} else if (Expression.isFunction(nextToken)) {
					StringBuilder nextExpr = new StringBuilder(nextToken);
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

					if (nextExpr.length() == 0)
						nextExpr
							.append("(")
							.append(Expression.getFunction(nextToken).getArgumentDefault())
							.append(")");

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
						StringBuilder nextExpr = new StringBuilder(nextValue.toString());

						while (tokenizer.hasNext())
							nextExpr.append(tokenizer.next());

						return Expression.getOperator(token).eval(res, this.eval(nextExpr.toString()));
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

	public String getAlgorithm() {
		return this.expr;
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

	public static boolean isOperator(String symbol) {
		return operators.containsKey(symbol);
	}

	public static Operator getOperator(String symbol) {
		return operators.get(symbol);
	}

	public static void addFunction(Function function) {
		functions.put(function.getName(), function);
	}

	public static boolean isFunction(String name) {
		return functions.containsKey(name);
	}

	public static Function getFunction(String name) {
		return functions.get(name);
	}

	public static void addFlowControl(FlowControl control) {
		controllers.put(control.getName(), control);
	}

	public static boolean isFlowControl(String name) {
		return controllers.containsKey(name);
	}

	public static FlowControl getFlowControl(String name) {
		return controllers.get(name);
	}
}