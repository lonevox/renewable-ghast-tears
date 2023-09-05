package com.lonevox.renewableghasttears;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

/**
 * This class exists because Ghast has no tick() method to hook into without excessive mixin magic. In pursuit of
 * keeping things simple, this class maintains a set of all Ghasts and makes sure they cry Ghast Tears when needed
 * by calling cryGhastTears() on each Ghast every tick.
 */
public class GhastEntityCryingHandler {
	private static final Set<GhastEntity> ghastEntities = new HashSet<>();

	/**
	 * Must be called in ModInitializer.onInitialize().
	 */
	public static void registerEvents() {
		// Register callback for Ghast loading.
		ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
			if (entity instanceof GhastEntity ghastEntity) {
				ghastEntities.add(ghastEntity);
				updateGhastCryingState(ghastEntity);
			}
		});

		// Register callback for Ghast unloading.
		ServerEntityEvents.ENTITY_UNLOAD.register((entity, world) -> {
			if (entity instanceof GhastEntity ghastEntity) {
				ghastEntities.remove(ghastEntity);
			}
		});

		// Register callback for spawning Ghast tears.
		ServerTickEvents.START_WORLD_TICK.register(__ -> cryGhastTears());
	}

	/**
	 * Updates a Ghast's crying state based on the Ghast's current state and the config.
	 * @param ghastEntity The Ghast to update the crying state of.
	 */
	private static void updateGhastCryingState(GhastEntity ghastEntity) {
		// If the ghast is in a dimension that it is configured to cry in, make it cry.
		var worldKey = ghastEntity.getEntityWorld().getRegistryKey();
		var ghastEntityAccessor = (GhastEntityAccessor)ghastEntity;
		ghastEntityAccessor.setCrying(worldKey.equals(World.OVERWORLD));
		RenewableGhastTearsMod.LOGGER.debug("crying: " + ghastEntityAccessor.isCrying() + " id: " + ghastEntity.getUuidAsString());
	}

	/**
	 * Tries to make all known Ghasts cry.
	 */
	private static void cryGhastTears() {
		for (var ghastEntity : ghastEntities) {
			if (ghastEntity instanceof GhastEntityAccessor ghastEntityAccessor) {
				ghastEntityAccessor.tryCryGhastTear();
			}
		}
	}
}
