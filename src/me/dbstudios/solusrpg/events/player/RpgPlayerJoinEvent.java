package me.dbstudios.solusrpg.events.player;

import me.dbstudios.solusrpg.entities.player.RpgPlayer;

public class RpgPlayerJoinEvent extends RpgPlayerEvent {
	private String joinMessage;

	public RpgPlayerJoinEvent(RpgPlayer player, String joinMessage) {
		super(player);

		this.joinMessage = joinMessage;
	}

	public String getJoinMessage() {
		return this.joinMessage;
	}

	public void setJoinMessage(String joinMessage) {
		this.joinMessage = joinMessage;
	}
}