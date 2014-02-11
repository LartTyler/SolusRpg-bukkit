package me.dbstudios.solusrpg.entities.stats;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.config.Configuration;
import me.dbstudios.solusrpg.config.Directories;
import me.dbstudios.solusrpg.config.Metadata;
import me.dbstudios.solusrpg.entities.player.RpgPlayer;
import me.dbstudios.solusrpg.events.player.RpgPlayerAuxStatLevelEvent;
import me.dbstudios.solusrpg.events.player.RpgPlayerCoreStatLevelEvent;
import me.dbstudios.solusrpg.exceptions.CreationException;
import me.dbstudios.solusrpg.util.Util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class AuxStat {
	private final String fullyQualifiedName;

	private Metadata<String> metadata = new Metadata<>();
	private List<StatScaler> scalers = new ArrayList<>();

	private AuxStat(String fqn) {
		File f = new File(Directories.CONFIG_STATS + fqn + ".yml");

		if (!f.exists())
			throw new CreationException(String.format("No configuration file found for auxiliary stat with qualified name '%s'.", fqn));

		this.fullyQualifiedName = fqn;

		FileConfiguration conf = YamlConfiguration.loadConfiguration(f);

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
					if (Configuration.is("logging.verbose"))
						e.printStackTrace();
				}
			else
				SolusRpg.log(Level.WARNING, String.format("'%s' is not a valid identifier for a core stat; please make sure you've used the abbreviation in the core stat scaling definition.", ident));
		}

		for (String ident : conf.getConfigurationSection("scaling.aux-stats").getKeys(false))
			try {
				scalers.add(new StatScaler(ident, conf.getString("scaling.aux-stats." + ident)));
			} catch (CreationException e) {
				if (Configuration.is("logging.verbose"))
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

	public void applyRank(RpgPlayer target, int rank) {
		if (!ranks.containsKey(rank))
			return;

		ranks.get(rank).applyRank(target);
	}

	public void removeRank(RpgPlayer target, int rank) {
		if (!ranks.containsKey(rank))
			return;

		ranks.get(rank).removeRank(target);
	}

	public StatScaler getScalerFor(String fqn) {
		return this.getScalerFor(AuxStat.getByFQN(fqn));
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

	public AuxStat addStatScaler(StatScaler scaler, String fqn) {
		if (this.hasScalerFor(fqn))
			this.removeStatScaler(fqn);

		scalers.add(scaler);

		return this;
	}

	public AuxStat addStatScaler(StatScaler scaler, AuxStat stat) {
		return this.addStatScaler(scaler, stat.getName());
	}

	public AuxStat addStatScaler(StatScaler scaler, StatType type) {
		if (this.hasScalerFor(type))
			this.removeStatScaler(type);

		scalers.add(scaler);

		return this;
	}

	public AuxStat removeStatScaler(String fqn) {
		StatScaler scaler = this.getScalerFor(fqn);

		if (scaler != null)
			scalers.remove(scaler);

		return this;
	}

	public AuxStat removeStatScaler(AuxStat stat) {
		return this.removeStatScaler(stat.getName());
	}

	public AuxStat removeStatScaler(StatType type) {
		StatScaler scaler = this.getScalerFor(type);

		if (scaler != null)
			scalers.remove(scaler);

		return this;
	}

	public List<StatScaler> getStatScalers() {
		return Collections.unmodifiableList(this.scalers);
	}

	public boolean hasScalerFor(String fqn) {
		return this.hasScalerFor(AuxStat.getByFQN(fqn));
	}

	public boolean hasScalerFor(AuxStat auxStat) {
		return this.getScalerFor(auxStat) != null;
	}

	public boolean hasScalerFor(StatType coreStat) {
		return this.getScalerFor(coreStat) != null;
	}

	public AuxStat validate() {
		for (StatScaler scaler : scalers)
			if (!scaler.isCoreStatScaler() && AuxStatFactory.getByFQN(scaler.getIdentifier()) == null) {
				scalers.remove(scaler);

				SolusRpg.log(
					Level.WARNING,
					String.format("An invalid identifier '%s' was found in a stat scaler for %s; it has been removed, but it is recommended that you remove the unnecessary definition.", scaler.getIdentifier(), this.getName())
				);
			} else if (!scaler.isCoreStatScaler() && AuxStatFactory.getByFQN(scaler.getIdentifier()) == this) {
				scalers.remove(scaler);

				SolusRpg.log(Level.WARNING, String.format("A circular scaler reference was detected in %s; it has been removed, but it is recommended that you remove the unnecessary definition.", this.getName()));
			}

		for (StatRank rank : ranks.values())
			rank.validateRequirements();

		return this;
	}
}