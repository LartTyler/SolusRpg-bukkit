package me.dbstudios.solusrpg.chat;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import me.dbstudios.solusrpg.config.Configuration;
import me.dbstudios.solusrpg.config.Directories;
import me.dbstudios.solusrpg.entities.player.RpgPlayer;
import me.dbstudios.solusrpg.exceptions.CreationException;
import me.dbstudios.solusrpg.language.Phrase;
import me.dbstudios.solusrpg.language.SimplePhrase;

import org.bukkit.ChatColor;
import org.bukkit.configuration.FileConfiguration;
import org.bukkit.configuration.YamlConfiguration;

public class ChatChannel implements Channel {
	private final Metadata<String> metadata = new Metadata<>();
	private final Set<RpgPlayer> members = new HashSet<>()
	private final DistortionManager distorter;
	private final qualifiedName;

	private ChannelLogger logger = null;

	public ChatChannel(String fqn) {
		File f = new File(Directories.CONFIG_CHANNELS + fqn + ".yml");

		if (!f.exists())
			throw new CreationException(String.format("No configuration file found for channel with qualified name '%s'.", fqn));

		this.qualifiedName = fqn;

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

		if (!metadata.hasOfType("formatting.message"))
			metadata.set("formatting.message", "[{symbol}] {sender}: {message}");

		Phrase format = SimplePhrase.newInstance(metadata.getAsString("formatting.message"))
			.setParameter("symbol", this.getSymbol())
			.setParameter("channel", this.getDisplayName())
			.setParameter("primary-color", Configuration.getAsChatColor("color.primary", ChatColor.WHITE).toString())
			.setParameter("secondary-color", Configuration.getAsChatColor("color.secondary", ChatColor.WHITE).toString());

		metadata.set("formatting.message", format.asText());

		if (conf.getBoolean("distortion.enabled", true) && metadata.getAsInteger("max-range", 0) > 0 && conf.isString("distortion.algorithm"))
			this.distorter = new DistortionManager(
				metadata.getAsInteger("max-range", 0),
				conf.getDouble("distortion.threshold.distance", 0.0),
				conf.getDouble("distortion.threshold.chance", 0.0),
				conf.getString("distortion.algorithm")
			);
		else
			this.distorter = null;
	}

	public String getName() {
		return this.qualifiedName;
	}

	public String getDisplayName() {
		return metadata.getAsString("display-name", this.getName());
	}

	public String getSymbol() {
		return metadata.getAsString("symbol", Character.toString(this.getName().charAt(0)));
	}

	public String getFormat() {
		return metadata.getAsString("formatting.message");
	}

	public int getMemberCount() {
		return members.size();
	}

	public Set<RpgPlayer> getMembers() {
		return Collections.unmodifiableSet(this.members);
	}

	public ChatChannel addMember(RpgPlayer player) {
		if (!members.contains(player))
			members.add(player);

		if (this.logger != null)
			logger.logChannelJoin(player).flush();

		return this;
	}

	public ChatChannel removeMember(RpgPlayer player) {
		members.remove(player);

		if (this.logger != null)
			logger.logChannelLeave(player).flush();

		return this;
	}

	public boolean hasMember(RpgPlayer player) {
		return members.contains(player);
	}

	public ChatChannel sendMessage(RpgPlayer sender, String message) {
		Phrase msg = SimplePhrase.newInstance(this.getFormat())
			.setParameter("sender", sender.getDisplayName())
			.setParameter("sender-name", sender.getName())
			.setParameter("sender-class", sender.getRpgClass().getDisplayName());

		if (this.logger != null)
			logger.logMessageSent(msg.setParameter("message", message).asText());

		for (RpgPlayer member : this.members) {
			if (this.distorter != null)
				msg.setParameter("message", distorter.distort(message, sender, member));
			else
				msg.setParameter("message", message);

			String toSend = msg.asText();

			if (this.logger != null)
				logger.logMessageRecieved(toSend);

			member.sendMessage(toSend);
		}

		if (this.logger != null)
			logger.flush();

		return this;
	}

	public ChatChannel attachLogger(ChannelLogger logger) {
		this.logger = logger;

		return this;
	}
}