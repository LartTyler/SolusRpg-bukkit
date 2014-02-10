package me.dbstudios.solusrpg.config;

import java.io.File;

public enum Directories {
	BASE("plugins::dbstudios::SolusRpg::"),
	CONFIG(BASE + "config::"),
	CONFIG_CHANNELS(CONFIG + "channels::"),
	CONFIG_CLASSES(CONFIG + "classes::"),
	CONFIG_STATS(CONFIG + "stats::"),
	DATA(BASE + "data::"),
	DATA_PLAYERS(DATA + "players::");

	private final String path;

	private Directories(String path) {
		this.path = path.replace("::", File.separator);
	}

	public String toString() {
		return this.path;
	}

	public static String getPlayerDataDir(String username) {
		return String.format("%s%s::%s::", Directories.DATA_PLAYERS.toString(), username.toLowerCase().substring(0, 2), username.toLowerCase()).replace("::", File.separator);
	}
}