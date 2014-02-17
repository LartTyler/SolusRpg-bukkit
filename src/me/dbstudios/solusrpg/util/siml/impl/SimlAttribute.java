package me.dbstudios.solusrpg.util.siml.impl;

import me.dbstudios.solusrpg.util.siml.Attribute;
import me.dbstudios.solusrpg.util.siml.AttributeType;

public class SimlAttribute implements Attribute {
	private final String name;

	private AttributeType<?> attrType;
	private Object value = null;

	public SimlAttribute(String name) {
		this(name, new StringAttributeType(), null);
	}

	public SimlAttribute(String name, Object value) {
		this(name, new StringAttributeType(), value);
	}

	public SimlAttribute(String name, Object value, AttributeType<?> type) {
		this.name = name;
		this.attrType = type;
		this.value = value;
	}

	public String getName() {
		return this.name;
	}

	public Object getValue() {
		return this.value;
	}

	public SimlAttribute setValue(String value) {
		if (!attrType.test(value))
			throw new IllegalArgumentException(String.format("'%s' is not a valid value according to this attribute's type (%s)!", value, attrType.getClass().getName()));

		return this.setValue(attrType.convert(value));
	}

	public SimlAttribute setValue(Object value) {
		this.value = value;

		return this;
	}

	public AttributeType<?> getType() {
		return this.attrType;
	}

	public SimlAttribute setType(AttributeType<?> type) {
		this.attrType = type;

		return this;
	}
}