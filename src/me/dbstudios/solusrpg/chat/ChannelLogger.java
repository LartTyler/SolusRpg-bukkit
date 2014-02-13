package me.dbstudios.solusrpg.chat;

import java.util.Set;

import me.dbstudios.solusrpg.entities.player.RpgPlayer;

public interface ChannelLogger {
	public void logChannelJoin(RpgPlayer player);
	public void logChannelLeave(RPgPlayer player);
	public void logMessageSent(RpgPlayer sender, String message);
	public void logMessageRecieved(RpgPlayer sender, RpgPlayer reciever, String message);
}