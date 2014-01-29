package me.dbstudios.solusrpg.entities.stats;

import java.util.logging.Level;

import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.exceptions.CreationException;
import me.dbstudios.solusrpg.util.Util;

public class StatScaler {
	private final int ratioX;
	private final int ratioY;
	private final int ratioZ;

	private StatType coreStatType = null;
	private String identifier = null;

	public StatScaler(StatType coreType, String scaling) {
		this(scaling);

		this.coreStatType = coreType;
		this.identifier = coreType.name();
	}

	public StatScaler(String identifier, String scaling) {
		this(scaling);

		this.identifier = Util.toQualifiedName(identifier, "Stat");
	}

	private StatScaler(String scaling) {
		String[] parts = scaling.split(":");

		if (parts.length < 2) {
			SolusRpg.log(Level.WARNING, String.format("I encountered an error while initialzing a StatScaler; '%s' with identifier '%s' is an invalid ratio.", scaling, identifier));

			throw CreationException("Invalid scaler ratio");
		}

		this.ratioX = parts[0];
		this.ratioY = parts[1];
		this.ratioZ = parts.length >= 3 ? parts[2] : 0;
	}

	public boolean isCoreStatScaler() {
		return this.coreStatType != null;
	}

	public StatType getCoreStatType() {
		return this.coreStatType;
	}

	public String getIdentitifer() {
		return this.identifier;
	}

	public int getBonus(int statLevel) {
		int bonus = Math.floor(statLevel / this.ratioX) * ratioY;

		if (this.getBonusCap() > 0)
			return Math.max(bonus, this.ratioZ);

		return bonus;
	}

	public int getBonusCap() {
		return this.ratioZ;
	}
}