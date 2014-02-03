package me.dbstudios.solusrpg;

import java.util.logging.Level;
import java.util.logging.Logger;

import me.dbstudios.solusrpg.events.EventDistributor;
import me.dbstudios.solusrpg.events.RpgStockListener;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SolusRpg extends JavaPlugin {
	private static final Logger logger = Logger.getLogger("Minecraft");
	private static SolusRpg instance = null;

	public void onEnable() {
		long loadStart = System.currentTimeMillis();

		instance = this;

		Bukkit.getPluginManager().registerEvents(new EventDistributor(), this);
		Bukkit.getPluginManager().registerEvents(new RpgStockListener(), this);

		SolusRpg.log("Successfully loaded in {0} milliseconds.", System.currentTimeMillis() - loadStart);
	}

	public void onDisable() {

	}

	public static void log(String message) {
		SolusRpg.log(Level.INFO, message);
	}

	public static void log(String message, Object... args) {
		SolusRpg.log(Level.INFO, message, args);
	}

	public static void log(Level level, String message) {
		logger.log(level, "[SolusRpg] {0}", message);
	}

	public static void log(Level level, String message, Object... args) {
		for (int i = 0; i < args.length; i++)
			message = message.replace("{" + i + "}", args[i] + "");

		SolusRpg.log(level, message);
	}

	public static SolusRpg getInstance() {
		return instance;
	}
}