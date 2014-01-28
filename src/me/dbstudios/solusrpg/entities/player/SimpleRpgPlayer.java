package me.dbstudios.solusrpg.entities.player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.dbstudios.solusrpg.config.Metadata;
import me.dbstudios.solusrpg.entities.RpgPlayer;
import me.dbstudios.solusrpg.entities.stats.AuxStat;
import me.dbstudios.solusrpg.entities.stats.StatType;
import me.dbstudios.solusrpg.events.player.RpgActionType;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SimpleRpgPlayer implements RpgPlayer {
	private static final Map<UUID, RpgPlayer> players = new HashMap<>();

	private final Map<StatType, Integer> coreStats = new EnumMap<>(StatType.class);
	private final Map<String, Integer> stats = new HashMap<>();
	private final Metadata<String> metadata = new Metadata<>();
	private final Player basePlayer;

	private ExperienceScaler expScaler = null;

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

		// Silly fix to update exp bar progression on level up
		this.setExp(this.getExp());

		return this;
	}

	public int getExp() {
		return metadata.getAsType("experience", Integer.class, 0);
	}

	public RpgPlayer setExp(int exp) {
		metadata.set("experience", exp);

		ExperienceScaler scaler = this.getExperienceScaler();

		if (scaler = null)
			scaler = ExperienceScaler.getGlobalScaler();

		this.setRealExp(scaler.toRealExp(this.getLevel(), exp));

		return this;
	}

	public float getRealExp() {
		return basePlayer.getExp();
	}

	public RpgPlayer setRealExp(float exp) {
		basePlayer.setExp(exp);

		return this;
	}

	public int getStatLevel(AuxStat stat) {
		return this.getStatLevel(stat.getName());
	}

	public int getStatLevel(String fqn) {
		if (stats.containsKey(fqn))
			return stats.get(fqn);

		return 0;
	}

	public int getStatLevel(StatType type) {
		if (coreStats.containsKey(type))
			return coreStats.get(type);

		return 0;
	}

	public RpgPlayer setStatLevel(AuxStat stat, int level) {
		return this.setStatLevel(stat.getName(), level);
	}

	public RpgPlayer setStatLevel(String fqn, int level) {
		if (AuxStat.getByFQN(fqn) == null)
			SolusRpg.log(Level.WARNING, String.format("Attempted to set non-existant auxiliary stat '%s'.", fqn));
		else
			stats.put(fqn, level);

		return this;
	}

	public RpgPlayer setStatLevel(StatType type, int level) {
		coreStats.put(type, level);

		return this;
	}

	public boolean isAllowed(RpgActionType action, Material material) {

	}

	public static RpgPlayer getOrCreate(Player basePlayer) {
		if (players.containsKey(basePlayer.getUniqueId()))
			return players.get(basePlayer.getUniqueId());

		RpgPlayer player = new RpgPlayer(basePlayer);

		players.put(basePlayer.getUniqueId(), player);

		return player;
	}
}