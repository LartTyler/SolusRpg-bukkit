package me.dbstudios.solusrpg.entities.resources;

public class EnergyResource implements Resource {
	private int max;
	private int current;

	public EnergyResource(int max) {
		this(max, max);
	}

	public EnergyResource(int max, int current) {
		this.max = max;
		this.current = current;
	}

	public int getMax() {
		return this.max;
	}

	public int getCurrent() {
		return this.current;
	}

	public Resource setMax(int max) {
		this.max = max;

		return this;
	}

	public Resource setCurrent(int current) {
		this.current = current;

		return this;
	}

	public Resource expend(int amount) {
		this.current = Math.max(0, this.current - amount);

		return this;
	}

	public Resource restore(int amount) {
		this.current = Math.min(this.max, this.current + amount);

		return this;
	}
}