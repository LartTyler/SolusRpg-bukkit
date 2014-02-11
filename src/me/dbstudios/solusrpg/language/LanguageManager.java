package me.dbstudios.solusrpg.language;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.config.Directories;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public final class LanguageManager {
	private static Map<String, Phrase> language = new HashMap<>();

	static {
		LanguageManager.initialize();
	}

	public static void initialize() {
		if (!language.isEmpty())
			return;

		long initStart = System.currentTimeMillis();

		File f = new File(Directories.CONFIG + "language.yml");

		if (!f.exists())
			return;

		FileConfiguration conf = YamlConfiguration.loadConfiguration(f);

		for (String key : conf.getKeys(true))
			language.put(key, new SimplePhrase(conf.getString(key)));

		SolusRpg.log(String.format("Language manager initialized in %d milliseconds.", System.currentTimeMillis() - initStart));
	}

	public static Phrase getPhrase(String phrase) {
		return language.get(phrase);
	}

	public static void setPhrase(String key, Phrase phrase) {
		language.put(key, phrase);
	}

	public static void removePhrase(String phrase) {
		if (language.containsKey(phrase))
			language.remove(phrase);
	}

	public static boolean hasPhrase(String phrase) {
		return language.containsKey(phrase);
	}
}