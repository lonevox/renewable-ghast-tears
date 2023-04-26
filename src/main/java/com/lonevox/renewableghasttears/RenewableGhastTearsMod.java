package com.lonevox.renewableghasttears;

import net.fabricmc.api.ModInitializer;
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
		GhastEntityCryingHandler.registerEvents();
	}
}
