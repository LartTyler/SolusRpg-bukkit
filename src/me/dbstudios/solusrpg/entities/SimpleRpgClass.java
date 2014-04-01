package me.dbstudios.solusrpg.entities;

import java.io.File;
import java.util.EnumSet;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import me.dbstudios.solusrpg.AuxStatFactory;
import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.config.Directories;
import me.dbstudios.solusrpg.config.Metadata;
import me.dbstudios.solusrpg.entities.stats.AuxStat;
import me.dbstudios.solusrpg.entities.stats.StatType;
import me.dbstudios.solusrpg.events.player.RpgActionType;
import me.dbstudios.solusrpg.exceptions.CreationException;
import me.dbstudios.solusrpg.util.Util;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class SimpleRpgClass implements RpgClass {
	private final String fullyQualifiedName;

	private Map<RpgActionType, Set<Material>> permits = new EnumMap<>(RpgActionType.class);
	private Map<StatType, Integer> coreStats = new EnumMap<>(StatType.class);
	private Map<String, Integer> stats = new HashMap<>();
	private Metadata<String> metadata = new Metadata<>();
	private Metadata<String> healthMeta = new Metadata<>();
	private Metadata<String> energyMeta = new Metadata<>();

	public SimpleRpgClass(String fqn) {
		File f = new File(Directories.CONFIG_CLASSES + fqn + ".yml");

		if (!f.exists())
			throw new CreationException(String.format("No configuraiton file found for class with FQN '%s'.", fqn));

		this.fullyQualifiedName = fqn;

		FileConfiguration conf = YamlConfiguration.loadConfiguration(f);

		// If the class being loaded extends another, grab that class configuration and overwrite paths NOT
		// present in the child configuration.
		if (conf.getString("extends", null) != null) {
			File baseFile = new File(Directories.CONFIG_CLASSES + Util.toQualifiedName(conf.getString("extends"), "Class") + ".yml");

			if (baseFile.exists()) {
				FileConfiguration base = YamlConfiguration.loadConfiguration(baseFile);

				for (String key : base.getKeys(true))
					if (!key.equals("extends") && !conf.isSet(key))
						conf.set(key, base.get(key));
					else
						SolusRpg.log(Level.WARNING, String.format("Parent class of %s is a child of another class, and descending inheritance is not supported!", fqn));
			}
		}

		// Only load values under the "metdata" path into our Metdata storage... Everything else will be managed by another system.
		if (conf.isConfigurationSection("metadata"))
			for (String key : conf.getConfigurationSection("metadata").getKeys(true))
				metadata.set(key, conf.get("metadata." + key));

		for (StatType type : StatType.values())
			coreStats.put(type, conf.getInt("vitals.core-stats." + type.name().toLowerCase(), 1));

		for (AuxStat stat : AuxStatFactory.getAll())
			stats.put(stat.getName(), conf.getInt("vitals.aux-stats." + stat.getPathName().toLowerCase(), 1));

		for (RpgActionType action : RpgActionType.values()) {
			Set<Material> p =  EnumSet.noneOf(Material.class);

			for (String m : conf.getStringList("permit." + action.name().toLowerCase())) {
				Material material = Material.matchMaterial(m);

				if (material != null && !p.contains(material))
					p.add(material);
				else if (material == null)
					SolusRpg.log(Level.WARNING, String.format("Invalid material name '%s' found in permit.%s node while loading %s.", m, action.name().toLowerCase(), this.fullyQualifiedName));
				else
					SolusRpg.log(Level.WARNING, String.format("Duplicate material found with name '%s' while loading %s.", material.name(), this.fullyQualifiedName));
			}

			permits.put(action, p);
		}

		if (conf.isConfigurationSection("vitals.meters.health"))
			for (String key : conf.getConfigurationSection("vitals.meters.health").getKeys(true))
				healthMeta.set(key, conf.get("vitals.meters.health." + key));

		if (conf.isConfigurationSection("vitals.meters.energy"))
			for (String key : conf.getConfigurationSection("vitals.meters.energy").getKeys(true))
				energyMeta.set(key, conf.get("vitals.meters.energy." + key));
	}

	public String getName() {
		return this.fullyQualifiedName;
	}

	public String getDisplayName() {
		return metadata.getAsType("display-name", String.class, this.fullyQualifiedName);
	}

	public String getDescription() {
		return metadata.getAsType("description", String.class, "");
	}

	public int getStatLevel(AuxStat stat) {
		return this.getStatLevel(stat.getName());
	}

	public int getStatLevel(String fqn) {
		return stats.get(fqn);
	}

	public int getStatLevel(StatType type) {
		return coreStats.get(type);
	}

	public boolean isAllowed(RpgActionType action, Material material) {
		return permits.get(action).contains(material);
	}

	public int getHealth() {
		return healthMeta.getAsType("base", Integer.class, 20);
	}

	public String getHealthName() {
		return healthMeta.getAsType("name", String.class, "Health");
	}

	public boolean hasHealthRegeneration() {
		return healthMeta.getAsType("regen.enabled", Boolean.class, true) && this.getHealthRegenRate() >= 0.05;
	}

	public double getHealthRegenRate() {
		return healthMeta.getAsType("regen.rate", Double.class, 5.0);
	}

	public String getHealthRegenFormula() {
		return healthMeta.getAsType("regen.formula", String.class, "max * 0.1");
	}

	public int getEnergy() {
		return energyMeta.getAsType("base", Integer.class, 100);
	}

	public String getEnergyName() {
		return energyMeta.getAsType("name", String.class, "Energy");
	}

	public boolean hasEnergyRegeneration() {
		return energyMeta.getAsType("regen.enabled", Boolean.class, true) && this.getEnergyRegenRate() >= 0.05;
	}

	public double getEnergyRegenRate() {
		return energyMeta.getAsType("regen.rate", Double.class, 5.0);
	}

	public String getEnergyRegenFormula() {
		return energyMeta.getAsType("regen.formula", String.class, "max * 0.05");
	}
}