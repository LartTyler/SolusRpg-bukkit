package me.dbstudios.solusrpg.gui.popups;

import org.getspout.spoutapi.gui.GenericPopup;

public abstract class SolusPopup extends GenericPopup {
	public abstract void onAction(String action, Widget sender);
}