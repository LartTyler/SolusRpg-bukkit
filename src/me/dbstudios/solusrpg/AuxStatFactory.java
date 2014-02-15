package me.dbstudios.solusrpg;

import java.lang.reflect.Constructor;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import me.dbstudios.solusrpg.config.Configuration;
import me.dbstudios.solusrpg.config.Directories;
import me.dbstudios.solusrpg.entities.stats.AuxStat;
import me.dbstudios.solusrpg.entities.stats.SimpleAuxStat;
import me.dbstudios.solusrpg.exceptions.CreationException;
import me.dbstudios.solusrpg.util.Util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public final class AuxStatFactory {
	private static Class<? extends AuxStat> statClass;
	private static Map<String, AuxStat> stats = new HashMap<>();;
	private static Map<String, String> dnameLookup = new HashMap<>();
	private static Map<String, String> pnameLookup = new HashMap<>();
	private static boolean initialized = false;

	public static void initialize() {
		if (initialized)
			return;

		long initStart = System.currentTimeMillis();

		String className = Configuration.getAsString("factory.aux-stat", "me.dbstudios.solusrpg.entities.stats.SimpleAuxStat");

		try {
			Class<?> cl = Class.forName(className);

			if (AuxStat.class.isAssignableFrom(cl))
				statClass = cl.asSubclass(AuxStat.class);
		} catch (Exception e) {
			SolusRpg.log(Level.WARNING, String.format("Could not get class %s; please check your configuration and restart the server", className));

			if (Configuration.is("logging.verbose"))
				e.printStackTrace();

			statClass = SimpleAuxStat.class;
		}

		File f = new File(Directories.CONFIG + "config.yml");

		if (!f.exists()) {
			SolusRpg.log(Level.SEVERE, "I could not find required file 'config.yml'; if I did not unpack a copy for you, please report this issue to my Creator.");

			return;
		}

		FileConfiguration conf = YamlConfiguration.loadConfiguration(f);

		for (String name : conf.getStringList("aux-stats.enabled")) {
			String fqn = Util.toQualifiedName(name, "Stat");

			try {
				Constructor<? extends AuxStat> ctor = statClass.getConstructor(String.class);
				AuxStat stat = ctor.newInstance(fqn);

				stats.put(fqn, stat);

				pnameLookup.put(stat.getPathName(), fqn);
				dnameLookup.put(stat.getDisplayName(), fqn);
			} catch (CreationException e) {
				SolusRpg.log(Level.WARNING, String.format("Could not initialize %s; please check the configuration file for errors.", fqn));

				if (Configuration.is("logging.verbose"))
					e.printStackTrace();
			} catch (ReflectiveOperationException | LinkageError e) {
				SolusRpg.log(Level.WARNING, String.format("%s has no constructor capable of accepting a single String as it's argument! THIS IS NOT GOOD!!!", statClass.getName()));

				if (Configuration.is("logging.verbose"))
					e.printStackTrace();
			}
		}

		for (AuxStat stat : AuxStatFactory.getAll())
			stat.validate();

		initialized = true;

		SolusRpg.log(String.format("%d auxiliary stat%s initialized in %d milliseconds.", stats.size(), stats.size() != 1 ? "s" : "", System.currentTimeMillis() - initStart));
	}

	public static Set<String> getQualifiedNames() {
		return stats.keySet();
	}

	public static Set<String> getDisplayNames() {
		return dnameLookup.keySet();
	}

	public static Set<String> getPathNames() {
		return pnameLookup.keySet();
	}

	public static AuxStat getByFQN(String fqn) {
		return stats.get(fqn);
	}

	public static AuxStat getByDisplayName(String name) {
		if (!dnameLookup.containsKey(name))
			return null;

		return AuxStatFactory.getByFQN(dnameLookup.get(name));
	}

	public static AuxStat getByPathName(String path) {
		if (!pnameLookup.containsKey(path))
			return null;

		return AuxStatFactory.getByFQN(pnameLookup.get(path));
	}

	public static List<AuxStat> find(String name) {
		List<String> search = Util.getFuzzyMatch(name, AuxStatFactory.getDisplayNames());
		List<AuxStat> matches = new ArrayList<>();

		for (String s : search)
			matches.add(AuxStatFactory.getByDisplayName(s));

		return matches;
	}

	public static Collection<AuxStat> getAll() {
		return stats.values();
	}
}