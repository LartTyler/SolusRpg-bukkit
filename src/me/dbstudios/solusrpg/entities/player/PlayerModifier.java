package me.dbstudios.solusrpg.entities.player;

public interface PlayerModifier {
	public void modify(RpgPlayer player);
	public void modify(RpgPlayer player, long duration);
	public void unmodify(RpgPlayer player);
}