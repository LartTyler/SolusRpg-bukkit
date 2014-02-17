package me.dbstudios.solusrpg.util.siml;

public interface Attribute {
	/**
	 * Gets the value of this attribute.
	 *
	 * @return this attribute's value
	 */
	public Object getValue();

	/**
	 * Gets the name of this attribute.
	 *
	 * @return this attribute's name
	 */
	public String getName();

	/**
	 * Sets the value of this attribute.
	 *
	 * @param  value the new value
	 * @return       object reference for method chaining
	 */
	public Attribute setValue(Object value);

	/**
	 * Gets the {@link AttributeType} that can convert to and from this attribute's value.
	 *
	 * @return  the {@link AttributeType} that defines that data type of this attribute's value
	 */
	public AttributeType<?> getType();

	/**
	 * Sets the {@link AttributeType} of this attribute.
	 *
	 * It is an optional operation whether or not the current value of this attribute is checked
	 * against the new {@link AttributeType}.
	 *
	 * @param  type the new {@link AttributeType} of this attribute
	 * @return      object reference for method chaining
	 */
	public Attribute setType(AttributeType<?> type);
}