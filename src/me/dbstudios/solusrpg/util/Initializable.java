package me.dbstudios.solusrpg.util;

public abstract class Initializable {
	protected static boolean initialized = false;

	public static boolean isInitialized() {
		return initialized;
	}

	public static void initialize() {}
}