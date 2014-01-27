package me.dbstudios.solusrpg.util.math;

import java.util.HashMap;
import java.util.Map;

/**
 * Static test class for Solus's Expression class
 */
public final class MathParser {
	private static final Map<String, Double> params = new HashMap<>();

	public static void main(String[] args) {
		if (args.length < 1)
			args = new String[] {"50 * ((level - 1) * 150)"};

		String algo = null;

		for (int i = 0; i < args.length; i = i) {
			if (args[i].equalsIgnoreCase("-set") && i + 1 < args.length) {
				String[] split = args[i + 1].split("=");

				if (split.length == 2)
					try {
						params.put(split[0], Double.valueOf(split[1]));

						i += 2;
					} catch (NumberFormatException e) {
						System.out.println("\n/!\\ '" + split[1] + "' is not a valid number.\n");

						i++;
					}
				else
					System.out.println("\n/!\\ -set must be in the format '-set x=y'");
			} else
				algo = args[i++];
		}

		if (algo == null) {
			System.out.println("\n/!\\ No algorithm provided");

			return;
		}

		Expression expr = new Expression(algo);

		for (String k : params.keySet())
			expr.setParameter(k, params.get(k));

		System.out.println("\nResult: " + expr.eval());
	}
}