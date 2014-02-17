package me.dbstudios.solusrpg.util.siml;

import org.getspout.spoutapi.gui.Color;

public enum TextColor {
	WHITE(new Color(255, 255, 255)),
	RED(new Color(255, 0, 0)),
	GREEN(new Color(0, 128, 0)),
	BLUE(new Color(0, 0, 255)),
	YELLOW(new Color(255, 255, 0)),
	NAVY(new Color(0, 0, 128)),
	BLACK(new Color(0, 0, 0)),
	GRAY(new Color(128, 128, 128)),
	LIGHTGRAY(new Color(211, 211, 211));

	private final Color color;

	private TextColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color.clone();
	}
}