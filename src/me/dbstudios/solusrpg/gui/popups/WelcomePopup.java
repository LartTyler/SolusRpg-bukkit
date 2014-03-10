package me.dbstudios.solusrpg.gui.popups;

import me.dbstudios.solusrpg.gui.widgets.SolusButton;

import org.getspout.spoutapi.gui.Widget;

public class WelcomePopup extends SolusPopup {
	public void onAction(String action, Widget sender) {
		switch (action.toLowerCase()) {
			case "click":
				if (!sender instanceof SolusButton)
					throw new UnsupportedOperationException("Click events cannot be thrown by widgets that do not inherit from Button!");

				SolusButton button = (SolusButton)sender;

				if (!button.getName().equalsIgnoreCase("submit"))
					break;
		}
	}
}