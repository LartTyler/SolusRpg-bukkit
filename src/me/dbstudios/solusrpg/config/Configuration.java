package me.dbstudios.solusrpg.config;

import java.util.HashMap;
import java.util.Map;

import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.util.Initializable;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public final class Configuration extends Initializable {
	private static Map<String, Object> options = new HashMap<>();

	static {
		Configuration.initialize();
	}

	private static void initialize() {
		if (initialized) {
			SolusRpg.log(Level.WARNING, "Additional calls made to Configuration.initialize(); this may indicate an internal optimization issue, and should be reported to my Creator.");

			return;
		}

		long initStart = System.currentTimeMillis();

		File f = new File(Directories.CONFIG + "config.yml");

		if (!f.exists()) {
			SolusRpg.log(Level.SEVERE, "Could not find required file 'config.yml'; if I did not unpack a copy for you, please report this issue to my Creator.");

			return;
		}

		FileConfiguration conf = YamlConfiguration.load(f);

		if (!conf.isConfigurationSection("configuration"))
			return;

		for (String key : conf.getConfigurationSection("configuration").getKeys(true))
			options.put(key, conf.get("configuration." + key));

		initialized = true;

		SolusRpg.log(String.format("Configuration initialized in %d milliseconds.", System.currentTimeMillis() - initStart));
	}

	public static boolean is(String key) {
		return Configuration.is(key, false);
	}

	public static boolean is(String key, boolean def) {
		return Configuration.getAs(key, Boolean.class, def);
	}

	public static Object get(String key) {
		return Configuration.get(key, null);
	}

	public static Object get(String key, Object def) {
		Object o = options.get(key);

		if (o == null)
			return def;

		return o;
	}

	public static <T> T getAs(String key, Class<T> type) {
		return Configuration.getAs(key, type, null);
	}

	public static <T> T getAs(String key, Class<T> type, T def) {
		Object o = options.get(key);

		if (!type.isInstance(o))
			return def;

		return type.cast(o);
	}

	public static void set(String key, Object value) {
		options.put(key, value);
	}

	public static void save() {
		File f = new File(Directories.CONFIG + "config.yml");
		FileConfiguration conf = YamlConfiguration.load(f);

		for (String key : options.keySet())
			conf.set("configuration." + key, options.get(key));

		try {
			conf.save(f);
		} catch (IOException e) {
			SolusRpg.log(Level.WARNING, "I could not persist changes to config.yml; please make sure the file exists and is writeable.");

			if (Configuration.is("logging.verbose"))
				e.printStackTrace();
		}
	}
}