package me.dbstudios.solusrpg.entities.meters;

import me.dbstudios.solusrpg.util.math.Expression;

public class HealthMeter extends VitalMeter {
	private Expression regenFormula;
	private long regenRate;
	private int max;
	private int current;

	public HealthMeter(int max, String regenFormula, long regenRate) {

	}
}