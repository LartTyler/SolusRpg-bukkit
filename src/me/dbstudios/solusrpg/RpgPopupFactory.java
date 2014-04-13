package me.dbstudios.solusrpg;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.gui.LayoutManager;
import me.dbstudios.solusrpg.gui.popups.RpgPopup;
import me.dbstudios.solusrpg.gui.popups.WelcomePopup;

import org.getspout.spoutapi.gui.Container;
import org.getspout.spoutapi.gui.Widget;

public class RpgPopupFactory {
	private static final Map<String, RpgPopup> popups = new HashMap<>();

	private static boolean initialized = false;

	protected static void registerPopup(String name, RpgPopup popup) {
		popups.put(name, popup);

		Widget w = popup.getAttachedWidgets()[0];

		if (w instanceof Container)
			LayoutManager.layout((Container)w);
		else if (w.hasContainer())
			LayoutManager.layout(w.getContainer());
	}

	public static void initialize() {
		if (initialized)
			return;

		long start = System.currentTimeMillis();

		registerPopup("Welcome", new WelcomePopup());

		SolusRpg.log(Level.INFO, String.format("RpgPopupFactory initialized in %d seconds with %d SIML layouts.", System.currentTimeMillis() - start, popups.size()));

		initialized = true;
	}

	public static RpgPopup getPopup(String name) {
		return RpgPopupFactory.getPopup(name, RpgPopup.class);
	}

	public static <T extends RpgPopup> T getPopup(String name, Class<T> cl) {
		RpgPopup popup = popups.get(name);

		if (cl.isInstance(popup))
			return cl.cast(popup);
		
		return null;
	}
}