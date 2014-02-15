package me.dbstudios.solusrpg;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.lang.reflect.Constructor;
import java.util.logging.Level;

import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.chat.*;
import me.dbstudios.solusrpg.config.Configuration;
import me.dbstudios.solusrpg.config.Directories;
import me.dbstudios.solusrpg.events.ChatListener;
import me.dbstudios.solusrpg.util.Util;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public final class RpgChannelFactory {
	private static final Map<String, Channel> channels = new HashMap<>();
	private static final Map<String, String> dnameLookup = new HashMap<>();
	private static final Map<String, String> symbolLookup = new HashMap<>();

	private static Class<? extends ChannelLogger> loggerClass;

	public static void initialize() {
		long initStart = System.currentTimeMillis();
		String loggerName = Configuration.getAsString("factory.channel-logger", "me.dbstudios.solusrpg.chat.ChatChannelLogger");

		try {
			Class<?> cl = Class.forName(loggerName);

			if (ChannelLogger.class.isAssignableFrom(cl))
				loggerClass = cl.asSubclass(ChannelLogger.class);
		} catch (Exception e) {
			SolusRpg.log(Level.SEVERE, String.format("Could not get class '%s'; please check your configuration and restart the server.", loggerName));

			loggerClass = null;

			if (Configuration.is("logging.verbose"))
				e.printStackTrace();
		}

		File f = new File(Directories.CONFIG + "config.yml");

		if (!f.exists())
			throw new RuntimeException("Could not locate config.yml! This file is needed for SolusRpg to operate normally.");

		FileConfiguration conf = YamlConfiguration.loadConfiguration(f);

		if (conf.getBoolean("chat.enabled", true)) {
			for (String name : conf.getStringList("chat.channels")) {
				String fqn = Util.toQualifiedName(name, "Channel");
				File file = new File(Directories.CONFIG_CHANNELS + fqn + ".yml");

				if (!file.exists()) {
					SolusRpg.log(Level.WARNING, String.format("Could not find a configuration for %s using fully-qualified name %s.", name, fqn));

					continue;
				}

				FileConfiguration config = YamlConfiguration.loadConfiguration(file);
				String channelClassName = config.getString("factory-class", "me.dbstudios.solusrpg.chat.ChatChannel");
				Class<? extends Channel> channelClass = null;

				try {
					Class<?> cl = Class.forName(channelClassName);

					if (Channel.class.isAssignableFrom(cl))
						channelClass = cl.asSubclass(Channel.class);
				} catch (Exception e) {
					SolusRpg.log(Level.WARNING, String.format("Could not get class '%s'; please check your configuration and restart the server.", channelClassName));

					if (Configuration.is("logging.verbose"))
						e.printStackTrace();
				}

				if (channelClass == null || !Channel.class.isAssignableFrom(channelClass))
					continue;

				Channel channel = null;

				try {
					Constructor<? extends Channel> ctor = channelClass.getConstructor(String.class);

					channel = ctor.newInstance(fqn);
				} catch (Exception e) {
					SolusRpg.log(Level.SEVERE, String.format("%s has no constructor capable of accepting a String as it's argument! THIS IS VERY, VERY BAD!!!", channelClass.getName()));

					if (Configuration.is("logging.verbose"))
						e.printStackTrace();
				}

				if (loggerClass != null)
					try {
						Constructor<? extends ChannelLogger> ctor = loggerClass.getConstructor(Channel.class);

						channel.attachLogger(ctor.newInstance(channel));
					} catch (Exception e) {
						SolusRpg.log(Level.SEVERE, String.format("Could not create new ChannelLogger from %s! Check your configuration and restart the server."));

						if (Configuration.is("logging.verbose"))
							e.printStackTrace();
					}

				if (channel != null) {
					channels.put(fqn, channel);
					dnameLookup.put(channel.getDisplayName(), fqn);
					symbolLookup.put(channel.getSymbol(), fqn);
				}
			}

			if (channels.size() > 0)
				Bukkit.getPluginManager().registerEvents(new ChatListener(), SolusRpg.getInstance());
		}
	}

	public static Channel getByFQN(String fqn) {
		if (fqn != null)
			return channels.get(fqn);

		return null;
	}

	public static Channel getByDisplayName(String dname) {
		return RpgChannelFactory.getByFQN(dnameLookup.get(dname));
	}

	public static Channel getBySymbol(String symbol) {
		return RpgChannelFactory.getByFQN(symbolLookup.get(symbol));
	}
}