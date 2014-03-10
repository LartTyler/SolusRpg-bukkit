package me.dbstudios.solusrpg.gui;

import org.getspout.spoutapi.gui.Button;

public class ButtonConverter implements Converter<Button> {
	public Button convert(Element element) {
		Button button = new SolusRpgButton();
	}
}