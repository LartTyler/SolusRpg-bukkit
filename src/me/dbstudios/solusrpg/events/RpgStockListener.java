package me.dbstudios.solusrpg.events;

import me.dbstudios.solusrpg.entities.player.RpgPlayer;
import me.dbstudios.solusrpg.entities.player.SimpleRpgPlayer;
import me.dbstudios.solusrpg.events.player.RpgPlayerJoinEvent;
import me.dbstudios.solusrpg.events.player.RpgPlayerQuitEvent;
import me.dbstudios.solusrpg.language.LanguageManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class RpgStockListener implements Listener {
	@EventHandler(priority = EventPriority.LOW)
	public void onRpgPlayerJoin(RpgPlayerJoinEvent ev) {
		RpgPlayer player = ev.getPlayer();

		if (LanguageManager.has("server.player-join-message"))
			ev.setJoinMessage(
				LanguageManager
					.get("server.player-join-message")
						.reset()
						.setParameter("player", player.getDisplayName())
						.setParameter("player-name", player.getDisplayName())
						.setParameter("class", player.getRpgClass().getDisplayName())
						.setParameter("class-name", player.getRpgClass().getName())
						.asText()
			);
		else
			ev.setJoinMessage("");
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onRpgPlayerQuit(RpgPlayerQuitEvent ev) {
		RpgPlayer player = ev.getPlayer();

		if (LanguageManager.has("server.player-quit-message"))
			ev.setQuitMessage(
				LanguageManager
					.get("server.player-quit-message")
						.reset()
						.setParameter("player", player.getDisplayName())
						.setParameter("player-name", player.getDisplayName())
						.setParameter("class", player.getRpgClass().getDisplayName())
						.setParameter("class-name", player.getRpgClass().getName())
						.asText()
			);
		else
			ev.setQuitMessage("");
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onRpgPlayerQuitFinal(RpgPlayerQuitEvent ev) {
		SimpleRpgPlayer.destroy(ev.getPlayer());
	}
}