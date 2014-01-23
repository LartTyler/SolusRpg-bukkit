package me.dbstudios.solusrpg.config;

public class Metadata<K> {
	private final Map<K, Object> data = new HashMap<>();

	public boolean has(K key) {
		return data.containsKey(key);
	}

	public boolean hasOfType(K key, Class<T> type) {
		return this.has(key) && type.isInstance(data.get(key));
	}

	public Object get(K key) {
		return this.get(key, null);
	}

	public Object get(K key, Object def) {
		if (this.has(key))
			return data.get(key);

		return def;
	}

	public <T> T getAsType(K key, Class<T> type) {
		return this.getAsType(key, type, null);
	}

	public <T> T getAsType(K key, Class<T> type, T def) {
		if (this.hasOfType(key, type))
			return type.cast(data.get(key));

		return def;
	}

	public Metadata<K> set(K key, Object value) {
		data.put(key, value);

		return this;
	}

	public Metadata<K> remove(K key) {
		data.remove(key);

		return this;
	}

	public Metadata<K> clear() {
		data.clear();

		return this;
	}
}