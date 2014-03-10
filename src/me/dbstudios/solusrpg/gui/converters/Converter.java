package me.dbstudios.solusrpg.gui.converters;

import me.dbstudios.solusrpg.util.siml.Element;

import org.getspout.spoutapi.gui.Widget;

public interface Converter<T extends Widget> {
	public T convert(Element element);
	// public Element convert(Widget widget);
}