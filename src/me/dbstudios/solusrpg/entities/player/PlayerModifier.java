package me.dbstudios.solusrpg.entities.player;

public interface PlayerModifier {
	/**
	 * Defines if this modifier effect is a negative ("bad") effect or not.
	 * 
	 * @return true if the effect of this modifier should be a considered a malus on the player
	 */
	public boolean isEffectNegative();

	/**
	 * Applies this modifier to the player.
	 * 
	 * @param player the player to apply the modifier to
	 */
	public void modify(RpgPlayer player);

	/**
	 * Applies this modifier to the player for a period of time.
	 * 
	 * @param player   the player to apply the modifier to
	 * @param duration the duration, in ticks, that the modification will last
	 */
	public void modify(RpgPlayer player, long duration);

	/**
	 * Removes this modifier from the player.
	 *
	 * Implementations should make sure to call the RpgPlayer#removeModifier method to remove this
	 * modifier from the player's modifiers list.
	 * 
	 * @param player the player to unmodify
	 */
	public void unmodify(RpgPlayer player);
}