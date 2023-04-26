package com.lonevox.renewableghasttears;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;

public class RenewableGhastTearsModClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);
	}
}
