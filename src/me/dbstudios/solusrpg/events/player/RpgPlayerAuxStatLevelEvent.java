package me.dbstudios.solusrpg.events.player;

import me.dbstudios.solusrpg.entities.player.RpgPlayer;
import me.dbstudios.solusrpg.entities.stats.AuxStat;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class RpgPlayerAuxStatLevelEvent extends RpgPlayerEvent implements Cancellable {
	private static HandlerList handlers = new HandlerList();

	private final AuxStat stat;
	private final int oldLevel;

	private int newLevel;
	private boolean cancelled = false;

	public RpgPlayerAuxStatLevelEvent(RpgPlayer player, String fqn, int oldLevel, int newLevel) {
		this(player, AuxStat.getByFQN(fqn), oldLevel, newLevel);
	}

	public RpgPlayerAuxStatLevelEvent(RpgPlayer player, AuxStat stat, int oldLevel, int newLevel) {
		super(player);

		this.stat = stat;
		this.oldLevel = oldLevel;
		this.newLevel = newLevel;
	}

	public AuxStat getStat() {
		return this.stat;
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