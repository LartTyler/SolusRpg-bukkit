package me.dbstudios.solusrpg.entities.resources;

public interface Resource {
	/**
	 * Gets the maximum value of this resource.
	 * 
	 * @return resource max value
	 */
	public int getMax();

	/**
	 * Gets the current value of this resource.
	 * 
	 * @return resource current value
	 */
	public int getCurrent();

	/**
	 * Sets the max value of this resource.
	 * 
	 * @param  max the new maximum value of this resource
	 * @return     object reference for method chaining
	 */
	public Resource setMax(int max);

	/**
	 * Sets the current value of this resource.
	 * 
	 * @param  current the new current value of this resource
	 * @return         object reference for method chaining
	 */
	public Resource setCurrent(int current);

	/**
	 * Spends <code>amount</code> of this resource.
	 *
	 * This can be used for various actions, such as:
	 * 		- Health damaging to save the player from death
	 * 		- Health payments for spells
	 * 		- Energy costs for spells / abilities
	 *
	 * This method will not allow the resource to drop below zero.
	 * 		
	 * @param  amount the amount of this resource to spend
	 * @return        object reference for method chaining
	 */
	public Resource expend(int amount);

	/**
	 * Restores <code>amount</code> of this resource.
	 *
	 * This can be used for various actions, such as:
	 * 		- Healing spells restoring health
	 * 		- Health / Energy regen over time
	 *
	 * This method will not allow the resource to go above {@link Resource#getMax()}.
	 * 
	 * @param  amount the amount to restore to the meter
	 * @return        object reference for method chaining
	 */
	public Resource restore(int amount);
}