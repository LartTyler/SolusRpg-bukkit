package me.dbstudios.solusrpg.entities.player;

import java.util.HashMap;
import java.util.Map;

import me.dbstudios.solusrpg.util.math.Expression;

public class ExperienceScaler extends Initializable {
	private static final ExperienceScaler globalScaler;

	private final Map<Integer, Integer> algorithmCache = new HashMap<>();

	private Expression algorithm;

	static {
		ExperienceScaler.initialize();
	}

	public static void initialize() {
		if (initialized) {
			SolusRpg.log(Level.WARNING, "Additional calls made to RpgClass.initialize(); this may indicate an internal optimization issue, and should be reported to my Creator.");

			return;
		}

		long initStart = System.currentTimeMillis();

		File f = new File(Directories.CONFIG + "config.yml");

		if (!f.exists()) {
			SolusRpg.log(Level.SEVERE, "I could not find required file 'config.yml'; if I did not unpack a copy for you, please report this issue to my Creator.");

			return;
		}

		FileConfiguration conf = YamlConfiguration.load(f);
	}

	public ExperienceScaler(String algorithm) {
		this.algorithm = new Expression(algorithm);
	}

	public float toRealExp(int currentLevel, int exp) {
		return (float)(exp / this.getLevelTotal(currentLevel));
	}

	public int fromRealExp(int currentLevel, float exp) {
		return (int)Math.floor((double)this.getLevelTotal(currentLevel) * (double)exp);
	}

	public int getLevelTotal(int level) {
		if (algorithmCache.containsKey(level))
			return algorithmCache.get(level);

		int total = (int)Math.floor(
			algorithm
				.clearParameters()
				.setParameter("level", level)
				.eval();
		);

		algorithmCache.put(level, total);

		return total;
	}

	public Expression getAlgorithm() {
		return this.algorithm;
	}

	public ExperienceScaler setAlgorithm(String algorithm) {
		this.algorithm = new Expression(algorithm);
		algorithmCache.clear();

		return this;
	}

	public static ExperienceScaler getGlobalScaler() {
		return globalScaler;
	}
}