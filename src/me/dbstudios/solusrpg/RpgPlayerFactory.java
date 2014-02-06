package me.dbstudios.solusrpg;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import me.dbstudios.solusrpg.config.Configuration;
import me.dbstudios.solusrpg.config.Directories;
import me.dbstudios.solusrpg.entities.player.RpgPlayer;
import me.dbstudios.solusrpg.entities.player.SimpleRpgPlayer;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public final class RpgPlayerFactory {
	private static final Map<UUID, RpgPlayer> players = new HashMap<>();

	private static final Class<? extends RpgPlayer> playerClass;

	static {
		long initStart = System.currentTimeMillis();
		String name = Configuration.getAs("factory.player", "me.dbstudios.solusrpg.entities.player.SimpleRpgPlayer");

		try {
			Class<?> cl = Class.forName(name);

			if (RpgPlayer.class.isAssignableFrom(cl))
				playerClass = cl.asSubclass(RpgPlayer.class);
		} catch (Exception e) {
			SolusRpg.log(Level.SEVERE, String.format("Could not get class '%s'; please check your configuration and restart the server.", name));

			playerClass = SimpleRpgPlayer.class;

			if (Configuration.is("logging.verbose"))
				e.printStackTrace();
		}

		if (Configuration.is("logging.verbose"))
			SolusRpg.log(Level.INFO, String.format("Using %s as player class.", playerClass.getName()));

		SolusRpg.log(Level.INFO, String.format("RpgPlayerFactory initialized in %d milliseconds.", System.currentTimeMillis() - initStart));
	}

	public static RpgPlayer getPlayer(Player player) {
		if (players.containsKey(uid))
			return players.get(uid);

		RpgPlayer p = null;

		try {
			Constructor<? extends RpgPlayer> ctor = playerClass.getConstructor(Player.class);

			p = ctor.newInstance(player);
		} catch (NoSuchMethodException e) {
			SolusRpg.log(Level.SEVERE, String.format("Factory target class %s has no constructor capable of accepting %s as an argument.", playerClass.getName(), player.getClass().getName()));

			if (Configuration.is("logging.verbose"))
				e.printStackTrace();
		}

		if (p != null)
			players.put(player.getUniqueId(), p);

		return p;
	}

	public static boolean playerExists(Player player) {
		return RpgPlayerFactory.playerExists(player.getUniqueId());
	}

	public static boolean playerExists(UUID uid) {
		return players.containsKey(uid);
	}

	public static void removePlayer(RpgPlayer player) {
		RpgPlayerFactory.removePlayer(player.getBasePlayer());
	}

	public static void removePlayer(Player player) {
		RpgPlayerFactory.removePlayer(player.getUniqueId());
	}

	public static void removePlayer(UUID uid) {
		if (!RpgPlayerFactory.playerExists(uid)) {
			SolusRpg.log(Level.WARNING, "RpgPlayerFactory.removePlayer(UUID) called on non-player UUID; this may indicate an internal optimization issue.");

			return;
		}

		players.remove(uid)
			.save()
			.close();
	}
}