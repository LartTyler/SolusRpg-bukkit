package me.dbstudios.solusrpg.chat;

import me.dbstudios.solusrpg.util.math.Expression;

public class DistortionManager {
	private int maxRange;
	private double distThreshold;
	private double chanceThreshold;
	private Expression algo;

	public DistortionManager(int maxRange, double distThreshold, double chanceThreshold, String algo) {
		this.maxRange = maxRange;
		this.distThreshold = distThreshold;
		this.chanceThreshold = chanceThreshold;
		this.algo = new Expression(algo);
	}

	public String distort(String str, RpgPlayer sender, RpgPlayer reciever) {
		double chance = 0.0;
		double distance = 0.0;

		// HERE
	}
}