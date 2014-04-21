package me.dbstudios.solusrpg;

import java.io.File;
import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Level;

import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.config.Configuration;
import me.dbstudios.solusrpg.config.Directories;
import me.dbstudios.solusrpg.gui.RegisteredPopup;
import me.dbstudios.solusrpg.gui.RpgPopup;
import me.dbstudios.solusrpg.gui.RpgPopupType;
import me.dbstudios.solusrpg.util.siml.Document;
import me.dbstudios.solusrpg.util.siml.impl.SimlDocument;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class RpgPopupFactory {
	private static final Map<RpgPopupType, RegisteredPopup> popups = new EnumMap<>(RpgPopupType.class);

	private static boolean initialized = false;

	public static void initialize() {
		long start = System.currentTimeMillis();

		if (initialized)
			return;

		for (RpgPopupType type : RpgPopupType.values())
			try {
				Document doc = SimlDocument.create(new File(Directories.CONFIG_UI + type.getQualifiedName() + ".siml"));

				if (doc.isValid())
					popups.put(type, new RegisteredPopup(type.getPopupClass(), doc));
			} catch (Exception e) {
				SolusRpg.log(Level.WARNING, String.format("Could not create RegisteredPopup for %s: %s", type.getQualifiedName(), e.getClass().getSimpleName()));

				if (Configuration.is("logging.verbose"))
					e.printStackTrace();
			}

		SolusRpg.log(Level.INFO, String.format("RpgPopupFactory loading in %d milliseconds with %d SIML documents pre-parsed.", System.currentTimeMillis() - start, popups.size()));

		initialized = true;
	}

	public static RpgPopup getPopup(String name) {
		return RpgPopupFactory.getPopup(RpgPopupType.valueOf(name));
	}

	public static RpgPopup getPopup(RpgPopupType type) {
		return popups.get(type).createPopup();
	}
}