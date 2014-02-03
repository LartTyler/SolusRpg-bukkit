package me.dbstudios.solusrpg.entities.stats;

public final class CoreStat {
	private final StatType type;
	private int value;

	public CoreStat(StatType type, Integer value) {
		this.value = value;
		this.type = type;
	}

	public StatType getType() {
		return this.type;
	}

	public int getValue() {
		return this.value;
	}
}