package me.dbstudios.solusrpg.entities.stats;

import me.dbstudios.solusrpg.entities.player.RpgPlayer;

public interface StatRequirement {
	public boolean satisfiedBy(RpgPlayer player);
}