package me.dbstudios.solusrpg.gui.popups;

import java.io.File;
import java.io.IOException;

import me.dbstudios.solusrpg.config.Directories;
import me.dbstudios.solusrpg.exceptions.CreationException;
import me.dbstudios.solusrpg.gui.converters.WidgetConverter;
import me.dbstudios.solusrpg.gui.widgets.SolusButton;
import me.dbstudios.solusrpg.util.siml.impl.SimlDocument;

import org.getspout.spoutapi.gui.Widget;

public class WelcomePopup extends RpgPopup {
	private Widget root = null;

	public WelcomePopup() {
		File f = new File(Directories.CONFIG_UI + "WelcomeUI.siml");

		if (!f.exists())
			throw new CreationException("Missing core UI file: WelcomeUI.siml");

		try {
			this.root = WidgetConverter.convert(SimlDocument.create(f), this);
		} catch (IOException e) {
			e.printStackTrace();

			throw new CreationException("Could not properly read from WelcomeUI.siml; please make sure the file exists and is readable.");
		}
	}

	public void onAction(String action, Widget sender) {
		switch (action.toLowerCase()) {
			case "click":
				if (!(sender instanceof SolusButton))
					throw new UnsupportedOperationException("Click events cannot be thrown by widgets that do not inherit from Button!");

				SolusButton button = (SolusButton)sender;

				if (!button.getName().equalsIgnoreCase("submit"))
					break;
		}
	}
}