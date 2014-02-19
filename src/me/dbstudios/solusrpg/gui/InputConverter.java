package me.dbstudios.solusrpg.gui;

import java.util.HashMap;
import java.util.Map;

import org.getspout.spoutapi.gui.Control;

public class InputConverter implements Converter<? extends Control> {
	private static final Map<String, InputConverter> typeConverters = new HashMap<>();

	static {
		typeConverters.put("radio", new RadioInputConverter());
	}

	public Control convert(Element element) {
		String type = null;

		if (!element.hasAttribute("type"))
			type = "text";
		else
			type = element.getAttribute("type").getValue().toString();

		if (!typeConverters.containsKey(type))
			throw new IllegalArgumentException("No input type converter exists with the name '" + type + "'.");

		return typeConverters.get(type).convert(element);
	}
}