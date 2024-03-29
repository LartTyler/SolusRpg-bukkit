package me.dbstudios.solusrpg.gui.converters;

import me.dbstudios.solusrpg.gui.Converter;
import me.dbstudios.solusrpg.gui.SimlConverter;
import me.dbstudios.solusrpg.util.siml.Element;

import org.getspout.spoutapi.gui.GenericRadioButton;
import org.getspout.spoutapi.gui.RadioButton;
import org.getspout.spoutapi.gui.Widget;

public class RadioInputConverter implements Converter<RadioButton> {
	public RadioButton convert(Element element, Widget parent) {
		RadioButton radio = SimlConverter.copyProperties(SimlConverter.getConverter(null).convert(element, parent), new GenericRadioButton());

		if (element.hasAttribute("name"))
			radio.setGroup(element.getAttribute("name").getValue().toString().hashCode());

		if (element.hasAttribute("value"))
			radio.setText(element.getAttribute("value").getValue().toString());

		return radio;
	}
}