package me.dbstudios.solusrpg.gui.converters;

import java.util.List;
import java.util.logging.Level;

import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.config.Configuration;
import me.dbstudios.solusrpg.gui.Converter;
import me.dbstudios.solusrpg.gui.widgets.BasicWidget;
import me.dbstudios.solusrpg.util.siml.Attribute;
import me.dbstudios.solusrpg.util.siml.Element;
import me.dbstudios.solusrpg.util.siml.impl.IntegerAttributeType;
import me.dbstudios.solusrpg.util.siml.impl.ListAttributeType;
import me.dbstudios.solusrpg.util.siml.impl.PercentAttributeType;
import me.dbstudios.solusrpg.util.siml.impl.StringAttributeType;

import org.getspout.spoutapi.gui.Container;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.Screen;
import org.getspout.spoutapi.gui.Widget;
import org.getspout.spoutapi.gui.WidgetAnchor;

public class BasicConverter implements Converter<Widget> {
	public Widget convert(Element element, Widget parent) {
		Widget widget = new BasicWidget();
		int parentWidth = parent.hasSize() ? parent.getMaxWidth() : 427;
		int parentHeight = parent.hasSize() ? parent.getMaxHeight() : 0;

		if (parent != null)
			if (parent instanceof Container)
				((Container)parent).addChild(widget);
			else if (parent instanceof Screen)
				((Screen)parent).attachWidget(((Screen)parent).getPlugin(), widget);

		if (element.hasAttribute("x")) {
			Attribute attr = element.getAttribute("x");

			if (attr.getType() instanceof IntegerAttributeType)
				widget.setX((int)attr.getValue());
			else if (attr.getType() instanceof PercentAttributeType)
				widget.setX((int)Math.round((double)parentWidth * ((double)attr.getValue() / 100.0)));
			else if (Configuration.is("logging.verbose"))
				SolusRpg.log(Level.WARNING, String.format("'%s' is not valid for attribute 'x'.", attr.getValue()));
		}

		if (element.hasAttribute("y")) {
			Attribute attr = element.getAttribute("y");

			if (attr.getType() instanceof IntegerAttributeType)
				widget.setY((int)attr.getValue());
			else if (attr.getType() instanceof PercentAttributeType)
				widget.setY((int)Math.round((double)parentHeight * ((double)attr.getValue() / 100.0)));
			else if (Configuration.is("logging.verbose"))
				SolusRpg.log(Level.WARNING, String.format("'%s' is not valid for attribute 'y'.", attr.getValue()));
		}

		if (element.getTagName().equals("root"))
			widget.setWidth(427);
		else if (element.hasAttribute("width")) {
			Attribute attr = element.getAttribute("width");

			if (attr.getType() instanceof IntegerAttributeType)
				widget.setWidth((int)attr.getValue());
			else if (attr.getType() instanceof PercentAttributeType)
				widget.setWidth((int)Math.round((double)parentWidth * ((double)attr.getValue() / 100.0)));
			else if (Configuration.is("logging.verbose"))
				SolusRpg.log(Level.WARNING, String.format("'%s' is not valid for attribute 'width'.", attr.getValue()));
		}

		if (element.hasAttribute("height")) {
			Attribute attr = element.getAttribute("height");

			if (attr.getType() instanceof IntegerAttributeType)
				widget.setHeight((int)attr.getValue());
			else if (attr.getType() instanceof PercentAttributeType)
				widget.setHeight((int)Math.round((double)parentHeight * ((double)attr.getValue() / 100.0)));
			else if (Configuration.is("logging.verbose"))
				SolusRpg.log(Level.WARNING, String.format("'%s' is not valid for attribute 'height'.", attr.getValue()));
		}

		if (element.hasAttribute("priority")) {
			Attribute attr = element.getAttribute("priority");

			if (attr.getType() instanceof StringAttributeType)
				try {
					widget.setPriority(RenderPriority.valueOf((String)attr.getValue()));
				} catch (Exception e) {
					if (Configuration.is("logging.verbose")) {
						SolusRpg.log(Level.WARNING, String.format("'%s' is not valid for attribute 'priority'.", attr.getValue()));

						e.printStackTrace();
					}
				}
		}

		if (element.hasAttribute("anchor")) {
			Attribute attr = element.getAttribute("anchor");

			if (attr.getType() instanceof StringAttributeType)
				try {
					widget.setAnchor(WidgetAnchor.valueOf((String)attr.getValue()));
				} catch (Exception e) {
					if (Configuration.is("logging.verbose")) {
						SolusRpg.log(Level.WARNING, String.format("'%s' is not valid for attribute 'anchor'.", attr.getValue()));

						e.printStackTrace();
					}
				}
		}

		if (element.hasAttribute("tooltip")) {
			Attribute attr = element.getAttribute("tooltip");

			if (attr.getType() instanceof StringAttributeType)
				widget.setTooltip((String)attr.getValue());
		}

		if (element.hasAttribute("margin")) {
			Attribute attr = element.getAttribute("margin");

			if (attr.getType() instanceof ListAttributeType) {
				List<String> list = (List<String>)attr.getValue();

				if (list.size() == 1) {
					Double d = this.toDouble(list.get(0));

					if (d != null)
						if (list.get(0).endsWith("%"))
							widget.setMargin(
								(int)Math.round((double)parentHeight * (d / 100.0)),
								(int)Math.round((double)parentWidth * (d / 100.0))
							);
						else
							widget.setMargin((int)Math.round(d));
				} else if (list.size() == 2) {
					Double mTB = this.toDouble(list.get(0));
					Double mRL = this.toDouble(list.get(1));

					if (mTB != null && mRL != null) {
						if (list.get(0).endsWith("%"))
							mTB = (double)parentHeight * (mTB / 100.0);

						if (list.get(1).endsWith("%"))
							mRL = (double)parentWidth * (mRL / 100.0);

						widget.setMargin((int)Math.round(mTB), (int)Math.round(mRL));
					}
				} else if (list.size() == 3) {
					Double mT = this.toDouble(list.get(0));
					Double mRL = this.toDouble(list.get(1));
					Double mB = this.toDouble(list.get(2));

					if (mT != null && mRL != null && mB != null) {
						if (list.get(0).endsWith("%"))
							mT = (double)parentHeight * (mT / 100.0);

						if (list.get(1).endsWith("%"))
							mRL = (double)parentWidth * (mRL / 100.0);

						if (list.get(2).endsWith("%"))
							mB = (double)parentHeight * (mB / 100.0);

						widget.setMargin((int)Math.round(mT), (int)Math.round(mRL), (int)Math.round(mB));
					}
				} else if (list.size() == 4) {
					Double mT = this.toDouble(list.get(0));
					Double mR = this.toDouble(list.get(1));
					Double mB = this.toDouble(list.get(2));
					Double mL = this.toDouble(list.get(3));

					if (mT != null && mR != null && mB != null && mL != null) {
						if (list.get(0).endsWith("%"))
							mT = (double)parentHeight * (mT / 100.0);

						if (list.get(1).endsWith("%"))
							mR = (double)parentWidth * (mR / 100.0);

						if (list.get(2).endsWith("%"))
							mB = (double)parentHeight * (mB / 100.0);

						if (list.get(3).endsWith("%"))
							mL = (double)parentHeight * (mL / 100.0);

						widget.setMargin((int)Math.round(mT), (int)Math.round(mR), (int)Math.round(mB), (int)Math.round(mL));
					}
				}
			}
		}

		return widget;
	}

	private Double toDouble(String s) {
		try {
			if (s.endsWith("%"))
				s = s.substring(0, s.length() - 1);

			return Double.valueOf(s);
		} catch(Exception e) {
			return null;
		}
	}
}