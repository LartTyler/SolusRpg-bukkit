package me.dbstudios.solusrpg.gui;

import me.dbstudios.solusrpg.gui.RpgPopup;
import me.dbstudios.solusrpg.gui.popups.WelcomePopup;

public enum RpgPopupType {
	WELCOME("Welcome", "WelcomeUI", WelcomePopup.class);

	private final String name;
	private final String fqn;
	private final Class<? extends RpgPopup> type;

	private RpgPopupType(String name, String fqn, Class<? extends RpgPopup> type) {
		this.name = name;
		this.fqn = fqn;
		this.type = type;
	}

	public String getName() {
		return this.name;
	}

	public String getQualifiedName() {
		return this.fqn;
	}

	public Class<? extends RpgPopup> getPopupClass() {
		return this.type;
	}
}