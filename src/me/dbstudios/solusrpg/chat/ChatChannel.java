package me.dbstudios.solusrpg.chat;

import java.io.File;

import me.dbstudios.solusrpg.config.Directories;
import me.dbstudios.solusrpg.exceptions.CreationException;

import org.bukkit.configuration.FileConfiguration;
import org.bukkit.configuration.YamlConfiguration;

public class ChatChannel implements Channel {
	private final Metadata<String> metadata = new Metadata<>();

	public ChatChannel(String fqn) {
		File f = new File(Directories.CONFIG_CHANNELS + fqn + ".yml");

		if (!f.exists())
			throw new CreationException(String.format("No configuration file found for channel with qualified name '%s'.", fqn));

		FileConfiguration conf = YamlConfiguration.loadConfiguration(f);

		if (conf.isString("extends")) {
			File file = new File(Directories.CONFIG_CHANNELS + Util.toQualifiedName(conf.getString("extends"), "Class") + ".yml");

			if (file.exists()) {
				FileConfiguration config = YamlConfiguration.loadConfiguration(file);

				for (String key : config.getKeys(true))
					if (!key.equals("extends") && !conf.isSet(key))
						conf.set(key, config.get(key));
			}
		}

		if (conf.isConfigurationSection("metadata"))
			for (String key : conf.getConfigurationSection("metadata").getKeys(true))
				metadata.set(key, conf.get("metadata." + key));
	}
}