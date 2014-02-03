package me.dbstudios.solusrpg.entities.stats;

import java.util.logging.Level;

import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.entities.player.RpgPlayer;
import me.dbstudios.solusrpg.util.Util;

public class AuxStatRequirement implements StatRequirement {
	private boolean validated = false;
	private String statName;
	private AuxStat stat;
	private int level;

	/**
	 * Temporary constructor; should only be used during initilization of stats
	 */
	public AuxStatRequirement(String stat, int level) {
		this.statName = Util.toQualifiedName(stat);
		this.level = level;
	}

	public AuxStatRequirement(AuxStat stat, int level) {
		this.stat = stat;
		this.level = level;
		this.validated = true;
	}

	public boolean satisfiedBy(RpgPlayer player) {
		if (!this.validated)
			SolusRpg.log(Level.WARNING, "AuxStatRequirement#satisfiedBy called on an unverified AuxStatRequirement instance! Something went while I was initializing auxiliary stats...");

		if (this.stat == null)
			this.stat = AuxStat.getByFQN(this.statName);

		return player.getStatLevel(this.stat) >= this.level;
	}

	public boolean hasBeenValidated() {
		return this.validated;
	}

	public String getStatName() {
		return this.statName != null ? this.statName : stat.getName();
	}

	public int getLevel() {
		return this.level;
	}
}