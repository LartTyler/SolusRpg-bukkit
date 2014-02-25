package me.dbstudios.solusrpg.gui;

import java.util.HashMap;
import java.util.Map;

import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.util.siml.Element;

import org.getspout.spoutapi.gui.Container;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.PopupScreen;
import org.getspout.spoutapi.gui.Widget;

public class WidgetConverter {
	private static Map<String, Converter<? extends Widget>> converters = new HashMap<>();

	static {
		WidgetConverter.registerConverter("widget", new GenericConverter());
		WidgetConverter.registerConverter("container", new ContainerConverter());
		WidgetConverter.registerConverter("root", WidgetConverter.getConverter("container"));
		WidgetConverter.registerConverter("input", new InputConverter());
		WidgetConverter.registerConverter("br", new LineBreakConverter());
		WidgetConverter.registerConverter(null, new TextConverter());
	}

	public static Widget convert(Element element) {
		if (converters.containsKey(element.getTagName()))
			return converters.get(element.getTagName()).convert(element);

		return converters.get(null).convert(element);
	}

	public static PopupScreen createScreen(Element root) {
		return WidgetConverter.createScreen(root, false);
	}

	public static PopupScreen createScreen(Element root, boolean transparent) {
		PopupScreen screen = new GenericPopup();
		screen
			.setTransparent(transparent)
			.attachWidget(SolusRpg.getInstance(), WidgetConverter.convert(root));

		return screen;
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