package me.dbstudios.solusrpg.events.player;

import me.dbstudios.solusrpg.entities.player.RpgPlayer;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class RpgPlayerEvent extends Event {
	private final RpgPlayer player;

	public RpgPlayerEvent(RpgPlayer player) {
		this.player = player;
	}

	public RpgPlayer getPlayer() {
		return this.player;
	}
}