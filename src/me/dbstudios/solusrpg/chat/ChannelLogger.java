package me.dbstudios.solusrpg.chat;

public interface ChannelLogger {
	public void log(RpgPlayer sender, String message, Set<RpgPlayer> recipients);
}