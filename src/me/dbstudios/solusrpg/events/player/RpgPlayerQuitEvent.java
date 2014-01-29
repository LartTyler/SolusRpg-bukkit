package me.dbstudios.solusrpg.events.player;

import me.dbstudios.solusrpg.entities.player.RpgPlayer;

public class RpgPlayerQuitEvent extends RpgPlayerEvent {
	private String quitMessage;

	public RpgPlayerQuitEvent(RpgPlayer player, String quitMessage) {
		super(player);

		this.quitMessage = quitMessage;
	}

	public String getQuitMessage() {
		return this.quitMessage;
	}

	public void setQuitMessage(String quitMessage) {
		this.quitMessage = quitMessage;
	}
}