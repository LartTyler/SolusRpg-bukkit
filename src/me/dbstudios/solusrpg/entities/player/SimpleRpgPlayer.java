package me.dbstudios.solusrpg.entities.player;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import me.dbstudios.solusrpg.config.Configuration;
import me.dbstudios.solusrpg.config.Directories;
import me.dbstudios.solusrpg.config.Metadata;
import me.dbstudios.solusrpg.entities.RpgClass;
import me.dbstudios.solusrpg.entities.stats.AuxStat;
import me.dbstudios.solusrpg.entities.stats.StatType;
import me.dbstudios.solusrpg.events.player.RpgActionType;
import me.dbstudios.solusrpg.language.LanguageManager;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class SimpleRpgPlayer implements RpgPlayer {
	private static final Map<UUID, RpgPlayer> players = new HashMap<>();

	private final Map<RpgActionType, Set<Material>> permits = new EnumMap<>(RpgActionType.class);
	private final Map<StatType, Integer> coreStats = new EnumMap<>(StatType.class);
	private final Map<String, Integer> stats = new HashMap<>();
	private final Metadata<String> metadata = new Metadata<>();
	private final Player basePlayer;

	private ExperienceScaler expScaler = null;
	private RpgClass rpgClass = null;

	private SimpleRpgPlayer(Player basePlayer) {
		this.basePlayer = basePlayer;

		File f = new File(Directories.getPlayerDataDir(basePlayer.getName()) + "player.yml");

		if (!f.exists())
			try {
				f.getParentFile().mkdirs();
				f.createNewFile();
			} catch (IOException e) {
				SolusRpg.log(Level.SEVERE, "Could not create player data at '%s'; please make sure the directory is writeable", f.getPath());

				if (Configuration.is("logging.verbose"))
					e.printStackTrace();

				Phrase kickPhrase = null;

				if (LanguageManager.has("system.player-init.kick-data-dir-not-writeable"))
					kickPhrase = LanguageManager.get("system.player-init.kick-data-dir-not-writeable").reset();

				basePlayer.kickPlayer(kickPhrase != null ? phrase.asText() : "");
			}

		FileConfiguration conf = YamlConfiguration.load(f);

		if (conf.isConfigurationSection("metadata"))
			for (String key : conf.getConfigurationSection("metadata").getKeys(false))
				metadata.set(key, conf.get("metadata." + key));

		for (StatType type : StatType.values())
			coreStats.put(type, conf.getInt("vitals.core-stats." + type.name().toLowerCase(), 1));

		int defAuxLevel = conf.getInt("vitals.aux-stats.default", 1);

		for (AuxStat stat : AuxStat.getAllAuxStats())
			stats.put(stat.getName(), conf.getInt("vitals.aux-stats." + stat.getPathName(), defAuxLevel));

		RpgClass rpgClass = null;

		if (conf.isString("vitals.class"))
			rpgClass = RpgClass.getByFQN(Util.toQualifiedName(conf.getString("vitals.class")));

		this.rpgClass = rpgClass;
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

		if (scaler == null)
			scaler = ExperienceScaler.getGlobalScaler();

		this.setRealExp(scaler.toRealExp(this.getLevel(), exp));

		return this;
	}

	public int getLevelCost() {
		ExperienceScaler scaler = this.getExperienceScaler();

		if (scaler == null)
			scaler = ExperienceScaler.getGlobalScaler();

		return scaler.getLevelCost(this.getLevel());
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
		if (permits.containsKey(action))
			return permits.get(action).contains(material);

		return false;
	}

	public RpgPlayer addAllowed(RpgActionType action, Material material) {
		if (!this.isAllowed(action, material)) // if this material isn't already permitted
			permits.get(action).add(material);

		return this;
	}

	public RpgPlayer addAllowed(RpgActionType action, Collection<Material> materials) {
		if (permits.containsKey(action)) // I _was_ going to just call RpgPlayer#isAllowed()... but then I realized it was pointless to repeat the permits#containsKey call in the loop
			for (Material material : materials)
				if (!permits.get(action).contains(material))
					permits.get(action).add(material);

		return this;
	}

	public RpgPlayer removeAllowed(RpgActionType action, Material material) {
		if (permits.containsKey(action))
			permits.get(action).remove(material);

		return this;
	}

	public RpgPlayer removeAllowed(RpgActionType action, Collection<Material> materials) {
		if (permits.containsKey(action))
			for (Material material : materials)
				permits.get(action).remove(material);

		return this;
	}

	public ExperienceScaler getExperienceScaler() {
		return this.expScaler;
	}

	public RpgPlayer setExperienceScaler(ExperienceScaler expScaler) {
		this.expScaler = expScaler;

		return this;
	}

	public boolean hasExperienceScaler() {
		return this.expScaler != null;
	}

	public RpgPlayer removeExperienceScaler() {
		this.setExperienceScaler(null);

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