package me.dbstudios.solusrpg.entities.player;

import java.util.Collection;
import java.util.Set;

import me.dbstudios.solusrpg.entities.RpgClass;
import me.dbstudios.solusrpg.entities.stats.AuxStat;
import me.dbstudios.solusrpg.entities.stats.StatType;
import me.dbstudios.solusrpg.events.player.RpgActionType;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public interface RpgPlayer {
	/**
	 * Gets the Bukkit <code>Player</code> instance that the RpgPlayer is built on.
	 *
	 * @return the <code>Player</code> instance that is being wrapped
	 */
	public Player getBasePlayer();

	/**
	 * Gets the player's username.
	 *
	 * Calling this method should have the same effect as RpgPlayer#getBasePlayer()#getName()
	 *
	 * @return the player's username
	 */
	public String getName();

	/**
	 * Gets the player's display name.
	 *
	 * Calling this method should have the same effect as calling RpgPlayer#getBasePlayer()#getDisplayName()
	 *
	 * @return [description]
	 */
	public String getDisplayName();

	/**
	 * Gets the player's current level.
	 *
	 * @return the player's level
	 */
	public int getLevel();

	/**
	 * Sets the player's level.
	 *
	 * @param  level the player's new level
	 * @return       object reference for method chaining
	 */
	public RpgPlayer setLevel(int level);

	/**
	 * Gets the player's scaled experience.
	 *
	 * @return the player's current experience total
	 */
	public int getExp();

	/**
	 * Sets the player's scaled experience.
	 *
	 * @param  exp the player's new experience total
	 * @return     object reference for method chaining
	 */
	public RpgPlayer setExp(int exp);

	/**
	 * Gets the level cost (total exp. required to level up) of the player's current level. A player's level cost is determined
	 * first by their personal {@link ExperienceScaler} or, if one does not exist, the globally defined scaler.
	 *
	 * @return the total experience cost for the player's current level
	 */
	public int getLevelCost();

	/**
	 * Gets the player's real experience.
	 *
	 * Calling this method should have the same effect as calling RpgPlayer#getBasePlayer()#getExp()
	 *
	 * @return a float value between 0.0 and 1.0
	 */
	public float getRealExp();

	/**
	 * Sets the player's real experience.
	 *
	 * Calling this method should have the same effect as calling RpgPlayer#getBasePlayer()#setExp(float)
	 *
	 * @param  exp a float value between 0.0 and 1.0
	 * @return     object reference for method chaining
	 */
	public RpgPlayer setRealExp(float exp);

	/**
	 * Gets the level of an auxiliary stat.
	 *
	 * @param  stat the stat to look up
	 * @return      the integer level of the aux. stat
	 */
	public int getRealStatLevel(AuxStat stat);

	/**
	 * Gets the level of the auxiliary stat with the given fully-qualified name.
	 *
	 * @param  fqn the fully-qualified name of the aux. stat
	 * @return     the integer level of the aux. stat
	 */
	public int getRealStatLevel(String fqn);

	/**
	 * Gets the level of the core stat with the given {@link StatType}.
	 *
	 * @param  type the core stat type to look up
	 * @return      the integer level of the core stat
	 */
	public int getRealStatLevel(StatType type);

	/**
	 * Gets the "real" level (stat level + bonuses) of an auxiliary stat.
	 *
	 * @param  stat the aux. stat to look up
	 * @return      the integer level, with bonuses, of the aux. stat
	 */
	public int getStatLevel(AuxStat stat);

	/**
	 * Gets the "real" level (stat level + bonuses) of an auxiliary stat.
	 *
	 * @param  fqn the fully-qualified name of the auxiliary stat to look up
	 * @return     the integer level, with bonuses, of the aux. stat
	 */
	public int getStatLevel(String fqn);

	/**
	 * Gets the "real" level (stat level + bonuses) of the core stat with the given {@link StatType}.
	 *
	 * @param  type the core stat to look up
	 * @return      the integer level, with bonuses, of the core stat
	 */
	public int getStatLevel(StatType type);

	/**
	 * Sets the level of an auxiliary stat.
	 *
	 * @param  stat  the stat to set
	 * @param  level the new level of the stat
	 * @return       object reference for method chaining
	 */
	public RpgPlayer setStatLevel(AuxStat stat, int level);

	/**
	 * Sets the level of an auxiliary stat with the fully-qualified name.
	 *
	 * @param  fqn   the fully-qualified name of the aux. stat
	 * @param  level the new level of the stat
	 * @return       object reference for method chaining
	 */
	public RpgPlayer setStatLevel(String fqn, int level);

	/**
	 * Sets the level of the core stat with the given {@link StatType}.
	 *
	 * @param  type  the core stat type to set
	 * @param  level the new level of the core stat
	 * @return       object reference for method chaining
	 */
	public RpgPlayer setStatLevel(StatType type, int level);

	/**
	 * Adds a {@link PlayerModifier} to the player.
	 *
	 * This is the preferred way for temporarily modifying a player's stats / state, as Solus will walk through all player
	 * modifiers and run the <code>PlayerModifier#unmodify</code> method to undo the changes made.
	 *
	 * @param  modifier the {@link PlayerModifier} to apply
	 * @return          object reference for method chaining
	 */
	public RpgPlayer addModifier(PlayerModifier modifier);

	/**
	 * Removes a {@link PlayerModifier} from the player.
	 *
	 * This method will be called for every active {@link PlayerModifier} on the player when the server stops
	 * or the player is unloaded.
	 *
	 * @param  modifier the modifier to remove
	 * @return          object reference for method chaining
	 */
	public RpgPlayer removeModifier(PlayerModifier modifier);

	/**
	 * Removes a {@link PlayerModifier} from the player.
	 *
	 * In addition to removing the modifier, this method should also call the PlayerModifer#unmodify method on
	 * each modifier before removing it.
	 *
	 * @param  modifier the modifier to clean and remove
	 * @return          object reference for method chaining
	 */
	public RpgPlayer cleanModifier(PlayerModifier modifier);

	/**
	 * Gets a set of all active {@link PlayerModifier}s on the player.
	 *
	 * @return a set of {@link PlayerModifier}s
	 */
	public Set<PlayerModifier> getModifiers();

	/**
	 * Checks if the player is allowed to perform {@link RpgActionType} using <code>Material</code>.
	 *
	 * @param  action   the action to check
	 * @param  material the material being used
	 * @return          true if the action is permitted, false otherwise
	 */
	public boolean isAllowed(RpgActionType action, Material material);

	/**
	 * Adds a material to the list of permits for {@link RpgActionType}.
	 *
	 * @param  action   the action to modify
	 * @param  material the material being allowed
	 * @return          object reference for method chaining
	 */
	public RpgPlayer addAllowed(RpgActionType action, Material material);

	/**
	 * Adds a collection of <code>Material</code>s to the permit list for {@link RpgActionType}.
	 *
	 * @param  action    the action to modify
	 * @param  materials the collection of materials to allow
	 * @return           object reference for method chaining
	 */
	public RpgPlayer addAllowed(RpgActionType action, Collection<Material> materials);

	/**
	 * Removes a material from the list of permits for {@link RpgActionType}.
	 *
	 * @param  action   the action to modify
	 * @param  material the material to disallow
	 * @return          object reference for method chaining
	 */
	public RpgPlayer removeAllowed(RpgActionType action, Material material);

	/**
	 * Removes a collection of materials from the permit list for {@link RpgActionType}.
	 *
	 * @param  action    the action to modify
	 * @param  materials the collection of materials to disallow
	 * @return           object reference for method chaining
	 */
	public RpgPlayer removeAllowed(RpgActionType action, Collection<Material> materials);

	/**
	 * Gets the player's personal {@link ExperienceScaler}.
	 *
	 * This method should return null if no personal scaler is defined and the player is using the global
	 * scaler.
	 *
	 * @return the player's {@link ExperienceScaler}, or null if one does not exist
	 */
	public ExperienceScaler getExperienceScaler();

	/**
	 * Sets the player's personal {@link ExperienceScaler}.
	 *
	 * This scaler should be used for all internal leveling operations in place of the global scaler.
	 *
	 * @return object reference for method chaining
	 */
	public RpgPlayer setExperienceScaler(ExperienceScaler expScaler);

	/**
	 * Checks if the player has an {@link ExperienceScaler} set.
	 *
	 * @return true if the player is using a personal scaler, false otherwise
	 */
	public boolean hasExperienceScaler();

	/**
	 * Removes the player's personal {@link ExperienceScaler}.
	 *
	 * If the player does not have a personal scaler (as determined by RpgPlayer#hasExperienceScaler()) this method should silently fail.
	 *
	 * @return object reference for method chaining
	 */
	public RpgPlayer removeExperienceScaler();

	/**
	 * Gets the player's {@link RpgClass}.
	 *
	 * @return the {@link RpgClass} of the player
	 */
	public RpgClass getRpgClass();

	/**
	 * Saves non-transient player data to file.
	 *
	 * @return object reference for method chaining
	 */
	public RpgPlayer save();

	/**
	 * This method should ONLY be called when removing an RpgPlayer from the game.
	 *
	 * The goal of this method is to clean up any remaining tasks or outstanding events
	 * that was being utilized by the player.
	 *
	 * After this method is called, the {@link RpgPlayer} object <strong>is no longer guaranteed to be
	 * valid or viable</strong>. As such, any behavior after this method is called considered undefined.
	 */
	public void close();
}