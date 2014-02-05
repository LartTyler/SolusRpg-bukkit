package me.dbstudios.solusrpg.entities.player;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.config.Configuration;
import me.dbstudios.solusrpg.config.Directories;
import me.dbstudios.solusrpg.config.Metadata;
import me.dbstudios.solusrpg.entities.RpgClass;
import me.dbstudios.solusrpg.entities.stats.AuxStat;
import me.dbstudios.solusrpg.entities.stats.StatScaler;
import me.dbstudios.solusrpg.entities.stats.StatType;
import me.dbstudios.solusrpg.events.player.RpgActionType;
import me.dbstudios.solusrpg.language.LanguageManager;
import me.dbstudios.solusrpg.language.Phrase;
import me.dbstudios.solusrpg.util.Util;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class SimpleRpgPlayer implements RpgPlayer {
	private static final Map<UUID, RpgPlayer> players = new HashMap<>();

	private final Map<RpgActionType, Set<Material>> permits = new EnumMap<>(RpgActionType.class);
	private final Map<StatType, Integer> coreStats = new EnumMap<>(StatType.class);
	private final Set<PlayerModifier> modifiers = new HashSet<>();
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

				basePlayer.kickPlayer(kickPhrase != null ? kickPhrase.asText() : "");
			}

		FileConfiguration conf = YamlConfiguration.loadConfiguration(f);

		if (conf.isConfigurationSection("metadata"))
			for (String key : conf.getConfigurationSection("metadata").getKeys(false))
				metadata.set(key, conf.get("metadata." + key));

		for (StatType type : StatType.values())
			coreStats.put(type, conf.getInt("vitals.core-stats." + type.name().toLowerCase(), 1));

		for (AuxStat stat : AuxStat.getAllAuxStats())
			stats.put(stat.getName(), conf.getInt("vitals.aux-stats." + stat.getPathName(), 1));

		RpgClass rpgClass = null;

		if (conf.isString("vitals.class"))
			rpgClass = RpgClass.getByFQN(Util.toQualifiedName(conf.getString("vitals.class")));

		this.rpgClass = rpgClass;

		for (RpgActionType action : RpgActionType.values()) {
			Set<Material> p =  EnumSet.noneOf(Material.class);

			for (String m : conf.getStringList("permit." + action.name().toLowerCase())) {
				Material material = Material.matchMaterial(m);

				if (material != null && !p.contains(material))
					p.add(material);
				else if (material == null)
					SolusRpg.log(Level.WARNING, String.format("Invalid material name '%s' found in permit.%s node while loading %s.", m, action.name().toLowerCase(), this.getName()));
				else
					SolusRpg.log(Level.WARNING, String.format("Duplicate material found with name '%s' while loading %s.", material.name(), this.getName()));
			}

			permits.put(action, p);
		}
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

	public int getRealStatLevel(AuxStat stat) {
		return this.getRealStatLevel(stat.getName());
	}

	public int getRealStatLevel(String fqn) {
		return stats.get(fqn);
	}

	public int getRealStatLevel(StatType type) {
		return coreStats.get(type);
	}

	public int getStatLevel(AuxStat stat) {
		int level = this.getRealStatLevel(stat);

		for (StatScaler scaler : stat.getStatScalers())
			if (scaler.isCoreStatScaler())
				level += scaler.getBonus(coreStats.get(scaler.getCoreStatType()));
			else
				level += scaler.getBonus(this.getRealStatLevel(scaler.getIdentifier()));

		return level;
	}

	public int getStatLevel(String fqn) {
		return this.getStatLevel(AuxStat.getByFQN(fqn));
	}

	public int getStatLevel(StatType type) {
		return coreStats.get(type);
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

	public RpgPlayer addModifier(PlayerModifier modifier) {
		if (!modifiers.contains(modifier)) {
			modifiers.add(modifier);
			modifier.modify(this);
		}

		return this;
	}

	public RpgPlayer removeModifier(PlayerModifier modifier) {
		if (modifiers.contains(modifier))
			modifiers.remove(modifier);

		return this;
	}

	public RpgPlayer cleanModifier(PlayerModifier modifier) {
		if (modifiers.contains(modifier)) {
			modifier.unmodify(this);

			this.removeModifier(modifier);
		}

		return this;
	}

	public Set<PlayerModifier> getModifiers() {
		return Collections.unmodifiableSet(this.modifiers);
	}

	public boolean isAllowed(RpgActionType action, Material material) {
		return permits.get(action).contains(material) || rpgClass.isAllowed(action, material);
	}

	public RpgPlayer addAllowed(RpgActionType action, Material material) {
		if (!this.isAllowed(action, material)) // if this material isn't already permitted
			permits.get(action).add(material);

		return this;
	}

	public RpgPlayer addAllowed(RpgActionType action, Collection<Material> materials) {
		for (Material material : materials)
			if (!this.isAllowed(action, material))
				permits.get(action).add(material);

		return this;
	}

	public RpgPlayer removeAllowed(RpgActionType action, Material material) {
		permits.get(action).remove(material);

		return this;
	}

	public RpgPlayer removeAllowed(RpgActionType action, Collection<Material> materials) {
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

	public RpgClass getRpgClass() {
		return this.rpgClass;
	}

	public RpgPlayer save() {
		File f = new File(Directories.getPlayerDataDir(this.getName()) + "player.yml");
		FileConfiguration conf = YamlConfiguration.loadConfiguration(f);

		for (String key : metadata.keySet())
			conf.set("metadata." + key, metadata.get(key));

		for (StatType type : StatType.values())
			conf.set("vitals.core-stats." + type.name().toLowerCase(), coreStats.get(type));

		for (AuxStat stat : AuxStat.getAllAuxStats())
			conf.set("vitals.aux-stats." + stat.getPathName(), stats.get(stat.getName()));

		conf.set("vitals.class", rpgClass.getName());

		try {
			conf.save(f);
		} catch (IOException e) {
			SolusRpg.log(Level.SEVERE, String.format("Could not save player data for %s; any changes made during this session will be lost", this.getName()));

			if (Configuration.is("logging.verbose"))
				e.printStackTrace();
		}

		return this;
	}

	public static RpgPlayer getOrCreate(Player basePlayer) {
		if (players.containsKey(basePlayer.getUniqueId()))
			return players.get(basePlayer.getUniqueId());

		RpgPlayer player = new SimpleRpgPlayer(basePlayer);
		players.put(basePlayer.getUniqueId(), player);

		return player;
	}

	public static void destroy(RpgPlayer player) {
		player.save();

		players.remove(player.getBasePlayer().getUniqueId());
	}
}