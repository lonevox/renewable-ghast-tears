package com.lonevox.renewableghasttears.integration;

import com.lonevox.renewableghasttears.config.ConfigHandler;
import dev.isxander.yacl.api.ConfigCategory;
import dev.isxander.yacl.api.ListOption;
import dev.isxander.yacl.api.Option;
import dev.isxander.yacl.api.YetAnotherConfigLib;
import dev.isxander.yacl.gui.controllers.TickBoxController;
import dev.isxander.yacl.gui.controllers.string.StringController;
import dev.isxander.yacl.gui.controllers.string.number.IntegerFieldController;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class YetAnotherConfigLibIntegration {
	private static List<String> getWorldListAsStringList(List<RegistryKey<World>> worldList) {
		return worldList.stream()
				.map((key) -> key.getValue().getPath())
				.collect(Collectors.toList());
	}

	private static List<RegistryKey<World>> getStringListAsWorldList(List<String> stringList) {
		return stringList.stream()
				.map((string) -> RegistryKey.of(Registry.WORLD_KEY, new Identifier(string)))
				.collect(Collectors.toList());
	}

	public static Screen createConfigScreen(Screen parent) {
		return YetAnotherConfigLib.createBuilder()
				.title(Text.literal("Renewable Ghast Tears Config"))
				.category(ConfigCategory.createBuilder()
						.name(Text.literal("General"))
						.option(Option.createBuilder(int.class)
								.name(Text.literal("Ghast Tear Delay"))
								.tooltip(Text.literal(ConfigHandler.getComment("ghastTearDelayTicksFlat")))
								.binding(1200, () -> ConfigHandler.CONFIG.ghastTearDelayTicksFlat, newVal -> ConfigHandler.CONFIG.ghastTearDelayTicksFlat = newVal)
								.controller(IntegerFieldController::new)
								.build())
						.option(Option.createBuilder(int.class)
								.name(Text.literal("Ghast Tear Random Delay"))
								.tooltip(Text.literal(ConfigHandler.getComment("ghastTearDelayTicksRandom")))
								.binding(1200, () -> ConfigHandler.CONFIG.ghastTearDelayTicksRandom, newVal -> ConfigHandler.CONFIG.ghastTearDelayTicksRandom = newVal)
								.controller(IntegerFieldController::new)
								.build())
						.option(Option.createBuilder(boolean.class)
								.name(Text.literal("Use Crying Texture"))
								.tooltip(Text.literal(ConfigHandler.getComment("useCryingTexture")))
								.binding(true, () -> ConfigHandler.CONFIG.useCryingTexture, newVal -> ConfigHandler.CONFIG.useCryingTexture = newVal)
								.controller(TickBoxController::new)
								.build())
						.option(ListOption.createBuilder(String.class)
								.name(Text.literal("World Whitelist"))
								.tooltip(Text.literal(ConfigHandler.getComment("worldWhitelist")))
								.binding(new ArrayList<>(), () -> getWorldListAsStringList(ConfigHandler.CONFIG.worldWhitelist), newVal -> ConfigHandler.CONFIG.worldWhitelist = getStringListAsWorldList(newVal))
								.controller(StringController::new)
								.initial("")
								.build())
						.option(ListOption.createBuilder(String.class)
								.name(Text.literal("World Blacklist"))
								.tooltip(Text.literal(ConfigHandler.getComment("worldBlacklist")))
								.binding(new ArrayList<>(), () -> getWorldListAsStringList(ConfigHandler.CONFIG.worldBlacklist), newVal -> ConfigHandler.CONFIG.worldBlacklist = getStringListAsWorldList(newVal))
								.controller(StringController::new)
								.initial("")
								.build())
						.build())
				.save(ConfigHandler::save)
				.build()
				.generateScreen(parent);
	}
}
