package me.dbstudios.solusrpg.gui;

import me.dbstudios.solusrpg.util.siml.Element;

public class LineBreakConverter implements Converter<LineBreakWidget> {
	public LineBreakWidget convert(Element element) {
		return new LineBreakWidget();
	}
}