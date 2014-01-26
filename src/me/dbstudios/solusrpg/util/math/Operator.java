package me.dbstudios.solusrpg.util.math;

public abstract class Operator {
	private final String symbol;
	private final int level;

	public Operator(String symbol, int level) {
		this.symbol = symbol;
		this.level = level;
	}

	public abstract double eval(double a, double b);

	public String getSymbol() {
		return this.symbol;
	}

	public int getLevel() {
		return this.level;
	}
}