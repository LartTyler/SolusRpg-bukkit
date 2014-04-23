package me.dbstudios.solusrpg.gui.popups;

import java.io.File;
import java.io.IOException;

import me.dbstudios.solusrpg.config.Directories;
import me.dbstudios.solusrpg.exceptions.CreationException;
import me.dbstudios.solusrpg.gui.RpgPopup;
import me.dbstudios.solusrpg.gui.SimlConverter;
import me.dbstudios.solusrpg.util.siml.Document;

import org.bukkit.plugin.Plugin;

import org.getspout.spoutapi.gui.Container;
import org.getspout.spoutapi.gui.Screen;
import org.getspout.spoutapi.gui.Widget;

public class WelcomePopup extends RpgPopup {
	private final Document document;

	private Container root = null;

	public WelcomePopup(Document document) {
		this.document = document;;

		// LayoutManager.layout(this.root, this);
	}

	public void onAction(String action, Widget source) {

	}

	public WelcomePopup setScreen(Plugin plugin, Screen screen) {
		super.setScreen(plugin, screen);

		this.root = (Container)SimlConverter.convert(this.document, this);
	}
}