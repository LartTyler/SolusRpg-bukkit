package me.dbstudios.solusrpg.util.math;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class FlowControl {
	private final String name;

	private Map<String, Double> parentParams = new HashMap<>();

	public FlowControl(String name, Map<String, Double> parentParams) {
		this(name);

		this.parentParams = parentParams;
	}

	public FlowControl(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public String eval(String... args) {
		return this.eval(null, args);
	}

	public abstract String eval(Expression parent, String... args);

	public Map<String, Double> getParentParameters() {
		return Collections.unmodifiableMap(this.parentParams);
	}
}