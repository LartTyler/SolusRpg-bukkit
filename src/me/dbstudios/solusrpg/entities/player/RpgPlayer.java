package me.dbstudios.solusrpg.entities.player;

import java.util.Collection;

import me.dbstudios.solusrpg.entities.RpgClass;
import me.dbstudios.solusrpg.entities.stats.AuxStat;
import me.dbstudios.solusrpg.entities.stats.StatType;
import me.dbstudios.solusrpg.events.player.RpgActionType;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public interface RpgPlayer {
	public Player getBasePlayer();
	public String getName();
	public String getDisplayName();
	public int getLevel();
	public RpgPlayer setLevel(int level);
	public int getExp();
	public RpgPlayer setExp(int exp);
	public float getRealExp();
	public RpgPlayer setRealExp(float exp);
	public int getStatLevel(AuxStat stat);
	public int getStatLevel(String fqn);
	public int getStatLevel(StatType type);
	public RpgPlayer setStatLevel(AuxStat stat, int level);
	public RpgPlayer setStatLevel(String fqn, int level);
	public RpgPlayer setStatLevel(StatType type, int level);
	public boolean isAllowed(RpgActionType action, Material material);
	public RpgPlayer addAllowed(RpgActionType action, Material material);
	public RpgPlayer addAllowed(RpgActionType action, Collection<Material> material);
	public RpgPlayer removeAllowed(RpgActionType action, Material material);
	public RpgPlayer removeAllowed(RpgActionType action, Collection<Material> material);
	public ExperienceScaler getExperienceScaler();
	public RpgPlayer setExperienceScaler();
	public boolean hasExperienceScaler();
	public RpgClass getRpgClass();
}