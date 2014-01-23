package me.dbstudios.solusrpg.entities.stats;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.config.Configuration;
import me.dbstudios.solusrpg.config.Directories;
import me.dbstudios.solusrpg.events.player.RpgPlayerAuxStatLevelEvent;
import me.dbstudios.solusrpg.events.player.RpgPlayerCoreStatLevelEvent;
import me.dbstudios.solusrpg.exception.CreationException;
import me.dbstudios.solusrpg.util.Initializable;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class AuxStat extends Initializable implements Listener {
	private static Map<String, AuxStat> stats = new HashMap<>();
	private static Map<String, String> displayNameLookup = new HashMap<>();
	private static Map<String, String> pathNameLookup = new HashMap<>();

	private final String fullyQualifiedName;

	private Metadata<String> metadata = new Metadata<>();
	private List<StatScaler> scalers = new ArrayList<>();
	private Map<Integer, StatRank> ranks = new HashMap<>();

	static {
		AuxStat.initialize();
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

		FileConfiguration conf = YamlConfiguration.load(f);

		for (String name : conf.getStringList("aux-stats.enabled")) {
			String qualifiedName = Util.toQualifiedName(name, "Stat");

			File statFile = new File(Directories.CONFIG_STATS + qualifiedName + ".yml");

			if (!statFile.exists()) {
				SolusRpg.log(Level.WARNING, String.format("I could not find a configuration for %s using fully-qualified name '%s'.", name, qualifiedName));

				continue;
			}

			AuxStat stat = null;

			try {
				stat = new AuxStat(qualifiedName);

				stats.put(qualifiedName, stat);

				pathNameLookup.put(stat.getPathName(), qualifiedName);
				displayNameLookup.put(stat.getDisplayName(), qualifiedName);

				Bukkit.getPluginManager().registerEvents(stat, SolusRpg.getInstance());
			} catch (CreationException e) {
				SolusRpg.log(Level.WARNING, String.format("I encountered an exception while initializing %s; please check the configuration file for errors.", qualifiedName));

				if (Configuration.is("logging.verbose"))
					e.printStackTrace();
			}
		}

		for (AuxStat stat : AuxStats.getAllAuxStats())
			stat
				.validateScalers()
				.validateRanks();

		initialized = true;

		SolusRpg.log(String.format("Auxiliary stats initialized in %d milliseconds.", System.currentTimeMillis() - initStart));
	}

	private AuxStat(String fqn) {
		File f = new File(Directories.CONFIG_STATS + fqn + ".yml");

		if (!f.exists())
			throw new CreationException(String.format("No configuration file found for auxiliary stat with qualified name '%s'.", fqn));

		this.fullyQualifiedName = fqn;

		FileConfiguration conf = YamlConfiguration.load(f);

		if (conf.isConfigurationSection("metadata"))
			for (String key : conf.getConfigurationSection("metadata").getKeys(true))
				metadata.set(key, conf.get("metadata." + key));

		if (!metadata.hasOfType("display-name", String.class))
			SolusRpg.log(Level.WARNING, String.format("%s does not have a display name explicitly defined; '%s' will be used, though it is recommended that you set one", fqn, this.getDisplayName()));

		if (!metadata.hasOfType("path-name", String.class))
			SolusRpg.log(Level.WARNING, String.format("%s does not have a path name explicitly defined; '%s' will be used, thought it is recommended that you set one", fqn, this.getPathName()));

		for (String ident : conf.getConfigurationSection("scaling.core-stats").getKeys(false)) {
			StatType type = StatType.fromAbbreviation(ident);

			if (type != null)
				try {
					scalers.add(new StatScaler(type, conf.getString("scaling.core-stats." + ident)));
				} catch (CreationException e) {
					if (Configuraiton.is("logging.verbose"))
						e.printStackTrace();
				}
			else
				SolusRpg.log(Level.WARNING, String.format("'%s' is not a valid identifier for a core stat; please make sure you've used the abbreviation in the core stat scaling definition.", ident));
		}

		for (String ident : conf.getConfigurationSection("scaling.aux-stats").getKeys(false))
			try {
				scalers.add(new StatScaler(ident, conf.getString("scaling.aux-stats." + ident)));
			} catch (CreationException e) {
				if (Configuraiton.is("logging.verbose"))
					e.printStackTrace();
			}

		metadata.set("rank-cap", conf.getInt("ranks.rank-cap", 0));

		for (String key : conf.getConfigurationSection("ranks").getKeys(false))
			try {
				int rankLevel = Integer.parseInt(key);
				StatRank rank = new StatRank(this, rankLevel);

				ranks.put(rankLevel, rank);
			} catch (CreationException e) {
				if (Configuration.is("logging.verbose"))
					e.printStackTrace();
			} catch (NumberFormatException e) {
				SolusRpg.log(Level.WARNING, String.format("'%s' is not a valid rank level for %s; ranks must be an integer.", key, this.getDisplayName()));

				if (Configuration.is("logging.verbose"))
					e.printStackTrace();
			}
	}

	public String getName() {
		return this.fullyQualifiedName;
	}

	public String getDisplayName() {
		return metadata.getAsType("display-name", String.class, this.fullyQualifiedName);
	}

	public String getPathName() {
		return metadata.getAsType("path-name", String.class, Util.toPathName(this.getDisplayName()));
	}

	public int getRankCap() {
		return metadata.getAsType("rank-cap", Integer.class, 0);
	}

	public boolean isRankCapped() {
		return this.getRankCap() > 0;
	}

	public StatScaler getScalerFor(AuxStat auxStat) {
		for (StatScaler scaler : this.scalers)
			if (!scaler.isCoreStatScaler() && auxStat.getName().equals(scaler.getIdentifier()))
				return scaler;

		return null;
	}

	public StatScaler getScalerFor(StatType coreStat) {
		for (StatScaler scaler : this.scalers)
			if (scaler.isCoreStatScaler() && coreStat == scaler.getCoreStatType())
				return scaler;

		return null;
	}

	public boolean hasScalerFor(AuxStat auxStat) {
		return this.getScalerFor(auxStat) != null;
	}

	public boolean hasScalerFor(StatType coreStat) {
		return this.getScalerFor(coreStat) != null;
	}

	// -------- Post-initialization methods ----------

	protected AuxStat validateScalers() {
		for (StatScaler scaler : scalers)
			if (!scaler.isCoreStatScaler() && AuxStat.getByFQN(scaler.getIdentifier()) == null) {
				scalers.remove(scaler);

				SolusRpg.log(
					Level.WARNING,
					String.format("An invalid identifier '%s' was found in a stat scaler for %s; it has been removed, but it is recommended that you remove the unnecessary definition.", scaler.getIdentifier(), this.getQualifiedName())
				);
			} else if (!scaler.isCoreStatScaler() && AuxStat.getByFQN(scaler.getIdentifier()) == this) {
				scalers.remove(scaler);

				SolusRpg.log(Level.WARNGING, String.format("A cyclical scaler reference was detected in %s; it has been removed, but it is recommended that you remove the unnecessary definition.", this.getQualifiedName()));
			}

		return this;
	}

	protected AuxStat validateRanks() {
		for (StatRank rank : ranks)
			rank.validateRequirements();
	}

	// -------- Event Listeners ----------

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onRpgPlayerAuxStatLevel(RpgPlayerAuxStatLevelEvent ev) {
		if (ev.isCancelled() || !this.hasScalerFor(ev.getStat()))
			return;

		StatScaler scaler = this.getScalerFor(ev.getStat());
		RpgPlayer player  = ev.getPlayer();

		// stat level = this level + scaler bonus @ new level
		player.setStatLevel(this, player.getStatLevel(this) + scaler.getBonus(ev.getNewLevel()));
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onRpgPlayerCoreStatLevel(RpgPlayerCoreStatLevel ev) {
		if (ev.isCancelled() || !this.hasScalerFor(ev.getType())
			return;

		StatScaler scaler = this.getScalerFor(ev.getType());
		RpgPlayer player  = ev.getPlayer();

		// stat level = this level + scaler bonus @ new level
		player.setStatLevel(ev.getType(), player.getStatLevel(this) + scaler.getBonus(ev.getNewLevel()));
	}

	// -------- Static factory methods ------------

	public static Set<String> getQualifiedNames() {
		return stats.keySet();
	}

	public static Set<String> getDisplayNames() {
		return displayNameLookup.keySet();
	}

	public static Set<String> getPathNames() {
		return pathNameLookup.keySet();
	}

	public static AuxStat getByFQN(String fqn) {
		return stats.get(fqn);
	}

	public static AuxStat getByDisplayName(String name) {
		if (!displayNameLookup.containsKey(name))
			return null;

		return AuxStat.getByFQN(displayNameLookup.get(name));
	}

	public static AuxStat getByPathName(String path) {
		if (!pathNameLookup.containsKey(path))
			return null;

		return AuxStat.getByFQN(pathNameLookup.get(path));
	}

	public static List<AuxStat> find(String name) {
		List<String> search = Util.getFuzzyMatch(name, AuxStat.getDisplayNames());
		List<AuxStat> matches = new ArrayList<>();

		for (String s : search)
			matches.add(AuxStat.getByDisplayName(s))

		return matches;
	}

	public static Collection<AuxStat> getAllAuxStats() {
		return stats.values();
	}
}