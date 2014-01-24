package me.dbstudios.solusrpg.entities.player;

import java.util.UUID;

import me.dbstudios.solusrpg.config.Metadata;

import org.bukkit.entity.Player;

public class SimpleRpgPlayer implements RpgPlayer {
	private static final Map<UUID, RpgPlayer> players = new HashMap<>();

	private final Metadata<String> metadata = new Metadata<>();
	private final Player basePlayer;

	private SimpleRpgPlayer(Player basePlayer) {
		this.basePlayer = basePlayer;
	}

	public Player getBasePlayer() {
		return this.basePlayer;
	}

	public String getName() {
		return basePlayer.getName();
	}

	public String getDisplayName() {
		return basePlayer.getDisplayName();
	}

	public int getLevel() {
		return basePlayer.getLevel();
	}

	public RpgPlayer setLevel(int level) {
		basePlayer.setLevel(level);

		return this;
	}

	public int getExp() {
		return metadata.getAsType("experience", Integer.class, 0);
	}

	public RpgPlayer setExp(int exp) {
		metadata.set("experience", exp);

		return this;
	}

	public static RpgPlayer getOrCreate(Player basePlayer) {
		if (players.containsKey(basePlayer.getUniqueId()))
			return players.get(basePlayer.getUniqueId());

		RpgPlayer player = new RpgPlayer(basePlayer);

		players.put(basePlayer.getUniqueId(), player);

		return player;
	}
}