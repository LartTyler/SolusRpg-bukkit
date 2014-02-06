package me.dbstudios.solusrpg.entities;

import me.dbstudios.solusrpg.entities.stats.AuxStat;
import me.dbstudios.solusrpg.entities.stats.StatType;
import me.dbstudios.solusrpg.events.player.RpgActionType;

import org.bukkit.Material;

public interface RpgClass {
	public String getName();
	public String getDisplayName();
	public String getDescription();
	public int getStatLevel(AuxStat stat);
	public int getStatLevel(String fqn);
	public int getStatLevel(StatType type);
	public boolean isAllowed(RpgActionType action, Material material);
	public int getHealth();
	public String getHealthName();
	public boolean hasHealthRegeneration();
	public double getHealthRegenRate();
	public String getHealthRegenFormula();
	public int getEnergy();
	public String getEnergyName();
	public boolean hasEnergyRegeneration();
	public double getEnergyRegenRate();
	public String getEnergyRegenFormula();
}