package me.dbstudios.solusrpg.language;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.config.Directories;
import me.dbstudios.solusrpg.util.Initializable;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public final class LanguageManager extends Initializable {
	private static LanguageManager instance = null;

	private Map<String, Phrase> language = new HashMap<>();

	static {
		LanguageManager.initialize();
	}

	public static void initialize() {
		if (initialized) {
			SolusRpg.log(Level.WARNING, "Additional calls made to LanguageManager.initialize(); this may indicate an internal optimization issue, and should be reported to my Creator.");

			return;
		}

		long initStart = System.currentTimeMillis();

		instance = new LanguageManager();
		initialized = true;

		SolusRpg.log(String.format("Language manager initialized in %d milliseconds.", System.currentTimeMillis() - initStart));
	}

	private LanguageManager() {
		this(Directories.CONFIG + "language.yml");
	}

	private LanguageManager(String configPath) {
		File f = new File(configPath);

		if (!f.exists())
			return;

		FileConfiguration conf = YamlConfiguration.load(f);

		for (String key : conf.getKeys(true))
			language.put(key, new SimplePhrase(conf.getString(key)));
	}

	public static LanguageManager getInstance() {
		return instance;
	}

	public Phrase getPhrase(String key) {
		if (language.containsKey(key))
			return language.get(key);

		return null;
	}

	public LanguageManager setPhrase(String key, Phrase phrase) {
		if (phrase == null)
			return this.removePhrase(key);

		language.put(key, phrase);

		return this;
	}

	public LanguageManager removePhrase(String key) {
		if (this.hasPhrase(key))
			language.remove(key);

		return this;
	}

	public boolean hasPhrase(String key) {
		return language.containsKey(key);
	}

	public static Phrase get(String phrase) {
		return LanguageManager.getInstance().getPhrase(phrase);
	}

	public static void set(String key, Phrase phrase) {
		LanguageManager.getInstance().setPhrase(key, phrase);
	}

	public static void remove(String phrase) {
		LanguageManager.getInstance().removePhrase(phrase);
	}

	public static boolean has(String phrase) {
		return LanguageManager.getInstance().hasPhrase(phrase);
	}
}