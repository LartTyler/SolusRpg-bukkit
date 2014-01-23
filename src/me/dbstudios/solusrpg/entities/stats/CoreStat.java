package me.dbstudios.solusrpg.entities.stats;

public final class CoreStat extends Stat<Integer> {
	private final StatType type;

	public CoreStat(StatType type, Integer value) {
		super(value);

		this.type = type;
	}

	public StatType getType() {
		return this.type;
	}
}