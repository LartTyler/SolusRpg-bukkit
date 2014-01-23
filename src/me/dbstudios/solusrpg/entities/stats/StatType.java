package me.dbstudios.solusrpg.entities.stats;

public enum StatType {
	STR("strength"),
	CON("constitution"),
	DEX("dexterity"),
	INT("intelligence"),
	WIS("wisdom"),
	CHR("charisma");

	private final String name;

	private StatType(String name) {
		this.name = name;
	}

	public String toString() {
		return this.name;
	}

	public static StatType fromAbbreviation(String abbr) {
		for (StatType t : StatType.values())
			if (t.name().equalsIgnoreCase(abbr))
				return t;

		return null;
	}

	public static StatType fromFullName(String name) {
		for (StatType t : StatType.values())
			if (t.toString().equalsIgnoreCase(name))
				return t;

		return null;
	}

	public static StatType fromNameFuzzyMatch(String fuzzy) {
		for (StatType t : StatType.values())
			if (Util.fuzzyMatches(fuzzy, t.toString()))
				return t;

		return null;
	}
}