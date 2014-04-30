package me.dbstudios.solusrpg.gui.converters;

import me.dbstudios.solusrpg.gui.Converter;
import me.dbstudios.solusrpg.gui.widgets.LineBreakWidget;
import me.dbstudios.solusrpg.util.siml.Element;

import org.getspout.spoutapi.gui.Container;
import org.getspout.spoutapi.gui.Screen;
import org.getspout.spoutapi.gui.Widget;

public class LineBreakConverter implements Converter<LineBreakWidget> {
	public LineBreakWidget convert(Element element, Widget parent) {
		LineBreakWidget lb = new LineBreakWidget();

		if (parent != null)
			if (parent instanceof Container)
				((Container)parent).addChild(lb);
			else if (parent instanceof Screen)
				((Screen)parent).attachWidget(((Screen)parent).getPlugin(), lb);

		return lb;
	}
}