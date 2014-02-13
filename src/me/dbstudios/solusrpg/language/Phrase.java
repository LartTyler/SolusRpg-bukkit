package me.dbstudios.solusrpg.language;

public interface Phrase {
	/**
	 * Sets the value of the parameter with the name of <code>key</code>.
	 *
	 * @param  key   the placeholder name to set
	 * @param  value the value to replace
	 * @return       the {@link Phrase} object for method chaining
	 */
	public Phrase setParameter(String key, String value);

	/**
	 * Gets the text output (using the currently provided parameter values).
	 *
	 * @return the text output using the current parameters
	 */
	public String asText();

	/**
	 * Resets the current parameters on the object. This should be called any time the same phrase object is reused, if Phrase#asText was never called.
	 *
	 * @return the {@link Phrase} object for method chaining
	 */
	public Phrase reset();
}