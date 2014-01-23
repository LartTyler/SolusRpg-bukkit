package me.dbstudios.solusrpg.config;

import java.io.File;

public enum Directories {
	BASE("plugins::dbstudios::SolusRpg::"),
	CONFIG(BASE + "config::"),
	CONFIG_CLASSES(CONFIG + "classes::"),
	CONFIG_STATS(CONFIG + "stats::"),
	DATA(BASE + "data::");

	private final String path;

	private Directories(String path) {
		this.path = path.replace("::", File.separator);
	}

	public String toString() {
		return this.path;
	}
}