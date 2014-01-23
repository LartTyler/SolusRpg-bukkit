package me.dbstudios.solusrpg.entities.player;

import me.dbstudios.solusrpg.entities.stats.AuxStat;
import me.dbstudios.solusrpg.entities.stats.StatType;

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
}