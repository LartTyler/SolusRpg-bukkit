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
					if (ch == '<')
						state = ConsumerState.TAG_OPEN;
					else
						buffer.append(ch);

					break;

				case TAG_OPEN:
					if (ch == '/')
						state = ConsumerState.END_TAG_OPEN;
					else if (Character.isLetter(ch)) {
						buffer.append(Character.toLowerCase(ch));

						state = ConsumerState.TAG_NAME;
					} else {
						buffer.append('>');
						reader.reset();

						state = ConsumerState.DATA;
					}

					break;

				case TAG_NAME:
					if (Character.isWhitespace(ch)) {
						element = new SimlElement(buffer.toString(), element);
						element.getParent().appendChild(element);

						buffer = new StringBuffer();
						state = ConsumerState.BEFORE_ATTRIBUTE_NAME;
					} else if (ch == '/')
						state = ConsumerState.SELF_CLOSING_TAG;
					else if (Character.isLetter(ch))
						buffer.append(Character.toLowerCase(ch));
					else if (ch == '>')
						state = ConsumerState.DATA;

					break;

				case SELF_CLOSING_TAG:
					if (ch == '>')
						state = ConsumerState.DATA;
					else {
						reader.reset();

						state = ConsumerState.BEFORE_ATTRIBUTE_NAME;
					}

					break;

				case BEFORE_ATTRIBUTE_NAME:
					if (Character.isWhitespace(ch))
						break;
					else if (ch == '/')
						state = ConsumerState.SELF_CLOSING_TAG;
					else if (ch == '>')
						state = ConsumerState.DATA;
					else {
						if (Character.isLetter(ch))
							buffer.append(Character.toLowerCase(ch));
						else
							buffer.append(ch);

						state = ConsumerState.ATTRIBUTE_NAME;
					}

					break;

				case ATTRIBUTE_NAME:
					if (Character.isWhitespace(ch))
						state = ConsumerState.AFTER_ATTRIBUTE_NAME;
					else if (ch == '/')
						state = ConsumerState.SELF_CLOSING_TAG;
					else if (ch == '=')
						state = ConsumerState.BEFORE_ATTRIBUTE_VALUE;
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
						state = ConsumerState.SELF_CLOSING_TAG;
					else if (ch == '=')
						state = ConsumerState.BEFORE_ATTRIBUTE_VALUE;
					else {
						if (Character.isLetter(ch))
							buffer.append(Character.toLowerCase(ch));
						else
							buffer.append(ch);

						state = ConsumerState.ATTRIBUTE_NAME;
					}

					break;

				case BEFORE_ATTRIBUTE_VALUE:
					if (Character.isWhitespace(ch))
						break;
					else if (ch == '"' || ch =='\'')
						state = ConsumerState.ATTRIBUTE_VALUE_QUOTED;
					else if (ch == '>')
						state = ConsumerState.DATA;
					else {
						if (Character.isLetter(ch))
							buffer.append(Character.toLowerCase(ch));
						else
							buffer.append(ch);

						state = ConsumerState.ATTRIBUTE_VALUE_UNQUOTED;
					}

					break;

				case ATTRIBUTE_VALUE_QUOTED:
					if (ch == '"' || ch == '\'') {
						
					}
			}

			reader.mark(1);
		}

		if (state != ConsumerState.DATA)
			throw new DocumentCreationException("Reached EOF while not in DATA state; this indicates a malformed document.");
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