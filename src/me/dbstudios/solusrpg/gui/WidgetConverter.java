package me.dbstudios.solusrpg.gui;

import java.util.HashMap;
import java.util.Map;

import me.dbstudios.solusrpg.util.siml.Element;

import org.getspout.spoutapi.gui.Container;
import org.getspout.spoutapi.gui.Widget;

public class WidgetConverter {
	private static Map<String, Converter<? extends Widget>> converters = new HashMap<>();

	static {
		WidgetConverter.registerConverter("widget", new GenericConverter());
		WidgetConverter.registerConverter("container", new ContainerConverter());
		WidgetConverter.registerConverter(null, new TextConverter());
	}

	public static Widget convertTree(Element el) {
		Widget widget = WidgetConverter.convert(el);

		if (el.hasChildren() && widget instanceof Container)
			for (Element child : el.getChildren())
				((Container)widget).addChild(WidgetConverter.convertTree(child));

		return widget;
	}

	public static Widget convert(Element element) {
		if (converters.containsKey(element.getTagName()))
			return converters.get(element.getTagName()).convert(element);

		return converters.get(null).convert(element);
	}

	/**
	 * Registers a new {@link Converter} to be used to translate {@link Element} objects into
	 * {@link Widget} objects.
	 *
	 * The special value <code>null</code> as the tag name is used to denote the default
	 * converter. If a tag is not handled by any previous converters, the converter
	 * present at <code>null</code> is expected to be able to emit a well-formed {@link Widget}.
	 *
	 * @param tag       the tag that the given {@link Converter} is capable of converting
	 * @param converter the new {@link Converter}
	 */
	public static void registerConverter(String tag, Converter converter) {
		converters.put(tag, converter);
	}

	public static Converter<? extends Widget> getConverter(String tag) {
		return converters.get(tag);
	}
}