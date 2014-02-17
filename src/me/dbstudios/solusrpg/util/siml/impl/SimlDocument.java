package me.dbstudios.solusrpg.util.siml.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class SimlDocument implements Document {
	private final Element root = new SimlElement("root");

	private ConsumerState state = ConsumerState.DATA;
	private ConsumerState lastState = null;

	private SimlDocument(Reader reader) throws IOException {
		StringBuffer buffer = new StringBuffer();
		Element element = this.root;
		Attribute attr = null;
		int read;

		reader.mark(1);

		while ((read = reader.read()) != -1) {
			char ch = (char)read;

			switch (state) {
				case DATA:
					if (ch == '<') {
						if (buffer.length() > 0) {
							element.appendChild(buffer.toString);

							buffer = new StringBuffer();
						}

						this.switchState(ConsumerState.TAG_OPEN);
					} else
						buffer.append(ch);

					break;

				case TAG_OPEN:
					if (ch == '/')
						this.switchState(ConsumerState.END_TAG_OPEN);
					else if (Character.isLetter(ch)) {
						buffer.append(Character.toLowerCase(ch));

						this.switchState(ConsumerState.TAG_NAME);
					} else {
						buffer.append('>');
						reader.reset();

						this.switchState(ConsumerState.DATA);
					}

					break;

				case TAG_NAME:
					if (Character.isWhitespace(ch) && this.lastState != ConsumerState.END_TAG_OPEN) {
						element = new SimlElement(buffer.toString(), element);
						element.getParent().appendChild(element);

						buffer = new StringBuffer();
						this.switchState(ConsumerState.BEFORE_ATTRIBUTE_NAME);
					} else if (ch == '/' && this.lastState != ConsumerState.END_TAG_OPEN)
						this.switchState(ConsumerState.SELF_CLOSING_TAG);
					else if (Character.isLetter(ch))
						buffer.append(Character.toLowerCase(ch));
					else if (ch == '>') {
						if (this.lastState == ConsumerState.END_TAG_OPEN) // We're closing a tag, so step up the tree
							element = element.getParent();

						this.switchState(ConsumerState.DATA);
					}

					break;

				case SELF_CLOSING_TAG:
					if (ch == '>') {
						element = element.getParent(); // We've closed a tag, so step up the tree

						this.switchState(ConsumerState.DATA);
					} else {
						reader.reset();

						this.switchState(ConsumerState.BEFORE_ATTRIBUTE_NAME);
					}

					break;

				case BEFORE_ATTRIBUTE_NAME:
					if (Character.isWhitespace(ch))
						break;
					else if (ch == '/')
						this.switchState(ConsumerState.SELF_CLOSING_TAG);
					else if (ch == '>')
						this.switchState(ConsumerState.DATA);
					else {
						if (Character.isLetter(ch))
							buffer.append(Character.toLowerCase(ch));
						else
							buffer.append(ch);

						this.switchState(ConsumerState.ATTRIBUTE_NAME);
					}

					break;

				case ATTRIBUTE_NAME:
					if (Character.isWhitespace(ch))
						this.switchState(ConsumerState.AFTER_ATTRIBUTE_NAME);
					else if (ch == '/')
						this.switchState(ConsumerState.SELF_CLOSING_TAG);
					else if (ch == '=')
						this.switchState(ConsumerState.BEFORE_ATTRIBUTE_VALUE);
					else
						if (Character.isLetter(ch))
							buffer.append(Character.toLowerCase(ch))
						else
							buffer.append(ch);

					if (state != ConsumerState.ATTRIBUTE_NAME) {
						attr = new SimlAttribute(buffer.toString());
						element.setAttribute(attr);

						buffer = new StringBuffer();
					}

					break;

				case AFTER_ATTRIBUTE_NAME:
					if (Character.isWhitespace(ch))
						break;
					else if (ch == '/')
						this.switchState(ConsumerState.SELF_CLOSING_TAG);
					else if (ch == '=')
						this.switchState(ConsumerState.BEFORE_ATTRIBUTE_VALUE);
					else {
						if (Character.isLetter(ch))
							buffer.append(Character.toLowerCase(ch));
						else
							buffer.append(ch);

						this.switchState(ConsumerState.ATTRIBUTE_NAME);
					}

					break;

				case BEFORE_ATTRIBUTE_VALUE:
					if (Character.isWhitespace(ch))
						break;
					else if (ch == '"' || ch =='\'')
						this.switchState(ConsumerState.ATTRIBUTE_VALUE_QUOTED);
					else if (ch == '>')
						this.switchState(ConsumerState.DATA);
					else {
						if (Character.isLetter(ch))
							buffer.append(Character.toLowerCase(ch));
						else
							buffer.append(ch);

						this.switchState(ConsumerState.ATTRIBUTE_VALUE_UNQUOTED);
					}

					break;

				case ATTRIBUTE_VALUE_QUOTED:
					if (ch == '"' || ch == '\'') {
						for (AttributeTypes type : AttributeTypes.values())
							if (type.test(buffer.toString()))
								attr.setValue(type.getType().convertFromString(buffer.toString()));

						buffer = new StringBuffer();

						this.switchState(ConsumerState.AFTER_ATTRIBUTE_VALUE);
					} else
						buffer.append(ch);

					break;

				case ATTRIBUTE_VALUE_UNQUOTED:
					if (Character.isWhitespace(ch))
						this.switchState(ConsumerState.BEFORE_ATTRIBUTE_NAME);
					else if (ch == '>')
						this.switchState(ConsumerState.DATA);
					else
						buffer.append(ch);

					if (state != ConsumerState.ATTRIBUTE_VALUE_UNQUOTED) {
						for (AttributeTypes type : AttributeTypes.values())
							if (type.test(buffer.toString()))
								attr.setValue(type.getType().convertFromString(buffer.toString()));

						buffer = new StringBuffer();
					}

					break;

				case AFTER_ATTRIBUTE_VALUE:
					if (Character.isWhitespace(ch))
						this.switchState(ConsumerState.BEFORE_ATTRIBUTE_NAME);
					else if (ch == '/')
						this.switchState(ConsumerState.SELF_CLOSING_TAG);
					else if (ch == '>')
						this.switchState(ConsumerState.DATA);
					else {
						this.switchState(ConsumerState.BEFORE_ATTRIBUTE_NAME);

						reader.reset();
					}

					break;

				case END_TAG_OPEN:
					if (Character.isLetter(ch)) {
						buffer.append(Character.toLowerCase(ch));

						this.switchState(ConsumerState.TAG_NAME);
					} else
						this.switchState(ConsumerState.DATA);

			}

			reader.mark(1);
		}

		if (state != ConsumerState.DATA)
			throw new DocumentCreationException("Reached EOF while not in DATA state; this indicates a malformed document.");
	}

	private void switchState(ConsumerState newState) {
		this.lastState = this.state;
		this.state = newState;
	}

	public static Document create(String document) {
		try {
			return SimlDocument.create(new StringReader(document));
		} catch (IOException e) {
			System.out.println("How on earth did an IOException occur while reading a string???");

			e.printStackTrace();
		}
	}

	public static Document create(File document) throws IOException e {
		return SimlDocument.create(Files.newBufferedReader(file.toPath(), Charset.forName("UTF-8")));
	}

	public static Document create(InputStream stream) throws IOException e {
		return SimlDocument.create(new InputStreamReader(stream));
	}

	public static Document create(Reader reader) throws IOException e {
		if (!(reader instanceof BufferedReader))
			reader = new BufferedReader(reader);

		return new SimlDocument(reader);
	}
}