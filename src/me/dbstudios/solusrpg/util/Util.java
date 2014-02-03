package me.dbstudios.solusrpg.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Util {
	/**
	 * Narrows down a collection of strings to those matched by the fuzzy search pattern.
	 *
	 * This operation is case insensitive.
	 *
	 * @param  str  the fuzzy match pattern to use
	 * @param  list the original collection of strings
	 * @return      the searched list of strings
	 */
	public static List<String> getFuzzyMatch(String str, Collection<String> list) {
		List<String> matches = new ArrayList<>();

		for (String s : list)
			if (Util.fuzzyMatches(str, s))
				matches.add(s);

		return matches;
	}

	/**
	 * Determines if a string matches a fuzzy match pattern.
	 *
	 * This operation is case insensitive.
	 *
	 * @param  fuzzy the fuzzy match pattern to use
	 * @param  str   the string to match against
	 * @return       true if <code>str</code> is matched by the search pattern
	 */
	public static boolean fuzzyMatches(String fuzzy, String str) {
		int lastPos = 0;

		String f = fuzzy.toLowerCase();
		String s = str.toLowerCase();

		for (int i = 0; i < f.length(); i++) {
			lastPos = s.indexOf(f.substring(i, i + 1), lastPos);

			if (lastPos == -1)
				return false;
		}

		return true;
	}

	public static String toQualifiedName(String name) {
		return Util.toQualifiedName(name, "");
	}

	public static String toQualifiedName(String name, String suffix) {
		if (Util.isQualifiedName(name))
			return name;

		StringBuilder fqn = new StringBuilder();

		for (String s : name.split("[ _.,'-]"))
			fqn
				.append(s.substring(0, 1).toUpperCase())
				.append(s.substring(1).toLowerCase());

		if (!fqn.toString().endsWith(suffix))
			fqn.append(suffix);

		return fqn.toString();
	}

	public static boolean isQualifiedName(String name) {
		return Util.isQualifiedName(name, "");
	}

	public static boolean isQualifiedName(String name, String suffix) {
		return !name.matches("[ _.,'-]") && name.endsWith(suffix);
	}

	public static String toPathName(String name) {
		if (Util.isPathName(name))
			return name;

		StringBuilder pn = new StringBuilder();

		for (String s : name.split("[ _.,'-]"))
			pn
				.append("-")
				.append(s.toLowerCase());

		return pn.toString().substring(1);
	}

	public static boolean isPathName(String name) {
		return name.matches("^[^ _.,'-]+(-[^ _.,'-]+)*$");
	}

	public static String toMaterialName(String name) {
		if (Util.isMaterialName(name))
			return name;

		StringBuilder mn = new StringBuilder();

		for (String s : name.split("[ _]"))
			mn
				.append("_")
				.append(s.toUpperCase());

		return mn.toString().substring(1);
	}

	public static boolean isMaterialName(String name) {
		return name.matches("^[A-Z]+(_[A-Z]+)*$");
	}
}