package xxrexraptorxx.suspicious_pots.utils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;
import xxrexraptorxx.magmacore.config.ConfigHelper;

import java.util.Arrays;
import java.util.List;

public class Config {

    private static final ModConfigSpec.Builder SERVER_BUILDER = new ModConfigSpec.Builder();
    public static ModConfigSpec SERVER_CONFIG;

    private static ModConfigSpec.BooleanValue DEBUG_MODE;
    private static ModConfigSpec.DoubleValue SILVERFISH_GROUP_SPAWN_PROBABILITY;
    private static ModConfigSpec.ConfigValue<List<? extends String>> SPAWNING_LIST;
    private static ModConfigSpec.ConfigValue<List<? extends String>> BLOCKS_WHITE_LIST;


    static {
        ConfigHelper.setCategory(SERVER_BUILDER, "general");
        DEBUG_MODE = SERVER_BUILDER.comment("Enables the Debug Mode. (shows you the spawn values & probabilities in the server console)").define("debug_mode", false);
        SERVER_BUILDER.pop();

        ConfigHelper.setCategory(SERVER_BUILDER, "spawn_settings");
        SPAWNING_LIST = SERVER_BUILDER.comment("A list with all the mobs that can spawn from a broken pot [id:entity-probability]").defineListAllowEmpty("spawning_list",
                        Arrays.asList(
                                "minecraft:bat-0.03",
                                "minecraft:endermite-0.06",
                                "minecraft:slime-0.05",
                                "minecraft:cave_spider-0.03",
                                "minecraft:husk-0.05",
                                "minecraft:vex-0.008",
                                "minecraft:cat-0.005",
                                "minecraft:silverfish-0.08"
                        ), () -> "id:entity-probability",  // default
                        obj -> obj instanceof String string && isValidEntitySpawning(string)
                );
        BLOCKS_WHITE_LIST = SERVER_BUILDER.comment("Only allows mobs to spawn if the pots are above a certain block. Useful if you want the mobs to only spawn in pots of certain structures. [id:block] (use 'minecraft:oxidized_copper' for trial chambers)").defineListAllowEmpty("blocks_white_list",
                        Arrays.asList(), () -> "id:block", obj -> obj instanceof String string && isValidBlock(string));
        SILVERFISH_GROUP_SPAWN_PROBABILITY = SERVER_BUILDER.comment("This is the probability that when a silverfish spawns from a broken pot, that other silverfish from nearby pots are spawning.").defineInRange("silverfish_group_spawn_probability", 0.1, 0.0, 1.0);
        SERVER_BUILDER.pop();

        SERVER_CONFIG = SERVER_BUILDER.build();
    }


    public static boolean isDebugModeEnabled() { return DEBUG_MODE.get() || xxrexraptorxx.magmacore.config.Config.getDebugMode(); }
    public static double getSilverfishGroupSpawnProbability() { return SILVERFISH_GROUP_SPAWN_PROBABILITY.get(); }
    public static List<String>  getSpawningList() { return (List<String>) SPAWNING_LIST.get(); }
    public static List<String> getBlockWhiteList() { return (List<String>) BLOCKS_WHITE_LIST.get(); }


    /**
     * Validates if a block string exists in Minecraft's registry
     *
     * @param blockString The block string to validate (e.g. "minecraft:stone")
     * @return true if the block exists, false otherwise
     */
    public static boolean isValidBlock(String blockString) {
        if (blockString == null || blockString.trim().isEmpty() || !blockString.contains(":")) {
            return false;
        }

        try {
            ResourceLocation location = ResourceLocation.parse(blockString.trim());
            return BuiltInRegistries.BLOCK.containsKey(location);
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * Validates a string with probability format: "id-probability"
     *
     * @param input The input string (e.g. "minecraft:bat-0.03")
     * @return true if format is valid, false otherwise
     */
    public static boolean hasValidProbabilityFormat(String input) {
        if (input == null || input.trim().isEmpty() || !input.contains("-")) {
            return false;
        }

        try {
            int lastDashIndex = input.trim().lastIndexOf("-");
            if (lastDashIndex <= 0 || lastDashIndex >= input.trim().length() - 1) {
                return false;
            }

            String probabilityPart = input.trim().substring(lastDashIndex + 1);
            double probability = Double.parseDouble(probabilityPart);
            return probability >= 0.0 && probability <= 1.0;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * Extracts the ID part from a probability string
     *
     * @param input The input string (e.g. "minecraft:bat-0.03")
     * @return the ID part (e.g. "minecraft:bat") or null if invalid
     */
    public static String extractId(String input) {
        if (!hasValidProbabilityFormat(input)) {
            return null;
        }

        int lastDashIndex = input.trim().lastIndexOf("-");
        return input.trim().substring(0, lastDashIndex);
    }


    /**
     * Validates entity spawning string: "namespace:entity-probability"
     */
    public static boolean isValidEntitySpawning(String input) {
        if (!input.contains(":") || !hasValidProbabilityFormat(input)) {
            return false;
        }

        String entityId = extractId(input);

        if (entityId == null) return false;
        try {
            ResourceLocation location = ResourceLocation.parse(entityId);

            return BuiltInRegistries.ENTITY_TYPE.containsKey(location);

        } catch (Exception e) {
            return false;
        }
    }

}