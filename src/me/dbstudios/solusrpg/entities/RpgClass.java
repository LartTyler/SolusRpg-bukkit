package me.dbstudios.solusrpg.entities;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.config.Configuration;
import me.dbstudios.solusrpg.config.Metadata;
import me.dbstudios.solusrpg.entities.stats.AuxStat;
import me.dbstudios.solusrpg.entities.stats.CoreStat;
import me.dbstudios.solusrpg.entities.stats.StatType;
import me.dbstudios.solusrpg.exceptions.CreationException;
import me.dbstudios.solusrpg.util.Initializable;
import me.dbstudios.solusrpg.util.Util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class RpgClass extends Initializable {
	private static Map<String, RpgClass> classes = new HashMap<>();
	private static Map<String, String> displayNameLookup = new HashMap<>();

	private final String fullyQualifiedName;

	private Map<StatType, Integer> coreStats = new EnumMap<>(StatType.class);
	private Map<String, Integer> stats = new HashMap<>();
	private Metadata<String> metadata = new Metadata();

	static {
		RpgClass.initialize();
	}

	private static void initialize() {
		if (initialized) {
			SolusRpg.log(Level.WARNING, "Additional calls made to RpgClass.initialize(); this may indicate an internal optimization issue, and should be reported to my Creator.");

			return;
		}

		long initStart = System.currentTimeMillis();

		File f = new File(Directories.CONFIG + "config.yml");

		if (!f.exists()) {
			SolusRpg.log(Level.SEVERE, "I could not find required file 'config.yml'; if I did not unpack a copy for you, please report this issue to my Creator.");

			return;
		}

		FileConfiguration conf = YamlConfiguration.load(f);

		List<String> pubClasses = conf.getStringList("classes.enabled");
		List<String> privClasses = conf.getStringList("classes.restricted");
		List<String> allClasses = new ArrayList<>();

		allClasses.addAll(pubClasses);
		allClasses.addAll(privClasses);

		for (String name : allClasses) {
			String qualifiedName = Util.toQualifiedName(name, "Class");
			File classFile = new File(Directories.CONFIG_CLASSES + qualifiedName + ".yml");

			if (!classFile.exists()) {
				SolusRpg.log(Level.WARNING, String.format("I could not find a configuration for %s using fully-qualified name '%s'.", name, qualifiedName));

				continue;
			}

			RpgClass cl = null;

			try {
				cl = new RpgClass(qualifiedName, privClasses.contains(name));
			} catch (CreationException e) {
				SolusRpg.log(Level.WARNING, String.format("I encountered an exception while initializing %s; please check the configuration file for errors.", qualifiedName));

				if (Configuration.is("logging.verbose"))
					e.printStackTrace();
			}

			if (cl != null) {
				classes.put(qualifiedName, cl);
				displayNameLookup.put(cl.getDisplayName(), qualifiedName);
			}
		}

		initialized = true;

		SolusRpg.log(String.format("Classes initialized in %d milliseconds.", System.currentTimeMillis() - initStart));
	}

	private RpgClass(String fqn, boolean restricted) {
		File f = new File(Directories.CONFIG_CLASSES + fqn + ".yml");

		if (!f.exists())
			throw new CreationException(String.format("No configuraiton file found for class with FQN '%s'.", fqn));

		this.fullyQualifiedName = fqn;

		FileConfiguration conf = YamlConfiguration.load(f);

		// If the class being loaded extends another, grab that class configuration and overwrite paths NOT
		// present in the child configuration.
		if (conf.getString("extends", null) != null) {
			File baseFile = new File(Directories.CONFIG_CLASSES + Util.toQualifiedName(conf.getString("extends"), "Class") + ".yml");

			if (baseFile.exists()) {
				FileConfiguration base = YamlConfiguration.load(baseFile);

				for (String key : base.getKeys(true))
					if (!key.equals("extends") && !conf.isSet(key))
						conf.set(key, base.get(key));
			}
		}

		// Only load values under the "metdata" path into our Metdata storage... Everything else will be managed by another system.
		if (conf.isConfigurationSection("metadata"))
			for (String key : conf.getConfigurationSection("metadata").getKeys(true))
				metadata.set(key, conf.get("metadata." + key));

		for (StatType type : StatType.values())
			coreStats.put(conf.getInt("vitals.core-stats." + type.name().toLowerCase(), 1));

		for (AuxStat stat : AuxStat.getAllAuxStats())
			stats.put(stat.getName(), conf.getInt("vitals.aux-stats." + stat.getPathName().toLwoerCase(), 1));
	}

	/**
	 * Gets a set of all known qualified RpgClass names.
	 *
	 * @return a set of all known qualified names
	 */
	public static Set<String> getQualifiedNames() {
		return classes.keySet();
	}

	/**
	 * Gets a set of all known RpgClass display names.
	 *
	 * @return a set of all known display names
	 */
	public static Set<String> getDisplayNames() {
		return displayNameLookup.keySet();
	}

	/**
	 * Gets an RpgClass by it's exact fully-qualified name (in the format [ClassName]Class, i.e. "LightMageClass")
	 * @param  fqn the fully-qualified name of the RpgClass to get
	 * @return      an RpgClass instance, or null if one does not exist with the given FQN
	 */
	public static RpgClass getByFQN(String fqn) {
		return classes.get(fqn);
	}

	/**
	 * Gets an RpgClass by it's exact display name (i.e. "Light Mage")
	 *
	 * @param  displayName the display name of the RpgClass to get
	 * @return             an RpgClass instance, or null if one does not exist
	 */
	public static RpgClass getByDisplayName(String displayName) {
		if (!displayNameLookup.containsKey(displayName))
			return null;

		return RpgClass.getByFQN(displayNameLookup.get(displayName));
	}

	/**
	 * Attempts to find an RpgClass using the given string. Matches are found using {@link Util#getFuzzyMatch(String, List<String>)}.
	 *
	 * @param  name the string to use as the "pattern" for the fuzzy match
	 * @return      a list of RpgClass instances that matched the search string
	 */
	public static List<RpgClass> find(String name) {
		List<String> search = Util.getFuzzyMatch(name, RpgClass.getDisplayNames());
		List<RpgClass> matches = new ArrayList<>();

		for (String s : search)
			matches.add(RpgClass.getByDisplayName(s));

		return matches;
	}
}