package me.dbstudios.solusrpg;

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

	private static Class<? extends ChannelLogger> loggerClass;

	static {
		// long initStart = System.currentTimeMillis();
		// String loggerName = Configuration.getAsString("factory.channel-logger", "me.dbstudios.solusrpg.chat.ChatChannelLogger");

		// try {
		// 	Class<?> cl = Class.forName(loggerName);

		// 	if (ChannelLogger.class.isAssignableFrom(cl))
		// 		loggerClass = cl.asSubclass(ChannelLogger.class);
		// } catch (Exception e) {
		// 	SolusRpg.log(Level.SEVERE, String.format("Could not get class '%s'; please check your configuration and restart the server.", loggerName));

		// 	loggerClass = ChatChannelLogger.class;

		// 	if (Configuration.is("logging.verbose"))
		// 		e.printStackTrace();
		// }

		// File f = new File(Directories.CONFIG + "config.yml");

		// if (!f.exists())
		// 	throw new RuntimeException("Could not locate config.yml! This file is needed for SolusRpg to operate normally.");

		// FileConfiguration conf = YamlConfiguration.loadConfiguration(conf);

		// if (conf.getBoolean("chat.enabled", true)) {
		// 	for (String name : conf.getStringList("chat.channels")) {
		// 		String fqn = Util.toQualifiedName(name);
		// 		File file = new File(Directories.CONFIG_CHANNELS + fqn + ".yml");

		// 		if (!file.exists()) {
		// 			SolusRpg.log(Level.WARNING, String.format("Could not find a configuration for %s using fully-qualified name %s.", name, fqn));

		// 			continue;
		// 		}

		// 		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		// 		String channelClass = config.getString("factory-class", "me.dbstudios.solusrpg.chat.ChatChannel");
		// 		Class<?> cl = null;

		// 		try {
		// 			cl = Class.forName(channelClass);
		// 		} catch (Exception e) {
		// 			SolusRpg.log(Level.WARNING, String.format("Could not get class '%s'; please check your configuration and restart the server.", channelClass));

		// 			if (Configuration.is("logging.verbose"))
		// 				e.printStackTrace();
		// 		}

		// 		if (cl == null || !Channel.class.isAssignableFrom(cl))
		// 			continue;

		// 		Channel channel = null;

		// 		try {
		// 			Constructor<? extends Channel> ctor = cl.getConstructor(String.class);

		// 			channel = ctor.newInstance(fqn);
		// 		} catch (Exception e) {
		// 			SolusRpg.log(Level.SEVERE, String.format("%s has no constructor capable of accepting a String as it's argument! THIS IS VERY, VERY BAD!!!", cl.getName()));

		// 			if (Configuration.is("logging.verbose"))
		// 				e.printStackTrace();
		// 		}

		// 		if (channel != null)
		// 			channels.put(fqn, channel);
		// 	}

		// 	if (channels.size() > 0)
		// 		Bukkit.getPluginManager().registerEvents(new ChatListener(), SolusRpg.getInstance());
		// }
	}
}