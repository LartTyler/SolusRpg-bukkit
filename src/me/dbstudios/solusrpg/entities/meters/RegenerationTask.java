package me.dbstudios.solusrpg.entities.meters;

import org.bukkit.scheduler.BukkitRunnable;

public class RegenerationTask extends BukkitRunnable {
	private final VitalMeter meter;

	public RegenerationTask(VitalMeter meter) {
		this.meter = meter;

		if (meter.getRegenRate() <= 0)
			throw new UnsupportedOperationException("Meters cannot regenerate at a rate less than or equal to zero.");
	}

	public void run() {
		meter.heal(meter.getRegenAmount());
	}
}