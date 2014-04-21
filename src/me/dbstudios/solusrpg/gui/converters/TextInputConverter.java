package me.dbstudios.solusrpg.gui.converters;

import me.dbstudios.solusrpg.gui.Converter;
import me.dbstudios.solusrpg.gui.SimlConverter;
import me.dbstudios.solusrpg.util.siml.Element;

import org.getspout.spoutapi.gui.GenericTextField;
import org.getspout.spoutapi.gui.TextField;

public class TextInputConverter implements Converter<TextField> {
	public TextField convert(Element element) {
		TextField text = SimlConverter.copyProperties(SimlConverter.getConverter(null).convert(element), new GenericTextField());

		return text;
	}
}