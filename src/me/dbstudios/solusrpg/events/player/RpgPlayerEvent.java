package me.dbstudios.solusrpg.events.player;

import me.dbstudios.solusrpg.entities.player.RpgPlayer;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class RpgPlayerEvent extends Event {
	private static HandlerList handlers = new HandlerList();

	private final RpgPlayer player;

	public RpgPlayerEvent(RpgPlayer player) {
		this.player = player;
	}

	public RpgPlayer getPlayer() {
		return this.player;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}