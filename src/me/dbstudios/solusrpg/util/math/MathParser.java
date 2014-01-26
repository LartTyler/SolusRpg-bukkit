package me.dbstudios.solusrpg.util.math;

/**
 * Static test class for Solus's Expression class
 */
public final class MathParser {
	public static void main(String[] args) {
		if (args.length < 1)
			args = new String[] {"50 * ((level - 1) * 150)"};

		Expression expr = new Expression(args[0]);

		expr.setParameter("level", 7);

		System.out.println("\nResult: " + expr.eval());
	}
}