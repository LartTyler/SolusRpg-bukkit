package me.dbstudios.solusrpg.chat;

import me.dbstudios.solusrpg.util.math.Expression;

public class DistortionManager {
	private final Map<Double, Double> algoCache = new HashMap<>();

	private int maxRange;
	private double distThreshold;
	private double chanceThreshold;
	private Expression algo;

	public DistortionManager(int maxRange, double distThreshold, double chanceThreshold, String algo) {
		this.maxRange = maxRange;
		this.distThreshold = Math.pow(distThreshold, 2);	// Store as distance squred for a faster initial lookup
		this.chanceThreshold = chanceThreshold;
		this.algo = new Expression(algo);
	}

	public String distort(String str, RpgPlayer sender, RpgPlayer reciever) {
		double dSquared = sender.getLocation().distanceSquared(reciever.getLocation());

		if (dSquared < this.distThreshold)
			return str;

		double chance = 0.0;

		if (algoCache.containsKey(dSquared))
			chance = algoCache.get(dSquared);
		else
			chance = algo
				.clearParameters()
				.setParameter("distance", sender.getLocation().distance(reciever.getLocation()))
				.eval();

		algoCache.put(dSquared, chance);

		StringBuilder sb = new StringBuilder();

		for (char c : str.toCharArray())
			if (!Character.isWhitespace(c))
				if (Math.random() * 100 < chance)
					sb.append(".")
				else
					sb.append(c);
			else
				sb.append(c);

		return sb.toString();
	}
}