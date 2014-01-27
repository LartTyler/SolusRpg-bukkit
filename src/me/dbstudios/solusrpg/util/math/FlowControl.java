package me.dbstudios.solusrpg.util.math;

public abstract class FlowControl {
	private final String name;

	public FlowControl(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public abstract String eval(String... args);
}