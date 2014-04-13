package me.dbstudios.solusrpg.gui.converters;

import me.dbstudios.solusrpg.util.siml.Element;

import org.getspout.spoutapi.gui.GenericTextField;
import org.getspout.spoutapi.gui.TextField;

public class TextInputConverter implements Converter<TextField> {
	public TextField convert(Element element) {
		TextField text = WidgetConverter.copyProperties(WidgetConverter.getConverter("widget").convert(element), new GenericTextField());

		return text;
	}
}