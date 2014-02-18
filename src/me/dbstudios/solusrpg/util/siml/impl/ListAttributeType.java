package me.dbstudios.solusrpg.util.siml.impl;

import java.util.Arrays;
import java.util.List;

public class ListAttributeType implements AttributeType<List<String>> {
	public boolean test(String value) {
		int count = 0;

		for (char c : value.toCharArray())
			if (count != 0)
				return true;
			else if (c == ',')
				count++;

		return false;
	}

	public List<String> convertFromString(String value) {
		String[] split = value.split(",");

		for (int i = 0; i < split.length; i++)
			split[i] = split[i].trim();

		return Arrays.asList(split);
	}

	public String convertToString(Object value) {
		if (!(value instanceof List<String>))
			throw new IllegalArgumentException(String.format("'%s' is not a valid list!", value));

		StringBuilder sb = new StringBuilder();

		for (String s : (List<String>)value)
			sb.append("," + s);

		return sb.toString().substring(1);
	}
}