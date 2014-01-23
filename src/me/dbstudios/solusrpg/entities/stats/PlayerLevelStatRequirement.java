package me.dbstudios.solusrpg.entities.stats;

public class PlayerLevelStatRequirement implements StatRequirement {
	private final int level;

	public PlayerLevelStatRequirement(int level) {
		this.level = level;
	}

	public boolean satisfiedBy(RpgPlayer player) {
		return this.level >= player.getLevel();
	}
}