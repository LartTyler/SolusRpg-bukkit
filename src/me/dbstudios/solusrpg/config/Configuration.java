package me.dbstudios.solusrpg.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.util.Initializable;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public final class Configuration extends Initializable {
	private static final Metadata<String> metadata = new Metadata<>();

	static {
		Configuration.initialize();
	}

	public static void initialize() {
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

		FileConfiguration conf = YamlConfiguration.loadConfiguration(f);

		if (!conf.isConfigurationSection("configuration"))
			return;

		for (String key : conf.getConfigurationSection("configuration").getKeys(true))
			metadata.set(key, conf.get("configuration." + key));

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
		return metadata.getAsType(key, Object.class, def);
	}

	public static <T> T getAs(String key, Class<T> type) {
		return Configuration.getAs(key, type, null);
	}

	public static <T> T getAs(String key, Class<T> type, T def) {
		return metadata.getAsType(key, type, def);
	}

	public static String getAsString(String key) {
		return Configuration.getAs(key, String.class);
	}

	public static String getAsString(String key, String def) {
		return Configuration.getAs(key, String.class, def);
	}

	public static Integer getAsInt(String key) {
		return Configuration.getAs(key, Integer.class);
	}

	public static Integer getAsInt(String key, Integer def) {
		return Configuration.getAs(key, Integer.class, def);
	}

	public static Double getAsDouble(String key) {
		return Configuration.getAs(key, Double.class);
	}

	public static Double getAsDouble(String key, Double def) {
		return Configuration.getAs(key, Double.class, def);
	}

	public static ChatColor getAsChatColor(String key) {
		return Configuration.getAsChatColor(key, ChatColor.WHITE);
	}

	public static ChatColor getAsChatColor(String key, ChatColor def) {
		if (!metadata.has(key))
			return def;

		if (metadata.hasOfType(key, ChatColor.class))
			return Configuration.getAs(key, ChatColor.class);

		String name = metadata.getAsType(key, String.class);

		try {
			ChatColor color = ChatColor.valueOf(name);

			Configuration.set(key, color);

			return color;
		} catch (Exception e) {
			SolusRpg.log(Level.WARNING, String.format("ChatColor expected at %s; got %s.", key, name));

			if (Configuration.is("logging.enabled"))
				e.printStackTrace();
		}

		return def;
	}

	public static void set(String key, Object value) {
		metadata.set(key, value);
	}

	public static void save() {
		File f = new File(Directories.CONFIG + "config.yml");
		FileConfiguration conf = YamlConfiguration.loadConfiguration(f);

		for (String key : metadata.keySet())
			conf.set("configuration." + key, metadata.get(key));

		try {
			conf.save(f);
		} catch (IOException e) {
			SolusRpg.log(Level.WARNING, "I could not persist changes to config.yml; please make sure the file exists and is writeable.");

			if (Configuration.is("logging.verbose"))
				e.printStackTrace();
		}
	}
}