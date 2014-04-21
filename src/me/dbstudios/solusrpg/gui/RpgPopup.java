package me.dbstudios.solusrpg.gui;

import org.getspout.spoutapi.gui.Container;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.Widget;

public abstract class RpgPopup extends GenericPopup {
	public abstract void onAction(String action, Widget source);
}