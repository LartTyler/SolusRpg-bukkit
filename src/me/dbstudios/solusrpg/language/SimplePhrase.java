package me.dbstudios.solusrpg.language;

import java.util.HashMap;
import java.util.Map;

public class SimplePhrase implements Phrase {
	private Map<String, String> parameters = new HashMap<>();
	private String phrase;

	public SimplePhrase(String phrase) {
		this.phrase = phrase;
	}

	public Phrase setParameter(String key, String value) {
		parameteres.put(key, value);

		return this;
	}

	public String asText() {
		String s = this.phrase;

		for (String key : parameters.keySet())
			s = s.replace("{" + key + "}", parameters.get(key));

		this.reset();

		return s;
	}

	public Phrase reset() {
		parameters.clear();

		return this;
	}
}