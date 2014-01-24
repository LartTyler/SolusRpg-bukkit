package me.dbstudios.util.math;

public final class MathParser {
	public static void main(String[] args) {
		if (args.length < 1)
			args = new String[] {"10 + 5"};

		Tokenizer tokenizer = new Tokenizer(args[0]);

		System.out.println("\n\n--------------\n");

		// for (int i = 0; i < 5; i++)
		// 	if (tokenizer.hasNext())
		// 		System.out.println(tokenizer.next());

		while (tokenizer.hasNext())
			System.out.println(tokenizer.next());

		System.out.println("\n\n--> Done.");
	}
}