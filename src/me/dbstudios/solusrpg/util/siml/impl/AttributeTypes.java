package me.dbstudios.solusrpg.util.siml.impl;

public enum AttributeTypes {
	INTEGER(new IntegerAttributeType()),
	PERCENT(new PercentAttributeType()),
	HEXIDECIMAL(new HexidecimalAttributeType()),
	COLOR(new ColorAttributeType()),
	STRING(new StringAttributeType());

	private final AttributeType<?> type;

	private AttributeTypes(AttributeType<?> type) {
		this.type = type;
	}

	public AttributeType<?> getType() {
		return this.type;
	}

	public boolean test(String value) {
		return type.test(value);
	}

	public boolean isSameType(AttributeType<?> type) {
		return this.type.getClass().isInstance(type);
	}
}