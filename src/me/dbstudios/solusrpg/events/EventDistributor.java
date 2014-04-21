package me.dbstudios.solusrpg.events;

import me.dbstudios.solusrpg.RpgPlayerFactory;
import me.dbstudios.solusrpg.RpgPopupFactory;
import me.dbstudios.solusrpg.entities.player.RpgPlayer;
import me.dbstudios.solusrpg.events.player.RpgPlayerJoinEvent;
import me.dbstudios.solusrpg.events.player.RpgPlayerQuitEvent;
import me.dbstudios.solusrpg.gui.RpgPopup;
import me.dbstudios.solusrpg.gui.RpgPopupType;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventDistributor implements Listener {
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent ev) {
		RpgPlayer player = RpgPlayerFactory.getPlayer(ev.getPlayer());

		if (player.getRpgClass() == null) {
			System.out.println(player.getName() + " has joined for the first time.");

			RpgPopup popup = RpgPopupFactory.getPopup(RpgPopupType.WELCOME);

			if (popup != null) {
				player.getBasePlayer().getMainScreen().attachPopupScreen(popup);
			} else
				System.out.println("Could not attach welcome popup!");
		} else {
			RpgPlayerJoinEvent event = new RpgPlayerJoinEvent(RpgPlayerFactory.getPlayer(ev.getPlayer()), ev.getJoinMessage());

			Bukkit.getPluginManager().callEvent(event);

			ev.setJoinMessage(event.getJoinMessage());
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerQuit(PlayerQuitEvent ev) {
		RpgPlayerQuitEvent event = new RpgPlayerQuitEvent(RpgPlayerFactory.getPlayer(ev.getPlayer()), ev.getQuitMessage());

		Bukkit.getPluginManager().callEvent(event);

		ev.setQuitMessage(event.getQuitMessage());
	}
}