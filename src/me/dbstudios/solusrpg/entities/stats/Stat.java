package me.dbstudios.solusrpg.entities.stats;

public abstract class Stat<T> {
	private T value;

	public Stat(T value) {
		this.value = value;
	}

	public T getValue() {
		return this.value;
	}

	public Stat<T> setValue(T value) {
		this.value = value;

		return this;
	}
}