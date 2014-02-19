package me.dbstudios.solusrpg.gui;

import me.dbstudios.solusrpg.config.Configuration;
import me.dbstudios.solusrpg.util.siml.Attribute;
import me.dbstudios.solusrpg.util.siml.impl.StringAttributeType;

import org.getspout.spoutapi.gui.ContainerType;
import org.getspout.spoutapi.gui.GenericContainer;
import org.getspout.spoutapi.gui.Widget;
import org.getspout.spoutapi.gui.WidgetAnchor;

public class ContainerConverter implements Converter<Container> {
	public Container convert(Element element) {
		Widget w = WidgetConverter.getConverter("widget");
		Container container = new GenericContainer();

		if (w.hasPosition())
			container
				.setX(w.getX())
				.setY(w.getY());

		if (w.hasSize())
			container
				.setWidth(w.getWidth())
				.setHeight(w.getHeight());

		container
			.setPriority(w.getPriority())
			.setAnchor(w.getAnchor())
			.setTooltip(w.getTooltip())

		container
			.setMarginTop(w.getMarginTop())
			.setMarginRight(w.getMarginRight())
			.setMarginBottom(w.getMarginBottom())
			.setMarginLeft(w.getMarginLeft());

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
					container.setLayout(WidgetAnchor.valueOf((String)attr.getValue());
				} catch (Exception e) {
					if (Configuration.is("logging.verbose")) {
						SolusRpg.log(Level.WARNING, String.format("'%s' is not a valid value for the 'align' attribute.", attr.getValue()));

						e.printStackTrace();
					}
				}
		}

		return container;
	}
}