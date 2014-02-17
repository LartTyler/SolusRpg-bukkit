package me.dbstudios.solusrpg.util.siml;

public interface Document {
	/**
	 * Gets the upper most (root) element of the document.
	 *
	 * @return the root element of the document; all other elements are descendants of the root node
	 */
	public Element getRootElement();

	/**
	 * Checks if the document is considered valid, or if a parse error occurred during parsing.
	 *
	 * @return true if the document is safe to be used, false otherwise
	 */
	public boolean isValid();
}