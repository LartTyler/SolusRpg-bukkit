package me.dbstudios.solusrpg.util.siml.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimlElement implements Element {
	private final Element parent;
	private final String tagName;

	private Map<String, Attribute> attributes = new HashMap<>();
	private List<Element> children = new ArrayList<>();
	private String textContent = null;

	public SimlElement(String tagName, Element parent) {
		this.tagName = tagName;
		this.parent = parent;
	}

	public SimlElement(String tagName) {
		this.tagName = tagName;
		this.parent = null;
	}

	public String getTagName() {
		return this.tagName;
	}

	public List<Element> getChildren() {
		return Collections.unmodifiableList(this.children);
	}

	public Element appendChild(Element child) {
		if (!this.isChild(child))
			children.add(child);

		return this;
	}

	public Element prependChild(Element child) {
		if (!this.isChild(child))
			children.add(0, child);

		return this;
	}

	public Element replaceChildAt(int index, Element child) {
		return children.set(index, child);
	}

	public Element insertChildAt(int index, Element child) {
		children.add(index, child);

		return this;
	}

	public boolean isChild(Element element) {
		return children.contains(element);
	}

	public Element removeElement(int index) {
		return children.remove(index);
	}

	public Element removeElement(Element child) {
		children.remove(child);

		return this;
	}

	public Element clearChildren() {
		children.clear();
		this.textContent = null;

		return this;
	}

	public boolean isPlainText() {
		return this.textContent != null;
	}

	public String getText() {
		return this.textContent;
	}

	public Element setText(String text) {
		this.textContent = text;

		return this;
	}

	public Element isRoot() {
		return this.parent == null;
	}

	public Element getParent() {
		return this.parent;
	}

	public boolean hasAttribute(String name) {
		return attributes.containsKey(name);
	}

	public Element setAttribute(Attribute attribute) {
		attributes.put(attribute.getName(), attribute);

		return this;
	}

	public Attribute removeAttribute(String name) {
		return attributes.remove(name);
	}

	public Element removeAttribute(Attribute attribute) {
		this.removeAttribute(attribute.getName());

		return this;
	}

	public Element clearAttributes() {
		attributes.clear();

		return this;
	}

	public Attribute getAttribute(String name) {
		return attributes.get(name);
	}
}