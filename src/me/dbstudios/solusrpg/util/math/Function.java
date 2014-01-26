package me.dbstudios.solusrpg.util.math;

public abstract class Function {
	private final String name;

	private double argDefault = 0;

	public Function(String name) {
		this(name, 0);
	}

	public Function(String name, double argDefault) {
		this.name = name;
		this.argDefault = argDefault;
	}

	public String getName() {
		return this.name;
	}

	public void setArgumentDefault(double argDefault) {
		this.argDefault = argDefault;
	}

	public double getArgumentDefault() {
		return this.argDefault;
	}

	public abstract double eval(double arg);
}