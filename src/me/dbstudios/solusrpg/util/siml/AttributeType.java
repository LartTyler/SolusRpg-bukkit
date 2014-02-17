package me.dbstudios.solusrpg.util.siml;

public interface AttributeType<T> {
	/**
	 * Tests if the given string satisfies the requirement for this attribute type.
	 *
	 * @param  value the string to test against
	 * @return       true if the given string could be assigned to this type
	 */
	public boolean test(String value);

	/**
	 * Converts a string to the type represented by this attribute type.
	 *
	 * No type checking will occur at this point; it is trusted that the value provided passes {@link AttributeType#test(String)}.
	 *
	 * @param  value the value to convert
	 * @return       the converted value of the given string
	 */
	public T convertFromString(String value);

	/**
	 * Converts a attribute value to it's string representation.
	 *
	 * The provided object should be an instance of the class represented by this attribute type, i.e:
	 * 		<code>value instanceof T</code>
	 *
	 * @param  value the object to stringify
	 * @return       the string representation of the object, or <code>null</code> if it could not be converted
	 */
	public String convertToString(Object value);
}