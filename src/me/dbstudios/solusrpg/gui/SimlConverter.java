package me.dbstudios.solusrpg.gui;

import java.util.HashMap;
import java.util.Map;

import me.dbstudios.solusrpg.gui.converters.BasicConverter;
import me.dbstudios.solusrpg.gui.converters.ContainerConverter;
import me.dbstudios.solusrpg.gui.converters.InputConverter;
import me.dbstudios.solusrpg.gui.converters.LineBreakConverter;
import me.dbstudios.solusrpg.gui.converters.TextConverter;
import me.dbstudios.solusrpg.util.siml.Document;
import me.dbstudios.solusrpg.util.siml.Element;

import org.getspout.spoutapi.gui.Widget;

public class SimlConverter {
	private static Map<String, Converter<?>> converters = new HashMap<>();
	private static boolean initialized = false;

	public static void initialize() {
		if (initialized)
			return;

		registerConverter("container", new ContainerConverter());
		registerConverter("root", converters.get("container"));
		registerConverter("input", new InputConverter());
		registerConverter("br", new LineBreakConverter());
		registerConverter("text", new TextConverter());
		registerConverter(null, new BasicConverter());
	}

	public static void registerConverter(String tag, Converter<?> converter) {
		converters.put(tag, converter);
	}

	public static Converter<?> getConverter(String tag) {
		return converters.get(tag);
	}

	public static boolean hasConverter(String tag) {
		return converters.containsKey(tag);
	}

	public static void removeConverter(String tag) {
		converters.remove(tag);
	}

	public static Widget convert(Document document, RpgPopup popup) {
		// TODO
		return SimlConverter.convert(document.getRootElement());
	}

	public static Widget convert(Element element) {
		if (SimlConverter.hasConverter(element.getTagName()))
			return SimlConverter.getConverter(element.getTagName()).convert(element);

		return SimlConverter.getConverter(null).convert(element);
	}

	public static <T extends Widget> T copyProperties(Widget fromWidget, T toWidget) {
		if (fromWidget.hasPosition())
			toWidget
				.setX(fromWidget.getX())
				.setY(fromWidget.getY());

		if (fromWidget.hasSize())
			toWidget
				.setWidth(fromWidget.getWidth())
				.setHeight(fromWidget.getHeight());

		toWidget
			.setPriority(fromWidget.getPriority())
			.setAnchor(fromWidget.getAnchor())
			.setTooltip(fromWidget.getTooltip())
			.setMarginTop(fromWidget.getMarginTop())
			.setMarginRight(fromWidget.getMarginRight())
			.setMarginBottom(fromWidget.getMarginBottom())
			.setMarginLeft(fromWidget.getMarginLeft());

		return toWidget;
	}
}