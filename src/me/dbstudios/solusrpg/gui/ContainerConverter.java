package me.dbstudios.solusrpg.gui;

import java.util.logging.Level;

import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.config.Configuration;
import me.dbstudios.solusrpg.util.siml.Attribute;
import me.dbstudios.solusrpg.util.siml.Element;
import me.dbstudios.solusrpg.util.siml.impl.StringAttributeType;

import org.getspout.spoutapi.gui.Container;
import org.getspout.spoutapi.gui.ContainerType;
import org.getspout.spoutapi.gui.GenericContainer;
import org.getspout.spoutapi.gui.WidgetAnchor;

public class ContainerConverter implements Converter<Container> {
	public Container convert(Element element) {
		Container container = WidgetConverter.copyProperties(WidgetConverter.getConverter("widget").convert(element), new GenericContainer());

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
				container.addChild(WidgetConverter.convert(child));

		return container;
	}
}