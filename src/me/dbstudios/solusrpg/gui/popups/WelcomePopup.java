package me.dbstudios.solusrpg.gui.popups;

import java.io.File;
import java.io.IOException;

import me.dbstudios.solusrpg.config.Directories;
import me.dbstudios.solusrpg.exceptions.CreationException;
import me.dbstudios.solusrpg.gui.RpgPopup;
import me.dbstudios.solusrpg.gui.SimlConverter;
import me.dbstudios.solusrpg.util.siml.Document;

import org.getspout.spoutapi.gui.Container;
import org.getspout.spoutapi.gui.Widget;

public class WelcomePopup extends RpgPopup {
	private final Container root;

	public WelcomePopup(Document document) {
		this.root = (Container)SimlConverter.convert(document);

		// LayoutManager.layout(this.root, this);
	}

	public void onAction(String action, Widget source) {

	}
}