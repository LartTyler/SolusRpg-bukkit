package me.dbstudios.solusrpg.util.math;

import java.util.Iterator;

public class Tokenizer implements Iterator<String> {
	private static final char DECIMAL_SEPERATOR = '.';
	private static final char NEGATIVE_SIGN = '-';

	private final String input;

	private String lastToken = null;
	private int pos;

	public Tokenizer(String input) {
		this.input = input;
	}

	public boolean hasNext() {
		return pos < input.length();
	}

	public String peek() {
		int pos = this.pos;
		String peek = null;

		if (this.hasNext())
			peek = this.next();

		this.pos = pos;

		return peek;
	}

	public String next() {
		if (!this.hasNext()) {
			this.lastToken = null;

			return null;
		}

		StringBuilder token = new StringBuilder();
		char ch = input.charAt(pos);

		while (Character.isWhitespace(ch) && this.hasNext())
			ch = input.charAt(++pos);

		if (Character.isDigit(ch))
			while ((Character.isDigit(ch) || ch == DECIMAL_SEPERATOR) && this.hasNext()) {
				token.append(input.charAt(pos++));

				ch = this.hasNext() ? input.charAt(pos) : 0;
			}
		else if (ch == NEGATIVE_SIGN && Character.isDigit(this.peekNextChar()) && (lastToken.equals("(") || lastToken == null)) {
			token.append(NEGATIVE_SIGN);

			pos++;

			token.append(this.next());
		} else if (ch == NEGATIVE_SIGN) {
			token.append(NEGATIVE_SIGN);

			pos++;
		} else if (Character.isLetter(ch))
			while ((Character.isLetter(ch) || Character.isDigit(ch) || ch == '_') && this.hasNext()) {
				token.append(input.charAt(pos++));

				ch = this.hasNext() ? input.charAt(pos) : 0;
			}
		else if (ch == '(' || ch == ')') {
			token.append(ch);

			pos++;
		} else
			while (!Character.isLetter(ch) && !Character.isDigit(ch) && ch != '(' && ch != ')' && this.hasNext()) {
				token.append(input.charAt(pos++));

				ch = this.hasNext() ? input.charAt(pos) : 0;
			}

		this.lastToken = token.toString().trim();

		return this.lastToken;
	}

	private char peekNextChar() {
		if (pos < input.length() - 1)
			return input.charAt(pos + 1);

		return 0;
	}

	public void remove() {
		throw new RuntimeException("Tokenizer#remove not supported.");
	}
}