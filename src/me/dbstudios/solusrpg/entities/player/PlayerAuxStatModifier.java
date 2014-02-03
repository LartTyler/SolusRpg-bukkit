package me.dbstudios.solusrpg.entities.player;

import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.entities.stats.AuxStat;

import org.bukkit.Bukkit;

public class PlayerAuxStatModifier implements PlayerModifier {
	private final AuxStat stat;
	private final int modifier;

	public PlayerAuxStatModifier(AuxStat stat, int modifier) {
		this.stat = stat;
		this.modifier = modifier;
	}

	public boolean isMalus() {
		return this.modifier < 0;
	}

	public void modify(RpgPlayer player) {
		player.setStatLevel(this.stat, player.getRealStatLevel(this.stat) + this.modifier);
	}

	public void modify(RpgPlayer player, long duration) {
		this.modify(player);

		Bukkit.getScheduler().scheduleSyncDelayedTask(SolusRpg.getInstance(), new ModifierUndoTask(this, player), duration);
	}

	public void unmodify(RpgPlayer player) {
		player
			.setStatLevel(this.stat, player.getRealStatLevel(this.stat) - this.modifier)
			.removeModifier(this);
	}
}