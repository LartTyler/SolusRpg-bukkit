package me.dbstudios.solusrpg.gui;

public class LineBreakConverter implements Converter<LineBreakWidget> {
	public LineBreakWidget convert(Element element) {
		return new LineBreakWidget();
	}
}