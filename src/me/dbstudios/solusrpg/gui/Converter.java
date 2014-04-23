package me.dbstudios.solusrpg.gui;

import me.dbstudios.solusrpg.util.siml.Element;

import org.getspout.spoutapi.gui.Widget;

public interface Converter<T extends Widget> {
	public T convert(Element element Object parent);
}