package me.dbstudios.solusrpg.gui.converters;

import java.util.HashMap;
import java.util.Map;

import me.dbstudios.solusrpg.util.siml.Element;

import org.getspout.spoutapi.gui.Control;
import org.getspout.spoutapi.gui.Widget;

public class InputConverter implements Converter<Control> {
	private static final Map<String, Converter<? extends Control>> typeConverters = new HashMap<>();

	static {
		typeConverters.put("radio", new RadioInputConverter());
		typeConverters.put("text", new TextInputConverter());
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