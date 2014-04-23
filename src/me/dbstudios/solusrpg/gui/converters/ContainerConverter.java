package me.dbstudios.solusrpg.gui.converters;

import java.util.logging.Level;

import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.config.Configuration;
import me.dbstudios.solusrpg.gui.Converter;
import me.dbstudios.solusrpg.gui.SimlConverter;
import me.dbstudios.solusrpg.util.siml.Attribute;
import me.dbstudios.solusrpg.util.siml.Element;
import me.dbstudios.solusrpg.util.siml.impl.StringAttributeType;

import org.getspout.spoutapi.gui.Container;
import org.getspout.spoutapi.gui.ContainerType;
import org.getspout.spoutapi.gui.GenericContainer;
import org.getspout.spoutapi.gui.WidgetAnchor;

public class ContainerConverter implements Converter<Container> {
	public Container convert(Element element, Object parent) {
		Container container = SimlConverter.copyProperties(SimlConverter.getConverter(null).convert(element, parent), new GenericContainer());

		if (element.hasAttribute("type")) {
			Attribute attr = element.getAttribute("type");

			if (attr.getType() instanceof StringAttributeType)
				try {
					container.setLayout(ContainerType.valueOf((String)attr.getValue()));
				} catch (Exception e) {
					if (Configuration.is("logging.verbose")) {
						SolusRpg.log(Level.WARNING, String.format("'%s' is not a valid value for the 'type' attribute.", attr.getValue()));

						e.printStackTrace();
					}
				}
		}

		if (element.hasAttribute("align")) {
			Attribute attr = element.getAttribute("align");

			if (attr.getType() instanceof StringAttributeType)
				try {
					container.setAlign(WidgetAnchor.valueOf((String)attr.getValue()));
				} catch (Exception e) {
					if (Configuration.is("logging.verbose")) {
						SolusRpg.log(Level.WARNING, String.format("'%s' is not a valid value for the 'align' attribute.", attr.getValue()));

						e.printStackTrace();
					}
				}
		}

		if (element.hasChildren())
			for (Element child : element.getChildren())
				container.addChild(SimlConverter.convert(child));

		return container;
	}
}