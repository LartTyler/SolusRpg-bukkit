package me.dbstudios.solusrpg.events;

import me.dbstudios.solusrpg.entities.player.RpgPlayer;
import me.dbstudios.solusrpg.entities.player.SimpleRpgPlayer;
import me.dbstudios.solusrpg.events.player.RpgPlayerJoinEvent;
import me.dbstudios.solusrpg.events.player.RpgPlayerQuitEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventDistributor implements Listener {
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent ev) {
		RpgPlayerJoinEvent event = new RpgPlayerJoinEvent(SimpleRpgPlayer.getOrCreate(ev.getPlayer()), ev.getJoinMessage());

		Bukkit.getPluginManager().callEvent(event);

		ev.setJoinMessage(event.getJoinMessage());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerQuit(PlayerQuitEvent ev) {
		RpgPlayerQuitEvent event = new RpgPlayerQuitEvent(SimpleRpgPlayer.getOrCreate(ev.getPlayer()), ev.getQuitMessage());

		Bukkit.getPluginManager().callEvent(event);

		ev.setQuitMessage(event.getQuitMessage());
	}
}