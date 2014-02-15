package me.dbstudios.solusrpg.entities.player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.config.Directories;
import me.dbstudios.solusrpg.util.math.Expression;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ExperienceScaler {
	private static final String defaultLevelCost = "if(level <= 15, 17 * level, if(between(level, 16, 30), 1.5 * level^2 - 29.5 * level + 360, 3.5 * level^2 - 151.5 * level + 2220))";
	
	private static ExperienceScaler globalScaler;
	private static boolean initialized = false;

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

		FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
		
		globalScaler = new ExperienceScaler(conf.getString("leveling.global-level-cost", defaultLevelCost));
		initialized = true;

		SolusRpg.log(String.format("Initialized global experience scaler in %d milliseconds.", System.currentTimeMillis() - initStart));
	}

	public ExperienceScaler(String algorithm) {
		this.algorithm = new Expression(algorithm);
	}

	public float toRealExp(int currentLevel, int exp) {
		return (float)(exp / this.getLevelCost(currentLevel));
	}

	public int fromRealExp(int currentLevel, float exp) {
		return (int)Math.floor((double)this.getLevelCost(currentLevel) * (double)exp);
	}

	public int getLevelCost(int level) {
		if (algorithmCache.containsKey(level))
			return algorithmCache.get(level);

		int cost = (int)Math.floor(
			algorithm
				.clearParameters()
				.setParameter("level", level)
				.eval()
		);

		algorithmCache.put(level, cost);

		return cost;
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