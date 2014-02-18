package me.dbstudios.solusrpg.gui;

import org.getspout.spoutapi.gui.Widget;

public interface Converter<T extends Widget> {
	public T convert(Element element);
	// public Element convert(Widget widget);
}