package me.dbstudios.solusrpg;

public class RpgPopupFactory {
	private static final Map<String, RpgPopup> popups = new HashMap<>();

	protected static void registerPopup(String name, RpgPopup popup) {
		popups.put(name, popup);
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