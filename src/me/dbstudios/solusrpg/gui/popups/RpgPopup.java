package me.dbstudios.solusrpg.gui.popups;

import org.getspout.spoutapi.gui.GenericPopup;

public abstract class RpgPopup extends GenericPopup {
	public abstract void onAction(String action, Widget sender);
}