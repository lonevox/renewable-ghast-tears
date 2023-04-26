package com.lonevox.renewableghasttears;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RenewableGhastTearsMod implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("renewable-ghast-tears");

	public static Identifier id(String name) {
		return new Identifier("renewableghasttears", name);
	}

	@Override
	public void onInitialize() {
		// Register callback for Ghast loading.
		ServerEntityEvents.ENTITY_LOAD.register((entity, world) ->
		{
			// If the spawned entity is a ghast and the ghast is in a dimension that it is configured to cry in, make it cry.
			if (entity instanceof GhastEntityCryingAccessor ghastEntityCryingAccessor) {
				RenewableGhastTearsMod.LOGGER.debug("ghast loaded");
				if (entity.getEntityWorld().getRegistryKey().getValue().equals(new Identifier("overworld"))) {
					ghastEntityCryingAccessor.setCrying(true);
				} else {
					ghastEntityCryingAccessor.setCrying(false);
				}
				RenewableGhastTearsMod.LOGGER.info("crying: " + ghastEntityCryingAccessor.isCrying() + " id: " + entity.getUuidAsString());
			}
		});
	}
}
