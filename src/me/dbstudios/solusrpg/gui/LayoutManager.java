package me.dbstudios.solusrpg.gui;

import org.getspout.spoutapi.gui.Container;
import org.getspout.spoutapi.gui.Screen;
import org.getspout.spoutapi.gui.Widget;

public class LayoutManager {
	public static void layout(Container root) {
		if (!root.hasPosition())
			root
				.setX(0)
				.setY(0);

		if (!root.hasSize())
			root.setWidth(root.hasContainer() ? root.getContainer().getWidth() : 427);

		root.setWidth(427);

		System.out.println(root.getWidth());

		int nextX = 0;
		int nextY = 0;

		for (Widget child : root.getChildren()) {
			System.out.println(String.format("(%d, %d) -> %s", nextX, nextY, child.getClass().getName()));

			if (child instanceof Container)
				LayoutManager.layout((Container)child);
			else {
				if (nextX + child.getWidth() > root.getWidth()) {
					nextX = 0;
					nextY += child.getHeight();
				}

				child
					.setX(nextX)
					.setY(nextY);

				nextX += child.getWidth();
			}
		}
	}
}