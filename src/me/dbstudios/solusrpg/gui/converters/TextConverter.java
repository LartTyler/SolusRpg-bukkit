package me.dbstudios.solusrpg.gui.converters;

import java.util.logging.Level;

import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.gui.Converter;
import me.dbstudios.solusrpg.gui.SimlConverter;
import me.dbstudios.solusrpg.util.siml.Attribute;
import me.dbstudios.solusrpg.util.siml.Element;
import me.dbstudios.solusrpg.util.siml.impl.PercentAttributeType;

import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.Label;
import org.getspout.spoutapi.gui.Widget;

public class TextConverter implements Converter<Label> {
	public Label convert(Element element, Widget parent) {
		Label label = SimlConverter.copyProperties(SimlConverter.getConverter(null).convert(element, parent), new GenericLabel());

		if (element.hasAttribute("scale")) {
			Attribute attr = element.getAttribute("scale");

			if (attr.getType() instanceof PercentAttributeType)
				label.setScale(1.0 * ((double)attr.getValue() / 100.0))
			else if (Configuration.is("logging.verbose"))
				SolusRpg.log(Level.WARNING, String.format("'%s' is not valid for attribute 'scale'.", attr.getValue()));
		}

		StringBuilder text = new StringBuilder();
		char[] chars = element.getText().toCharArray();
		int parentWidth = parent.hasSize() ? parent.getMaxWidth() : 427;

		for (int pos = 0; pos < chars.length; pos++) {
			char ch = chars[pos];

			if (GenericLabel.getStringWidth(text.toString() + Character.toString(ch), label.getScale()) <= parentWidth)
				text.append(ch);
			else
				text
					.append("\n")
					.append(ch);
		}

		label.setText(text.toString());

		return label;
	}
}