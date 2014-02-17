package me.dbstudios.solusrpg.util.siml.impl;

import me.dbstudios.solusrpg.util.siml.AttributeType;
import me.dbstudios.solusrpg.util.siml.TextColor;

import org.getspout.spoutapi.gui.Color;

public class ColorAttributeType implements AttributeType<Color> {
	public boolean test(String value) {
		try {
			TextColor.valueOf(value.toUpperCase());

			return true;
		} catch (Exception e) {}

		if (value.startsWith("#"))
			try {
				Integer.parseInt(value.substring(1), 16);

				return true;
			} catch (Exception e) {
				return false;
			}

		String[] split = value.split(",");

		if (split.length < 3 || split.length > 4)
			return false;

		for (int i = 0; i < split.length; i++)
			if (i <= 3)
				try {
					int v = Integer.valueOf(split[i].trim());

					if (v < 0 || v > 255)
						return false;
				} catch (Exception e) {
					return false;
				}
			else
				try {
					double d = Double.valueOf(split[i].trim());

					if (d < 0.0 || d > 1.0)
						return false;
				} catch (Exception e) {
					return false;
				}

		return true;
	}

	public Color convertFromString(String value) {
		try {
			return TextColor.valueOf(value.toUpperCase());
		} catch (Exception e) {}

		if (value.startsWith("#"))
			try {
				int rgba = Integer.parseInt(value.substring(1), 16);

				return new Color(rgba);
			} catch (Exception e) {}

		String[] split = value.split(",");
		Color color = new Color(0);

		for (int i = 0; i < split.length; i++)
			try {
				String channel = split[i].trim();

				switch (i) {
					case 1:
						color.setRed(this.clamp(Integer.valueOf(channel), 0, 255));

						break;

					case 2:
						color.setGreen(this.clamp(Integer.valueOf(channel), 0, 255));

						break;

					case 3:
						color.setBlue(this.clamp(Integer.valueOf(channel), 0, 255));

						break;

					case 4:
						if (channel.contains("."))
							color.setAlpha(this.clamp(Double.valueOf(channel), 0.0, 1.0));
						else
							color.setAlpha(this.clamp(Integer.valueOf(channel), 0, 255));

						break;
				}
			} catch (Exception e) {
				throw new IllegalArgumentexception(String.format("'%s' is not a valid color!", value));
			}

		return color;
	}

	public String convertToString(Object value) {
		if (!(value instanceof Color))
			throw new IllegalArgumentexception(String.format("'%s' is not a valid color!", value));

		Color c = (Color)value;

		return c.getRedI() + "," + c.getGreenI() + "," + c.getBlueI() + "," + c.getAlphaF();
	}

	private int clamp(int i, int min, int max) {
		return Math.max(min, Math.min(i, max));
	}

	private double clamp(double d, double min, double max) {
		return Math.max(min, Math.min(d, max));
	}
}