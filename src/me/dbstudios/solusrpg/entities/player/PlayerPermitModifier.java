package me.dbstudios.solusrpg.entities.player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.events.player.RpgActionType;

import org.bukkit.Bukkit;
import org.bukkit.Material;

public class PlayerPermitModifier implements PlayerModifier {
	private final Set<Material> permits = new HashSet<>();
	private final RpgActionType type;

	public PlayerPermitModifier(RpgActionType type, List<String> items) {
		this.type = type;

		for (String p : items) {
			Material m = Material.matchMaterial(p);

			if (m == null) {
				SolusRpg.log(Level.WARNING, String.format("Invalid material name '%s' in PlayerPermitModifier of type %s; it has been removed, but it is recommended that you fix the configuration", p, type.name()));

				continue;
			}

			if (!permits.contains(m))
				permits.add(m);
			else
				SolusRpg.log(Level.WARNING, String.format("Duplicate material name (%s) detected in PlayerPermitModifier of type %s; it has been skipped, but it is recommended you remove it from the configuration", m.name(), type.name()));
		}
	}

	public RpgActionType getActionType() {
		return this.type;
	}

	public Set<Material> getPermitSet() {
		return this.permits;
	}

	public void modify(RpgPlayer player) {
		player.addAllowed(this.getActionType(), this.getPermitSet());
	}

	public void modify(RpgPlayer player, long duration) {
		this.modify(player);

		Bukkit.getScheduler().scheduleSyncDelayedTask(SolusRpg.getInstance(), new ModifierUndoTask(this, player), duration);
	}

	public void unmodify(RpgPlayer player) {
		player.removeAllowed(this.getActionType(), this.getPermitSet());
	}
}