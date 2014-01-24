package me.dbstudios.solusrpg.events.player;

public enum RpgActionType {
	CRAFT(true),
	SMELT(true),
	USE(true),
	BREAK(true),
	PLACE(true),
	ATTACK,
	DEFEND;

	private final permitAction;

	private RpgActionType() {
		this(false);
	}

	private RpgActionType(boolean permitAction) {
		this.permitAction = permitAction;
	}

	public boolean isPermitAction() {
		return this.permitAction;
	}
}