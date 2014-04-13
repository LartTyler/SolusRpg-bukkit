package me.dbstudios.solusrpg.gui.converters;

import me.dbstudios.solusrpg.gui.widgets.SolusButton;
import me.dbstudios.solusrpg.util.siml.Element;

import org.getspout.spoutapi.gui.Button;

public class ButtonConverter implements Converter<Button> {
	public Button convert(Element element) {
		SolusButton button = new SolusButton();

		if (element.hasAttribute("name"))
			button.setName(element.getAttributeValue("name").toString());

		if (element.hasAttribute("value"))
			button.setText(element.getAttributeValue("value").toString());

		return button;
	}
}