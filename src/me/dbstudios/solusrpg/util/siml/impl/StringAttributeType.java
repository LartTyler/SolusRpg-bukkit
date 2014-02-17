package me.dbstudios.solusrpg.util.siml.impl;

import me.dbstudios.solusrpg.util.siml.AttributeType;

public class StringAttributeType implements AttributeType<String> {
	public boolean test(String value) {
		return true;
	}

	public String convertFromString(String value) {
		return value;
	}

	public String convertToString(Object value) {
		return value.toString();
	}
}