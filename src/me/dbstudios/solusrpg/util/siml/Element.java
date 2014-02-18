package me.dbstudios.solusrpg.util.siml;

import java.util.List;
import java.util.Map;

public interface Element {
	/**
	 * Gets the tag name of the element.
	 *
	 * @return the tag name of this element
	 */
	public String getTagName();

	/**
	 * Gets a list of all children elements.
	 *
	 * @return a list view of this element's children
	 */
	public List<Element> getChildren();

	/**
	 * Gets the size of the child element list.
	 *
	 * @return a count of the number of children this element has
	 */
	public int countChildren();

	/**
	 * Checks if this element has any children elements.
	 *
	 * @return true if children are found, false otherwise
	 */
	public boolean hasChildren();

	/**
	 * Appends the given element to the set of child elements.
	 *
	 * If <code>child</code> is already a child of this element, the element list
	 * should be unchanged and this method should fail silently (optional operation).
	 *
	 * @param  element the element to append
	 * @return         object reference for method chaining
	 */
	public Element appendChild(Element child);

	/**
	 * Appends a text node to this element.
	 *
	 * In effect, this should be the same as calling {@link Element#appendChild(Element)} where <code>Element</code> is
	 * a plain-text element.
	 *
	 * @param  text the text content of the node
	 * @return      object reference for method chaining
	 */
	public Element appendChild(String text);

	/**
	 * Prepends the given element to the set of child elements.
	 *
	 * If <code>child</code> is already a child of this element, the element list
	 * should be unchanged and this method should fail silently (optional operation).
	 *
	 * @param  element the element to prepend
	 * @return         object reference for method chaining
	 */
	public Element prependChild(Element child);

	/**
	 * Prepends a text node to this element.
	 *
	 * In effect, this should be the same as calling {@link Element#prependChild(Element)} where <code>Element</code> is
	 * a plain-text element.
	 *
	 * @param  text the text content of the node
	 * @return      object reference for method chaining
	 */
	public Element prependChild(String text);

	/**
	 * Sets the child element at the given index.
	 *
	 * If <code>index</code> is out of bounds, an {@link IndexOutOfBoundsException} should be thrown (optional operation).
	 *
	 * @param  index   the position to set the element at
	 * @param  element the element to place at index
	 * @return         the element that was replaced, or null if index was out of bounds
	 */
	public Element replaceChildAt(int index, Element child);

	/**
	 * Replaces the element at <code>index</code> with a plain-text code constructed from <code>text</code>.
	 *
	 * In effect, this should be the same as calling {@link Element#replaceChildAt(int, Element)} where <code>Element</code> is
	 * a plain-text element.
	 *
	 * If <code>index</code> is out of bounds, an {@link IndexOutOfBoundsException} should be thrown (optional operation).
	 *
	 * @param  index the position to set the element at
	 * @param  text  the text content of the new child
	 * @return       the element that was replaced, or null if index was out of bounds
	 */
	public Element replaceChildAt(int index, String text);

	/**
	 * Inserts the given element at index.
	 *
	 * If <code>index</code> is greater than the list boundry, a warning should be emitted and the element should be
	 * appended to the list of child elements.
	 *
	 * If <code>index</code> is less than zero, a warning should be emitted and the element should be
	 * prepended to the list of child elements.
	 *
	 * @param  index   the position to insert the element at
	 * @param  element the element to insert
	 * @return         object reference for method chaining
	 */
	public Element insertChildAt(int index, Element child);

	/**
	 * Inserts a new text node at <code>index</code>.
	 *
	 * In effect, this should be the same as calling {@link Element#insertChildAt(int, Element)} where <code>Element</code> is
	 * a plain-text element.
	 *
	 * If <code>index</code> is greater than the list boundry, a warning should be emitted and the element should be
	 * appended to the list of child elements.
	 *
	 * If <code>index</code> is less than zero, a warning should be emitted and the element should be
	 * prepended to the list of child elements.
	 *
	 * @param  index the position to insert the element at
	 * @param  text  the text content of the new child
	 * @return       object reference for method chaining
	 */
	public Element insertChildAt(int index, String text);

