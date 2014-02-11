package me.dbstudios.solusrpg;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import me.dbstudios.solusrpg.config.Configuration;
import me.dbstudios.solusrpg.config.Directories;
import me.dbstudios.solusrpg.entities.RpgClass;
import me.dbstudios.solusrpg.entities.SimpleRpgClass;
import me.dbstudios.solusrpg.exceptions.CreationException;
import me.dbstudios.solusrpg.util.Util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public final class RpgClassFactory {
	private static final Map<String, RpgClass> classes = new HashMap<>();
	private static final Map<String, String> dnameLookup = new HashMap<>();
	private static final Map<String, Set<RpgClass>> findCache = new HashMap<>();

	private static Class<? extends RpgClass> rpgClass;
	private static boolean initialized = false;

	static {
		RpgClassFactory.initialize();
	}

	public static void initialize() {
		if (initialized)
			return;

		long initStart = System.currentTimeMillis();
		String name = Configuration.getAs("factory.rpg-class", String.class, "me.dbstudios.solusrpg.entities.SimpleRpgClass");

		try {
			Class<?> cl = Class.forName(name);

			if (RpgClass.class.isAssignableFrom(cl))
				rpgClass = cl.asSubclass(RpgClass.class);
		} catch (Exception e) {
			SolusRpg.log(Level.SEVERE, String.format("Could not get class '%s'; please check your configuration and restart the server.", name));

			rpgClass = SimpleRpgClass.class;

			if (Configuration.is("logging.verbose"))
				e.printStackTrace();
		}

		if (Configuration.is("logging.verbose"))
			SolusRpg.log(Level.INFO, String.format("Using %s as RpgClass class (I hate the way that comes out...).", rpgClass.getName()));

		File f = new File(Directories.CONFIG + "config.yml");

		if (!f.exists())
			throw new RuntimeException("Could not locate config.yml! This file is needed for SolusRpg to operate normally.");

		FileConfiguration conf = YamlConfiguration.loadConfiguration(f);

		for (String className : conf.getStringList("classes.enabled")) {
			String fqn = Util.toQualifiedName(className, "Class");
			File classFile = new File(Directories.CONFIG_CLASSES + fqn + ".yml");

			if (!classFile.exists()) {
				SolusRpg.log(Level.WARNING, String.format("I could not find a configuration for %s using fully-qualified name '%s'.", className, fqn));

				continue;
			}

			RpgClass cl = null;

			try {
				Constructor<? extends RpgClass> ctor = rpgClass.getConstructor(String.class);

				cl = ctor.newInstance(fqn);
			} catch (CreationException e) {
				SolusRpg.log(Level.WARNING, String.format("I encountered an exception while initializing %s; please check the configuration file for errors.", fqn));

				if (Configuration.is("logging.verbose"))
					e.printStackTrace();
			} catch (Exception e) {
				SolusRpg.log(Level.SEVERE, String.format("%s has no constructor capable of accepting a String as it's argument! THIS IS VERY, VERY BAD!!!", rpgClass.getName()));

				if (Configuration.is("logging.verbose"))
					e.printStackTrace();
			}

			if (cl != null) {
				classes.put(fqn, cl);
				dnameLookup.put(cl.getDisplayName(), fqn);
			}
		}

		initialized = true;

		SolusRpg.log(Level.INFO, String.format("RpgClassFactory initialized in %d milliseconds with %d class%s.", System.currentTimeMillis() - initStart, classes.size(), classes.size() != 1 ? "s" : ""));
	}

	public static Set<String> getQualifiedNames() {
		return classes.keySet();
	}

	public static Set<String> getDisplayNames() {
		return dnameLookup.keySet();
	}

	public static RpgClass getByFQN(String fqn) {
		return classes.get(fqn);
	}

	public static RpgClass getByDisplayName(String name) {
		return RpgClassFactory.getByFQN(dnameLookup.get(name));
	}

	public static Collection<RpgClass> getAll() {
		return classes.values();
	}

	public static Set<RpgClass> find(String pattern) {
		if (findCache.containsKey(pattern))
			return findCache.get(pattern);

		Set<RpgClass> set = new HashSet<>();

		for (RpgClass cl : classes.values())
			if (Util.fuzzyMatches(pattern, cl.getDisplayName()))
				set.add(cl);

		findCache.put(pattern, set);

		return set;
	}
}