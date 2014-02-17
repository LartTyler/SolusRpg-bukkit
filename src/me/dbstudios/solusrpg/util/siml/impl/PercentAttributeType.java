package me.dbstudios.solusrpg.util.siml.impl;

import java.util.regex.Pattern;

import me.dbstudios.solusrpg.util.siml.AttributeType;

public class PercentAttributeType implements AttributeType<Double> {
	private static final Pattern pattern = Pattern.compile("^\d+(\.\d+)?%$");

	public boolean test(String value) {
		if (!value.endsWith("%"))
			return false;

		try {
			Double.valueOf(value.substring(0, value.length() - 2));

			return true;
		} catch (Exception e) {}

		return false;
	}

	public Double convertFromString(String value) {
		try {
			return Double.valueOf(value);
		} catch (Exception e) {
			throw new IllegalArgumentException(String.format("'%s' is not a valid double!", value));
		}
	}

	public String convertToString(Object value) {
		if (!(value instanceof Double))
			throw new IllegalArgumentException(String.format("'%s' is not a Double!", value));

		return value + "%";
	}
}