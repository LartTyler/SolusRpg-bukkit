package me.dbstudios.solusrpg.chat;

import java.util.Set;

import me.dbstudios.solusrpg.entities.player.RpgPlayer;

public interface ChannelLogger {
	public void log(RpgPlayer sender, String message, Set<RpgPlayer> recipients);
}