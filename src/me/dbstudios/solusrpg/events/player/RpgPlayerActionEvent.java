package me.dbstudios.solusrpg.events.player;

import me.dbstudios.solusrpg.entities.player.RpgPlayer;

import org.bukkit.Material;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * RpgPlayerActionEvents are odd in the sense that they BEGIN in a cancelled state.
 *
 * Internally, Solus assumes that all player actions are invalid until proven otherwise. By doing so,
 * the system gives user-defined auxiliary stats and abilities (which should be the most common
 * listeners to this event and any subclasses) a chance to say "Oh hey, the player should be able
 * to craft X or attack with Y because he has rank Z of me!!".
 *
 * At the time the event is dispatched, the described action HAS NOT OCURRED. Thus, cancelling
 * the event will result in crafting being prevented, damage dealt or received being mitigated,
 * etc.
 *
 * For handling specialized actions (such as applying enchantments after crafting or modifying
 * damage output) please see the other RpgPlayer*ActionEvent classes.
 */
public class RpgPlayerBeforeActionEvent extends RpgPlayerEvent implements Cancellable {
	private final RpgActionType action;
	private final Material item;

	private boolean cancelled = true;
	private boolean dirty = false;

	public RpgPlayerActionEvent(RpgPlayer player, RpgActionType action, Material item) {
		super(player);

		this.action = action;
		this.item = item;
	}

	public RpgActionType getAction() {
		return this.action;
	}

	public Material getMaterial() {
		return this.item;
	}

	/**
	 * Checks to see if this event has been modified yet. An event is considered modified if a listener modified it's
	 * cancelled state.
	 *
	 * This can be used to determine if the event is "fresh", meaning that nothing has said the player is or is not
	 * allowed to continue with the specified action.
	 *
	 * @return whether or not this event has been altered
	 */
	public boolean isDirty() {
		return this.dirty;
	}

	public boolean isCancelled() {
		return this.cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
		this.dirty = true;
	}
}