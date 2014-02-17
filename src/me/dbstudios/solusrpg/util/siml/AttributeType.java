package me.dbstudios.solusrpg.util.siml;

public interface AttributeType<T> {
	public boolean test(String value);
	public T convertFromString(String value);
	public String convertToString(Object value);
}