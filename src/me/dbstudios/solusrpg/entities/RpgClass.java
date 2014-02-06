package me.dbstudios.solusrpg.entities;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.config.Configuration;
import me.dbstudios.solusrpg.config.Directories;
import me.dbstudios.solusrpg.entities.stats.AuxStat;
import me.dbstudios.solusrpg.entities.stats.StatType;
import me.dbstudios.solusrpg.events.player.RpgActionType;
import me.dbstudios.solusrpg.exceptions.CreationException;
import me.dbstudios.solusrpg.util.Initializable;
import me.dbstudios.solusrpg.util.Util;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public abstract class RpgClass extends Initializable {
	private static Map<String, RpgClass> classes = new HashMap<>();
	private static Map<String, String> displayNameLookup = new HashMap<>();

	public abstract String getName();
	public abstract String getDisplayName();
	public abstract String getDescription();
	public abstract int getStatLevel(AuxStat stat);
	public abstract int getStatLevel(String fqn);
	public abstract int getStatLevel(StatType type);
	public abstract boolean isAllowed(RpgActionType action, Material material);
	public abstract int getBaseHealth();
	public abstract String getHealthName();
	public abstract boolean hasBaseHealthRegen();
	public abstract double getBaseHealthRegenRate();
	public abstract String getBaseHealthRegenFormula();
	public abstract int getBaseEnergy();
	public abstract String getEnergyName();
	public abstract boolean hasBaseEnergyRegen();
	public abstract double getBaseEnergyRegenRate();
	public abstract String getBaseEnergyRegenFormula();

	static {
		RpgClass.initialize();
	}

	public static void initialize() {
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

		FileConfiguration conf = YamlConfiguration.loadConfiguration(f);

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
				cl = new SimpleRpgClass(qualifiedName, privClasses.contains(name));
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
	 * Attempts to find an RpgClass using the given string. Matches are found using {@link Util#getFuzzyMatch(String, Collection) Util.getFuzzyMatch(String, Collection)}.
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