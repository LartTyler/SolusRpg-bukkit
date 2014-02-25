package me.dbstudios.solusrpg.gui;

import me.dbstudios.solusrpg.entities.player.RpgPlayer;

public interface RpgPopup {
	public RpgPopup attachTo(RpgPlayer player);
	public RpgPopup attachTo(RpgPlayer player, Method callback);
	public RpgPopup removeFrom(RpgPlayer player);
}