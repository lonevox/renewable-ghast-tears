package com.lonevox.renewableghasttears.config;

import com.lonevox.renewableghasttears.config.annotations.ConfigComment;
import dev.isxander.yacl.config.ConfigEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public final class Config {
	@ConfigEntry
	@ConfigComment("The delay between Ghasts dropping a Ghast Tear in ticks (1 second = 20 ticks). 1200 by default (1 minute).")
	public int ghastTearDelayTicksFlat = 1200;
	@ConfigEntry
	@ConfigComment("A random additional delay between Ghasts dropping a Ghast Tear in ticks (up to 1 second = 20 ticks). 1200 by default (up to 1 minute).")
	public int ghastTearDelayTicksRandom = 1200;
	@ConfigEntry
	@ConfigComment("If a Ghast is crying it will replace the normal Ghast texture with a visibly crying variant. True by default.")
	public boolean useCryingTexture = true;
	@ConfigEntry
	@ConfigComment("Worlds where Ghasts will cry. Only works if World Blacklist is empty. Empty by default.")
	public List<RegistryKey<World>> worldWhitelist = new ArrayList<>();
	@ConfigEntry
	@ConfigComment("Worlds where Ghasts won't cry. Only works if World Whitelist is empty. Empty by default.")
	public List<RegistryKey<World>> worldBlacklist = new ArrayList<>();
}
