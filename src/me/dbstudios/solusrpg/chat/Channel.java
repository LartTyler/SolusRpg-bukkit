package me.dbstudios.solusrpg.chat;

import java.util.Set;

import me.dbstudios.solusrpg.chat.ChannelLogger;
import me.dbstudios.solusrpg.entities.player.RpgPlayer;

public interface Channel {
	public String getName();
	public String getDisplayName();
	public String getSymbol();
	public String getFormat();
	// public int getMemberLimit();
	public int getMemberCount();
	public Set<RpgPlayer> getMembers();
	public Channel addMember(RpgPlayer player);
	public Channel removeMember(RpgPlayer player);
	public boolean hasMember(RpgPlayer player);
	public Channel sendMessage(RpgPlayer sender, String message);
	public Channel attachLogger(ChannelLogger logger);
}