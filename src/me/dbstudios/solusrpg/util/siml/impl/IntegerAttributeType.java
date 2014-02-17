package me.dbstudios.solusrpg.util.siml.impl;

import java.util.regex.Pattern;

import me.dbstudios.solusrpg.util.siml.AttributeType;

public class IntegerAttributeType implements AttributeType<Integer> {
	public boolean test(String value) {
		try {
			Integer.valueOf(value);

			return true;
		} catch (Exception e) {}

		return false;
	}

	public Integer convertFromString(String value) {
		try {
			return Integer.valueOf(value);
		} catch (Exception e) {
			throw new IllegalArgumentException(String.format("'%s' is not a valid interger!", value));
		}
	}

	public String convertToString(Object value) {
		if (!(value instanceof Integer))
			throw new IllegalArgumentException(String.format("'%s' is not a valid interger!", value));

		return value.toString();
	}
}