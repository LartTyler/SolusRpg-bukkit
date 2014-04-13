package me.dbstudios.solusrpg.gui.widgets;

import me.dbstudios.solusrpg.gui.popups.RpgPopup;

import org.getspout.spoutapi.event.screen.ButtonClickEvent;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.Screen;

public class SolusButton extends GenericButton {
	private RpgPopup parent;
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

	public RpgPopup getParent() {
		return this.parent;
	}

	public SolusButton setParent(RpgPopup parent) {
		return this.setScreen(parent);
	}

	public SolusButton setScreen(Screen screen) {
		super.setScreen(screen);

		if (screen instanceof RpgPopup)
			this.parent = (RpgPopup)parent;
		else
			this.parent = null;

		return this;
	}

	public void onButtonClick(ButtonClickEvent ev) {
		this.getParent().onAction("click", this);
	}
}