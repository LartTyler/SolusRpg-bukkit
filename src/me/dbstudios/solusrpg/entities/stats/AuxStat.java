package me.dbstudios.solusrpg.entities.stats;

public interface AuxStat {
	/**
	 * Gets the fully-qualified name of the stat.
	 *
	 * @return the stat's fully-qualified name
	 */
	public String getName();

	/**
	 * Gets the display name of the stat.
	 *
	 * @return the stat's display name
	 */
	public String getDisplayName();

	/**
	 * Gets the path name of the stat.
	 *
	 * The path name is used anywhere the stat should appear in a YML configuration in order
	 * to point to the stat (as opposed to referencing it by using the FQN, or the display name
	 * which might contain invalid characters for a node name).
	 *
	 * @return gets the stat's path name
	 */
	public String getPathName();

	/**
	 * Gets the maximum level this stat can be on an entity.
	 *
	 * @return the stat's max possible rank
	 */
	public int getRankCap();

	/**
	 * Gets if the stat's rank should be capped, or if progression should not
	 * be limited.
	 *
	 * @return true if the stat is capped, false otherwise
	 */
	public boolean isRankCapped();

	/**
	 * Applies the effects of this stat at the given rank to the player.
	 *
	 * @param  target the player to apply the stat rank to
	 * @param  rank   the level of the stat being applied
	 * @return        object reference for method chaining
	 */
	public AuxStat applyRank(RpgPlayer target, int rank);

	/**
	 * Removes the effects of this stat at the given rank from the player.
	 *
	 * @param  target the player to remove the stat rank from
	 * @param  rank   the level of the stat being removed
	 * @return        object reference for method chaining
	 */
	public AuxStat removeRank(RpgPlayer target, int rank);

	/**
	 * Gets the {@link StatScaler} associated with the <code>AuxStat</code> with the given fully-qualified name.
	 *
	 * @param  fqn the fully-qualified stat name to look up
	 * @return     a {@link StatScaler} instance, or <code>null</code> if one does not exist
	 */
	public StatScaler getScalerFor(String fqn);

	/**
	 * Gets the {@link StatScaler} associated with the given <code>AuxStat</code>.
	 *
	 * @param  stat the auxiliary stat to look up
	 * @return      a {@link StatScaler} instance, or <code>null</code> if one does not exist
	 */
	public StatScaler getScalerFor(AuxStat stat);

	/**
	 * Gets the {@link StatScaler} associated with the given {@link StatType}.
	 *
	 * @param  type the core stat ({@link StatType}) to look up
	 * @return      a {@link StatScaler} instance, or <code>null</code> if one does not exist
	 */
	public StatScaler getScalerFor(StatType type);

	/**
	 * Adds a {@link StatScaler} for the auxiliary stat with the given fully-qualified name.
	 *
	 * If one already exists, the new {@link StatScaler} should override it. Additionally, all
	 * implementations should ensure that player's stats are updated accordingly as
	 * soon as possible; this is, however, an optional operation.
	 *
	 * @param  scaler the new {@link StatScaler} to add to this auxiliary stat
	 * @param  fqn    the fully-qualified stat name to add the scaler under
	 * @return        object reference for method chaining
	 */
	public AuxStat addStatScaler(StatScaler scaler, String fqn);

	/**
	 * Adds a {@link StatScaler} for the given auxiliary stat.
	 *
	 * If one already exists, the new {@link StatScaler} should override it. Additionally, all
	 * implementations should ensure that player's stats are updated accordingly as
	 * soon as possible; this is, however, an optional operation.
	 *
	 * @param  scaler the new {@link StatScaler} to add to this auxiliary stat
	 * @param  stat   the auxiliary stat to add the scaler under
	 * @return        object reference for method chaining
	 */
	public AuxStat addStatScaler(StatScaler scaler, AuxStat stat);

	/**
	 * Adds a {@link StatScaler} for the given core stat ({@link StatType}).
	 *
	 * If one already exists, the new {@link StatScaler} should override it. Additionally, all
	 * implementations should ensure that player's stats are updated accordingly as
	 * soon as possible; this is, however, an optional operation.
	 *
	 * @param  scaler the new {@link StatScaler} to add to this auxiliary stat
	 * @param  type   the {@link StatType} to add the scaler under
	 * @return        object reference for method chaining
	 */
	public AuxStat addStatScaler(StatScaler scaler, StatType type);

	/**
	 * Removes a {@link StatScaler} from this auxiliary stat.
	 *
	 * If one does not exist, implementations should fail silently.
	 *
	 * @param  fqn the fully-qualified auxiliary stat the scaler exists under
	 * @return     object reference for method chaining
	 */
	public AuxStat removeStatScaler(String fqn);

	/**
	 * Removes a {@link StatScaler} from this auxiliary stat.
	 * If one does not exist, implementations should fail silently.
	 *
	 * @param  stat the auxiliary stat that the scaler exists under
	 * @return      object reference for method chaining
	 */
	public AuxStat removeStatScaler(AuxStat stat);

	/**
	 * Removes a {@link StatScaler} from this auxiliary stat.
	 * If one does not exist, implementations should fail silently.
	 *
	 * @param  type the {@link StatType} of the core stat the scaler exists under
	 * @return      object reference for method chaining
	 */
	public AuxStat removeStatScaler(StatType type);

	/**
	 * Checks if a {@link StatScaler} exists for the auxiliary stat with the given fully-qualified name.
	 *
	 * @param  fqn the fully-qualified name of the auxiliary stat to look up
	 * @return     true if a scaler exists, false otherwise
	 */
	public boolean hasScalerFor(String fqn);

	/**
	 * Checks if a {@link StatScaler} exists for the given auxiliary stat.
	 *
	 * @param  stat the auxiliary stat to look up
	 * @return      true if a scaler exists, false otherwise
	 */
	public boolean hasScalerFor(AuxStat stat);

	/**
	 * Checks if a {@link StatScaler} exists for the core stat with the given {@link StatType}.
	 *
	 * @param  type the {@link StatType} to look up
	 * @return      true if a scaler exists, false otherwise
	 */
	public boolean hasScalerFor(StatType type);

	/**
	 * Gets a list view of all the {@link StatScaler}s associated with this stat.
	 *
	 * @return a list of all {@link StatScaler}s attached to this stat
	 */
	public List<StatScaler> getStatScalers();

	/**
	 * Validates the auxiliary stat and it's attached scalers and ranks.
	 *
	 * Since auxiliary stats are loaded sequentially, there may be some cases where stat ranks
	 * or scalers are not fully initialized. For example:
	 *
	 * 		Heavy Weapon Forging depends on a rank of 50+ in Light Weapon Forging. However, due to
	 * 		it's location in the stats list, HWF is loaded before LWF. When constructing rank
	 * 		requirements, it would be impossible to get the dependency without messing up the load order.
	 * 		Instead of implementing a complex fix for this, aux stats have a two part initialization. First,
	 * 		they are created and their ranks and scalers use soft references to stats (i.e. a String containing
	 * 		the fully-qualified name of the stat). Next, after all known aux stats have been loaded, the factory
	 * 		iterates over all aux stats and calls the <code>AuxStat#validate()</code> method, telling
	 * 		each stat to make sure that their ranks and scalers are referencing valid auxiliary stats. Any attachments
	 * 		that do NOT reference a valid stat should be removed, and a warning should be emitted into the console.
	 *
	 * @return object reference for method chaining
	 */
	public AuxStat validate();
}