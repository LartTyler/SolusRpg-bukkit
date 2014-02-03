package me.dbstudios.solusrpg.entities.meters;

import me.dbstudios.solusrpg.util.math.Expression;

public class HealthMeter implements VitalMeter {
	private Expression regenFormula;
	private String name;
	private int regenAmount;
	private long regenRate;
	private int max;
	private int current;

	public HealthMeter(int max, String regenFormula, long regenRate, String meterName) {
		this.max = max;
		this.current = max;
		this.regenFormula = new Expression(regenFormula);
		this.regenRate = regenRate;
		this.name = meterName;

		this.regenAmount = this.regenFormula
			.clearParameters()
			.setParameter("max_health", max)
			.eval();
	}

	public int get() {
		return this.current;
	}

	public VitalMeter set(int current) {
		this.current = current;

		return this;
	}

	public int getMax() {
		return this.max;
	}

	public VitalMeter setMax(int max) {
		this.max = max;

		return this;
	}

	public String getName() {
		return this.name;
	}

	public VitalMeter setName(String meterName) {
		this.name = meterName;

		return this;
	}
}