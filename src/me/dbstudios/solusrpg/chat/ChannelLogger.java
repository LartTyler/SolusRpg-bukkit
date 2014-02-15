package me.dbstudios.solusrpg.chat;

import java.util.Set;

import me.dbstudios.solusrpg.entities.player.RpgPlayer;

public interface ChannelLogger {
	public ChannelLogger logChannelJoin(RpgPlayer player);
	public ChannelLogger logChannelLeave(RpgPlayer player);
	public ChannelLogger logMessageSent(RpgPlayer sender, String message);
	public ChannelLogger logMessageRecieved(RpgPlayer sender, RpgPlayer receiver, String message);
	public ChannelLogger flush();
	public ChannelLogger clean();
}