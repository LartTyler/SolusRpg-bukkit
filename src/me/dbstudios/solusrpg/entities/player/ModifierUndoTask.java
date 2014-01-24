package me.dbstudios.solusrpg.entities.player;

import org.bukkit.scheduler.BukkitRunnable;

public class ModifierUndoTask extends BukkitRunnable {
	private final PlayerModifier modifier;
	private final RpgPlayer target;

	public ModifierUndoTask(PlayerModifier modifier, RpgPlayer target) {
		this.modifier = modifier;
		this.target = target;
	}

	public void run() {
		modifier.unmodify(target);
	}
}