package me.dbstudios.solusrpg.gui.converters;

import me.dbstudios.solusrpg.gui.Converter;
import me.dbstudios.solusrpg.gui.SimlConverter;
import me.dbstudios.solusrpg.util.siml.Element;

import org.getspout.spoutapi.gui.GenericTextField;
import org.getspout.spoutapi.gui.TextField;
import org.getspout.spoutapi.gui.Widget;

public class TextInputConverter implements Converter<TextField> {
	public TextField convert(Element element, Widget parent) {
		TextField text = SimlConverter.copyProperties(SimlConverter.getConverter(null).convert(element, parent), new GenericTextField());

		return text;
	}
}