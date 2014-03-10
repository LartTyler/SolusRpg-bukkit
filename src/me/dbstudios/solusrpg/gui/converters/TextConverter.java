package me.dbstudios.solusrpg.gui.converters;

import me.dbstudios.solusrpg.util.siml.Element;

import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.Label;

public class TextConverter implements Converter<Label> {
	public Label convert(Element element) {
		Label text = WidgetConverter.copyProperties(WidgetConverter.getConverter("widget").convert(element), new GenericLabel());

		return text.setText(element.isPlainText() ? element.getText() : "null");
	}
}