package me.dbstudios.solusrpg.gui.converters;

import me.dbstudios.solusrpg.gui.Converter;
import me.dbstudios.solusrpg.gui.SimlConverter;
import me.dbstudios.solusrpg.util.siml.Element;

import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.Label;

public class TextConverter implements Converter<Label> {
	public Label convert(Element element) {
		Label label = SimlConverter.copyProperties(SimlConverter.getConverter(null).convert(element), new GenericLabel());

		return label.setText(element.getText());
	}
}