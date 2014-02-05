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

		this.regenAmount = (int)Math.floor(
			this.regenFormula
				.clearParameters()
				.setParameter("max_health", max)
				.eval()
		);
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

		return this.refreshRegenAmount();
	}

	public String getName() {
		return this.name;
	}

	public VitalMeter setName(String meterName) {
		this.name = meterName;

		return this;
	}

	public long getRegenRate() {
		return this.regenRate;
	}

	public VitalMeter setRegenRate(long regenRate) {
		this.regenRate = regenRate;

		return this;
	}

	public VitalMeter setRegenRate(double regenRate) {
		return this.setRegenRate((int)Math.ceil(regenRate * 20));
	}

	public int getRegenAmount() {
		return this.regenAmount;
	}

	public VitalMeter refreshRegenAmount() {
		this.regenAmount = (int)Math.floor(
			this.regenFormula
				.clearParameters()
				.setParameter("max_health", this.getMax())
				.eval()
		);

		return this;
	}

	public VitalMeter damage(int amount) {
		return this.set(Math.max(0, this.get() - amount));
	}

	public VitalMeter heal(int amount) {
		return this.set(Math.min(this.getMax(), this.get() - amount));
	}
}