package me.dbstudios.solusrpg.util.siml.impl;

import me.dbstudios.solusrpg.util.siml.AttributeType;

public class HexidecimalAttributeType implements AttributeType<Integer> {
	public boolean test(String value) {
		if (!value.startsWith("#"))
			return false;

		try {
			Integer.parseInt(value.substring(1), 16);

			return true;
		} catch (Exception e) {}

		return false;
	}

	public Integer convertFromString(String value) {
		try {
			return Integer.parseInt(value.substring(1), 16);
		} catch (Exception e) {
			throw new IllegalArgumentException(String.format("'%s' is not a valid hexidecimal!", value));
		}
	}

	public String convertToString(Object value) {
		if (!(value instanceof Integer))
			throw new IllegalArgumentException(String.format("'%s' is not a valid integer!", value));

		return "#" + Integer.toString((Integer)value, 16);
	}
}