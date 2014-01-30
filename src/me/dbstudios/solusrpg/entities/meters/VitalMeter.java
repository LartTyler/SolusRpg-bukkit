package me.dbstudios.solusrpg.entities.meters;

public interface VitalMeter {
	/**
	 * Gets the current value of this meter.
	 *
	 * @return this meter's value
	 */
	public int get();

	/**
	 * Sets this meter's value.
	 *
	 * @param  val the new value for this meter
	 * @return     object reference for method chaining
	 */
	public VitalMeter set(int val);

	/**
	 * Gets the max value of the meter.
	 *
	 * @return this meter's maximum value
	 */
	public int getMax();

	/**
	 * Sets the max value of the meter.
	 *
	 * @param  max this meter's maximum value
	 * @return     object reference for method chaining
	 */
	public VitalMeter setMax(int max);

	/**
	 * Gets the display name of the meter.
	 *
	 * @return this meter's display name
	 */
	public String getName();

	/**
	 * Sets the display name of the meter
	 *
	 * @param  name this meter's new display name
	 * @return      object reference for method chaining
	 */
	public VitalMeter setName(String name);

	/**
	 * Gets the duration, in ticks, of this meter's regeneration cycle.
	 *
	 * @return how many ticks it takes for this meter to regenerate one step
	 */
	public long getRegenRate();

	/**
	 * Sets the duration, in ticks, of this meter's regeneration cycle.
	 *
	 * @param  rate how many ticks it takes for this meter to regenerate one step
	 * @return      object reference for method chaining
	 */
	public VitalMeter setRegenRate(long rate);

	/**
	 * Sets the duration, in seconds, of this meter's regeneration cycle.
	 *
	 * Seconds are converted to ticks using the formula "ceil(rate * 20.0)".
	 *
	 * @param  rate the number of seconds it takes for this meter to regenerate one step
	 * @return      object reference for method chaining
	 */
	public VitalMeter setRegenRate(double rate);

	/**
	 * Gets the amount that one regeneration step should recover this meter by.
	 *
	 * @return the amount that a single regeneration step should recover this meter by
	 */
	public int getRegenAmount();

	/**
	 * "Damages" the meter, removing <code>amount</code> from it's value, and playing any animations that go along with
	 * a damage event.
	 *
	 * This should be the same as calling VialMeter#set(VitalMeter#get() - amount).
	 *
	 * @param  amount the amount to remove from the meter
	 * @return        object reference for method chaining
	 */
	public VitalMeter damage(int amount);
}