package me.dbstudios.solusrpg;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.dbstudios.solusrpg.entities.stats.AuxStat;
import me.dbstudios.solusrpg.events.EventDistributor;
import me.dbstudios.solusrpg.events.RpgStockListener;
import me.dbstudios.solusrpg.language.LanguageManager;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SolusRpg extends JavaPlugin {
	private static final Logger logger = Logger.getLogger("Minecraft");
	private static final Class<?>[] init = new Class<?>[] {
		Configuration.class,
		LanguageManager.class,
		AuxStatFactory.class,
		RpgClassFactory.class,
		RpgPlayerFactory.class
	};

	private static SolusRpg instance = null;

	public void onEnable() {
		long loadStart = System.currentTimeMillis();

		instance = this;

		Bukkit.getPluginManager().registerEvents(new EventDistributor(), this);
		Bukkit.getPluginManager().registerEvents(new RpgStockListener(), this);

		for (Class<?> cl : init)
			try {
				cl.getMethod("initialize", null).invoke(null, null);
			} catch (Exception e) {
				SolusRpg.log(Level.SEVERE, String.format("Could not initialize required class %s; SolusRpg will be disabled until server restart", cl.getSimpleName()));

				e.printStackTrace();

				Bukkit.getPluginManager().disablePlugin(this);
			}

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