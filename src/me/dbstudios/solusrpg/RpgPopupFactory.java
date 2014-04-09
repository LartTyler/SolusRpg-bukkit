package me.dbstudios.solusrpg;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import me.dbstudios.solusrpg.gui.popups.RpgPopup;
import me.dbstudios.solusrpg.gui.popups.WelcomePopup;

public class RpgPopupFactory {
	private static final Map<String, RpgPopup> popups = new HashMap<>();

	static {
		registerPopup("Welcome", new WelcomePopup());
	}

	protected static void registerPopup(String name, RpgPopup popup) {
		popups.put(name, popup);
	}

	public static Map<String, RpgPopup> getPopups() {
		return Collections.unmodifiableMap(popups);
	}

	public static Set<String> getPopupNames() {
		return popups.keySet();
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