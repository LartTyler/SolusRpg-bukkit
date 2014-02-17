package me.dbstudios.solusrpg.util.siml;

public interface Attribute {
	public Object getValue();
	public String getName();
	public Attribute setValue(Object value);
	public AttributeType<?> getType();
	public Attribute setType(AttributeType<?> type);
}