	/**
	 * Checks if the given element is a child of this element.
	 *
	 * @param  element the element to check for
	 * @return         true if this is the parent of the given element, false otherwise
	 */
	public boolean isChild(Element element);

	/**
	 * Removes the element at index.
	 *
	 * If <code>index</code> is out of bounds, an {@link IndexOutOfBoundsException} should be thrown (optional operation).
	 *
	 * @param  index the index to remove
	 * @return       the removed element, or null if one was not removed
	 */
	public Element removeElement(int index);

	/**
	 * Removes the given element from the list of child elements.
	 *
	 * If <code>element</code> is not contained in the list of child elements, this operation
	 * should fail silently.
	 *
	 * @param  element the element to remove
	 * @return         object reference for method chaining
	 */
	public Element removeElement(Element child);

	/**
	 * Removes all children from this element.
	 *
	 * @return object reference for method chaining
	 */
	public Element clearChildren();

	/**
	 * Checks if this element is a scaler (i.e. plain text) element, and not an element capable of
	 * containing other elements.
	 *
	 * @return true if this element contains nothing but text, false otherwise
	 */
	public boolean isPlainText();

	/**
	 * Gets the text content of this element.
	 *
	 * This method should return <code>null</code> if {@link Element#isPlainText} returns <code>false</code> (optional operation).
	 *
	 * @return the text content of this element
	 */
	public String getText();

	/**
	 * Sets the text content of this element, and wipes the child element list.
	 *
	 * @param  text the new text content
	 * @return      object reference for method chaining
	 */
	public Element setText(String text);

	/**
	 * Checks if this element is the root element in the {@link Document}.
	 *
	 * @return true if this element is the root, false otherwise
	 */
	public boolean isRoot();

	/**
	 * Gets the parent <code>Element</code> of this element.
	 *
	 * @return the parent element, or null if this is the root element
	 */
	public Element getParent();

	/**
	 * Checks if this element has an attribute with the given name.
	 *
	 * @param  name the name of the attribute to look up
	 * @return      true if the attribute exists, false otherwise
	 */
	public boolean hasAttribute(String name);

	/**
	 * Adds the given {@link Attribute} to this element.
	 *
	 * @param  attribute the {@link Attribute} to add
	 * @return           object reference for method chaining
	 */
	public Element setAttribute(Attribute attribute);

	/**
	 * Removes an {@link Attribute} from this element with the given <code>name</code>.
	 *
	 * If no attribute is found with the given name, this method should fail silently (optional operation).
	 *
	 * @param  name the name of the attribute to remove
	 * @return      the deleted attribute, or null if one did not exist
	 */
	public Attribute removeAttribute(String name);

	/**
	 * Removes the given {@link Attribute} from this element.
	 *
	 * If the attribute does not exist, this method should fail silently (optional operation).
	 *
	 * @param  attribute the {@link Attribute} to remove
	 * @return           object reference for method chaining
	 */
	public Element removeAttribute(Attribute attribute);

	/**
	 * Removes all attributes from this element.
	 *
	 * @return object reference for method chaining
	 */
	public Element clearAttributes();

	/**
	 * Gets the {@link Attribute} from this element with the given <code>name</code>.
	 *
	 * If no attribute is found with the given name, this method should fail silently and return <code>null</code> (optional operation).
	 *
	 * @param  name the name of the attribute to retrieve
	 * @return      the {@link Attribute} with the given name
	 */
	public Attribute getAttribute(String name);

	/**
	 * Gets the value held be the {@link Attribute} with the given name.
	 *
	 * In effect, calling this method should be the same as calling {@link Element#getAttribute(String)}{@link Attribute#getValue() .getValue()}.
	 *
	 * @param  name the name of the attribute to retrieve
	 * @return      the value of the attribute, or null if no such attribute exists
	 */
	public Object getAttributeValue(String name);

	/**
	 * Gets a map view of the attributes currently on this element.
	 *
	 * @return a map view of this element's attributes
	 */
	public Map<String, Attribute> getAttributes();
}