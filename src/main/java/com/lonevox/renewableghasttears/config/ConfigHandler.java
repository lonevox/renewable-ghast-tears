package com.lonevox.renewableghasttears.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lonevox.renewableghasttears.RenewableGhastTearsMod;
import com.lonevox.renewableghasttears.config.annotations.ConfigComment;
import com.moandjiezana.toml.TomlWriter;
import dev.isxander.yacl.config.ConfigInstance;
import dev.isxander.yacl.config.GsonConfigInstance;
import net.fabricmc.loader.api.FabricLoader;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class ConfigHandler {
	private static final Path TEMP_JSON_CONFIG_PATH;
	public static final Path CONFIG_PATH;
	private static final ConfigInstance<Config> CONFIG_INSTANCE;
	public static final Config CONFIG;
	public static final ConfigFieldData[] CONFIG_FIELD_DATA;

	static {
		var configDirectoryPath = FabricLoader.getInstance().getConfigDir();
		CONFIG_PATH = Paths.get(configDirectoryPath.toString(), "renewable-ghast-tears.toml");
		Path tempJsonConfigPath = null;
		ConfigInstance<Config> configInstance = null;
		Config config = null;
		ConfigFieldData[] configFieldData = null;
		try {
			tempJsonConfigPath = Files.createTempFile("renewable-ghast-tears-config", ".json");
			tempJsonConfigPath.toFile().deleteOnExit();
			var gson = new GsonBuilder()
					.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
					.create();
			configInstance = new GsonConfigInstance<>(Config.class, tempJsonConfigPath, gson);
			config = configInstance.getConfig();
			List<ConfigFieldData> configFieldDataList = new ArrayList<>();
			for (var field : Config.class.getFields()) {
				var tomlFieldName = gson.fieldNamingStrategy().translateName(field);
				var comment = field.getAnnotation(ConfigComment.class).value();
				configFieldDataList.add(new ConfigFieldData(field.getName(), tomlFieldName, comment));
			}
			configFieldData = configFieldDataList.toArray(new ConfigFieldData[0]);
		} catch (IOException e) {
			e.printStackTrace();
		}
		TEMP_JSON_CONFIG_PATH = tempJsonConfigPath;
		CONFIG_INSTANCE = configInstance;
		CONFIG = config;
		CONFIG_FIELD_DATA = configFieldData;
	}

	public static void save() {
		// Save the config as JSON
		CONFIG_INSTANCE.save();

		// Read the saved JSON file into a Map
		try (var reader = new FileReader(TEMP_JSON_CONFIG_PATH.toFile())) {
			Map<String, Object> intermediaryMap = new Gson().fromJson(reader, new TypeToken<Map<String, Object>>() {
			}.getType());
			// Write the intermediary map to the TOML config file
			try (var writer = new FileWriter(CONFIG_PATH.toFile())) {
				var tomlWriter = new TomlWriter();
				for (var entry : intermediaryMap.entrySet()) {
					// Write TOML comment if there is one
					var configFieldData = getConfigFieldDataFromTomlField(entry.getKey());
					if (configFieldData != null) {
						writer.write("# " + configFieldData.comment + "\n");
					}
					// Write single entry to TOML
					tomlWriter.write(Map.ofEntries(entry), writer);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the comment of a config field. Field must be annotated with {@link ConfigComment}.
	 *
	 * @param fieldName Name of the field in the config class.
	 * @return Comment on the field, null if no comment annotation exists.
	 */
	public static String getComment(String fieldName) {
		var configFieldData = getConfigFieldDataFromField(fieldName);
		if (configFieldData != null) {
			return configFieldData.comment;
		}
		RenewableGhastTearsMod.LOGGER.warn("No such field '" + fieldName + "'" + " with ConfigComment annotation in " + Config.class.getSimpleName());
		return null;
	}

	/**
	 * @param fieldName Name of the field in the config class.
	 * @return The ConfigFieldData for the given field.
	 */
	private static ConfigFieldData getConfigFieldDataFromField(String fieldName) {
		var comment = Arrays.stream(CONFIG_FIELD_DATA)
				.filter((c) -> c.fieldName.equals(fieldName))
				.findFirst();
		if (comment.isPresent()) {
			return comment.get();
		}
		RenewableGhastTearsMod.LOGGER.warn("No such field '" + fieldName + "'" + " in " + Config.class.getSimpleName());
		return null;
	}

	/**
	 * @param tomlFieldName Name of the field in the TOML file.
	 * @return The ConfigFieldData for the given TOML field.
	 */
	private static ConfigFieldData getConfigFieldDataFromTomlField(String tomlFieldName) {
		var comment = Arrays.stream(CONFIG_FIELD_DATA)
				.filter((c) -> c.tomlFieldName.equals(tomlFieldName))
				.findFirst();
		if (comment.isPresent()) {
			return comment.get();
		}
		RenewableGhastTearsMod.LOGGER.warn("No such TOML field '" + tomlFieldName + "'" + " in " + CONFIG_PATH);
		return null;
	}

	record ConfigFieldData(String fieldName, String tomlFieldName, String comment) {
	}
}
