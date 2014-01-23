package me.dbstudios.solusrpg.entities.stats;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.config.Directories;
import me.dbstudios.solusrpg.entities.player.RpgPlayer;
import me.dbstudios.solusrpg.exceptions.CreationException;
import me.dbstudios.solusrpg.util.Util;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class StatRank {
	private final List<StatRequirement> requirements = new ArrayList<>();
	private final AuxStat stat;
	private final int rank;

	public StatRank(AuxStat stat, int rank) {
		FileConfiguration conf = YamlConfiguration.load(new File(Directories.CONFIG_STATS + stat.getQualifiedName() + ".yml"));

		if (!conf.isConfigurationSection("ranks." + rank)) {
			SolusRpg.log(Level.WARNING, String.format("I encountered an error while defining rank %d of %s; there is no rank node of that level present in the configuration.", rank, stat.getQualifiedName()));

			throw new CreationException(String.format("Missing rank %d definition in %s", rank, stat.getQualifiedName()));
		}

		this.stat = stat;
		this.rank = rank;

		ConfigurationSection sect = conf.getConfigurationSection("ranks." + rank);

		for (String key : sect.getConfigurationSection("requires.core-stats").getKeys(false)) {
			StatType type = StatType.fromAbbreviation(key);

			if (type == null) {
				SolusRpg.log(Level.WARNING, String.format("Unknown core stat abbreviation '%s'.", key));

				continue;
			}

			requirements.add(new CoreStatRequirement(type, sect.getInt("requires.core-stats." + key)));
		}

		for (String key : sect.getConfigurationSection("requires.aux-stats").getKeys(false))
			requirements.add(new AuxStatRequirement(key, sect.getInt("requires.aux-stats." + key)));

		if (sect.isInt("requires.player-level"))
			requirements.add(new PlayerLevelStatRequirement(sect.getInt("requires.player-level")));
	}

	public List<StatRequirement> getRequirements() {
		return Collections.unmodifiableList(this.requirements);
	}

	public StatRank validateRequirements() {
		for (int i = 0; i < requirements.length; i++) {
			if (!(requirements.get(i) instanceof AuxStatRequirement))
				continue;

			AuxStatRequirement req = (AuxStatRequirement)requirements.get(i);

			if (req.hasBeenValidated())
				continue;

			AuxStat stat = AuxStat.getByFQN(req.getStatName());

			if (stat == null) {
				SolusRpg.log(Level.WARNING, String.format("Could not locate auxiliary stat with qualified name '%s' during initialization of rank %d of %s", req.getStatName(), this.level, this.stat.getName()));

				continue;
			}

			requirements.set(i, new AuxStatRequirement(stat, req.getLevel()));
		}

		return this;
	}

	public boolean hasRequirements(RpgPlayer player) {
		for (StatRequirement requirement : this.requirements)
			if (!requirement.satisfiedBy(player))
				return false;

		return true;
	}

	public AuxStat getStat() {
		return this.stat;
	}

	public int getRank() {
		return this.rank;
	}
}