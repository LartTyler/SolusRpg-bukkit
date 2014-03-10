package me.dbstudios.solusrpg.gui.widgets;

import me.dbstudios.solusrpg.gui.popups.SolusPopup;

import org.getspout.spoutapi.event.screen.ButtonClickEvent;
import org.getspout.spoutapi.gui.GenericButton;

public class SolusButton extends GenericButton {
	private SolusPopup parent;
	private String name;

	public SolusButton() {
		this(null);
	}

	public SolusButton(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public SolusButton setName(String name) {
		this.name = name;

		return this;
	}

	public SolusPopup getParent() {
		return this.parent;
	}

	public SolusButton setParent(SolusPopup parent) {
		return this.setScreen(parent);
	}

	public SolusButton setScreen(Screen screen) {
		super.setScreen(screen);

		if (screen instanceof SolusPopup)
			this.setParent((SolusPopup)screen);

		return this;
	}

	public void onButtonClick(ButtonClickEvent ev) {
		this.getParent().onAction("click", this);
	}
}