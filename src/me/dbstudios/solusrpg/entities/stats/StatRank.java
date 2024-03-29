package me.dbstudios.solusrpg.entities.stats;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import me.dbstudios.solusrpg.AuxStatFactory;
import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.config.Directories;
import me.dbstudios.solusrpg.entities.player.RpgPlayer;
import me.dbstudios.solusrpg.entities.player.PlayerModifier;
import me.dbstudios.solusrpg.entities.player.PlayerPermitModifier;
import me.dbstudios.solusrpg.events.player.RpgActionType;
import me.dbstudios.solusrpg.events.player.RpgPlayerBeforeActionEvent;
import me.dbstudios.solusrpg.exceptions.CreationException;
import me.dbstudios.solusrpg.util.Util;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class StatRank implements Listener {
	private final List<StatRequirement> requirements = new ArrayList<>();
	private final List<PlayerModifier> modifiers = new ArrayList<>();
	private final AuxStat stat;
	private final int rank;

	public StatRank(AuxStat stat, int rank) {
		FileConfiguration conf = YamlConfiguration.loadConfiguration(new File(Directories.CONFIG_STATS + stat.getName() + ".yml"));

		if (!conf.isConfigurationSection("ranks." + rank)) {
			SolusRpg.log(Level.WARNING, String.format("I encountered an error while defining rank %d of %s; there is no rank node of that level present in the configuration.", rank, stat.getName()));

			throw new CreationException(String.format("Missing rank %d definition in %s", rank, stat.getName()));
		}

		this.stat = stat;
		this.rank = rank;

		ConfigurationSection sect = conf.getConfigurationSection("ranks." + rank);

		if (sect.isConfigurationSection("requires.core-stats"))
			for (String key : sect.getConfigurationSection("requires.core-stats").getKeys(false)) {
				StatType type = StatType.fromAbbreviation(key);

				if (type == null) {
					SolusRpg.log(Level.WARNING, String.format("Unknown core stat abbreviation '%s'.", key));

					continue;
				}

				requirements.add(new CoreStatRequirement(type, sect.getInt("requires.core-stats." + key)));
			}

		if (sect.isConfigurationSection("requires.aux-stats"))
			for (String key : sect.getConfigurationSection("requires.aux-stats").getKeys(false))
				requirements.add(new AuxStatRequirement(key, sect.getInt("requires.aux-stats." + key)));

		if (sect.isInt("requires.player-level"))
			requirements.add(new PlayerLevelStatRequirement(sect.getInt("requires.player-level")));

		for (RpgActionType t : RpgActionType.values()) {
			if (!t.isPermitAction())
				continue;

			List<String> permits = sect.getStringList("permit." + t.name().toLowerCase());

			if (permits.isEmpty())
				continue;

			modifiers.add(new PlayerPermitModifier(t, permits));
		}
	}

	public void applyRank(RpgPlayer target) {
		if (!this.hasRequirements(target))
			SolusRpg.log(Level.WARNING, String.format("Rank %d of %s applied to %s without meeting rank requirements; was this intentional?", this.rank, this.stat.getName(), target.getName()));

		for (PlayerModifier mod : this.modifiers)
			mod.modify(target);
	}

	public void removeRank(RpgPlayer target) {
		for (PlayerModifier mod : this.modifiers)
			mod.unmodify(target);
	}

	public List<StatRequirement> getRequirements() {
		return Collections.unmodifiableList(this.requirements);
	}

	public StatRank validateRequirements() {
		for (int i = 0; i < requirements.size(); i++) {
			if (!(requirements.get(i) instanceof AuxStatRequirement))
				continue;

			AuxStatRequirement req = (AuxStatRequirement)requirements.get(i);

			if (req.hasBeenValidated())
				continue;

			AuxStat stat = AuxStatFactory.getByFQN(req.getStatName());

			if (stat == null) {
				SolusRpg.log(Level.WARNING, String.format("Could not locate auxiliary stat with qualified name '%s' during initialization of rank %d of %s", req.getStatName(), this.rank, this.stat.getName()));

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