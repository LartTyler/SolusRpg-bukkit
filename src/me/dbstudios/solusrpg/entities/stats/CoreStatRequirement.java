package me.dbstudios.solusrpg.entities.stats;

import me.dbstudios.solusrpg.entities.player.RpgPlayer;

public class CoreStatRequirement implements StatRequirement {
	private final StatType type;
	private final int level;

	public CoreStatRequirement(StatType type, int level) {
		this.type = type;
		this.level = level;
	}

	public boolean satisfiedBy(RpgPlayer player) {
		return player.getStatLevel(this.type) >= this.level;
	}
}