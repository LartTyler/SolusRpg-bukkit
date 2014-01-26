package me.dbstudios.solusrpg.util.math;

public final class MathParser {
	public static void main(String[] args) {
		if (args.length < 1)
			args = new String[] {"10 + 5"};

		Expression expr = new Expression("50 * ((level - 1) * 150)");

		expr.setParameter("level", 7);

		System.out.println("\nResult: " + expr.eval());
	}
}