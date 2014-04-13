package me.dbstudios.solusrpg.gui.converters;

import me.dbstudios.solusrpg.gui.widgets.LineBreakWidget;
import me.dbstudios.solusrpg.util.siml.Element;

public class LineBreakConverter implements Converter<LineBreakWidget> {
	public LineBreakWidget convert(Element element) {
		return new LineBreakWidget();
	}
}