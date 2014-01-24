package me.dbstudios.solusrpg.events.player;

import me.dbstudios.solusrpg.entities.player.RpgPlayer;
import me.dbstudios.solusrpg.entities.stats.AuxStat;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class RpgPlayerCoreStatLevelEvent extends RpgPlayerEvent implements Cancellable {
	private static HandlerList handlers = new HandlerList();

	private final StatType type;
	private final int oldLevel;

	private int newLevel;
	private boolean cancelled = false;

	public RpgPlayerCoreStatLevelEvent(RpgPlayer player, StatType type, int oldLevel, int newLevel) {
		super(player);

		this.type = type;
		this.oldLevel = oldLevel;
		this.newLevel = newLevel;
	}

	public StatType getType() {
		return this.type;
	}

	public int getOldLevel() {
		return this.oldLevel;
	}

	public int getNewLevel() {
		return this.newLevel;
	}

	public void setNewLevel(int newLevel) {
		this.newLevel = newLevel;
	}

	public boolean isCancelled() {
		return this.cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}