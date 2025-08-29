package xxrexraptorxx.suspicious_pots.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.world.item.DyeColor;
import net.neoforged.neoforge.common.ModConfigSpec;
import xxrexraptorxx.magmacore.config.ConfigHelper;
import xxrexraptorxx.magmacore.config.ConfigListHelper;

public class Config {

    private static final ModConfigSpec.Builder SERVER_BUILDER = new ModConfigSpec.Builder();
    public static ModConfigSpec SERVER_CONFIG;

    private static ModConfigSpec.BooleanValue DEBUG_MODE;
    private static ModConfigSpec.DoubleValue SILVERFISH_GROUP_SPAWN_PROBABILITY;
    private static ModConfigSpec.ConfigValue<List<? extends String>> SPAWNING_LIST;
    private static ModConfigSpec.ConfigValue<List<? extends String>> BLOCKS_WHITE_LIST;
    private static ModConfigSpec.ConfigValue<List<? extends String>> SPAWN_BLOCKS;

    static {
        ConfigHelper.setCategory(SERVER_BUILDER, "general");
        DEBUG_MODE = SERVER_BUILDER
                .comment("Enables the Debug Mode. (shows you the spawn values &"
                        + " probabilities in the server console)")
                .define("debug_mode", false);
        SERVER_BUILDER.pop();

        ConfigHelper.setCategory(SERVER_BUILDER, "spawn_settings");
        SPAWN_BLOCKS = SERVER_BUILDER
                .comment("A list of all blocks that should have the ability to spawn" + " monsters.")
                .defineListAllowEmpty(
                        "spawn_blocks",
                        makeSpawnBlockList(),
                        () -> "id:block",
                        obj -> obj instanceof String string && ConfigListHelper.isValidBlock(string));
        SPAWNING_LIST = SERVER_BUILDER
                .comment("A list with all the mobs that can spawn from a broken pot" + " [id:entity-probability]")
                .defineListAllowEmpty(
                        "spawning_list",
                        Arrays.asList(
                                "minecraft:bat-0.03",
                                "minecraft:endermite-0.06",
                                "minecraft:slime-0.05",
                                "minecraft:cave_spider-0.03",
                                "minecraft:husk-0.05",
                                "minecraft:vex-0.008",
                                "minecraft:cat-0.005",
                                "minecraft:silverfish-0.08"),
                        () -> "id:entity-probability", // default
                        obj -> obj instanceof String string && ConfigListHelper.isValidEntityWithProbability(string));
        BLOCKS_WHITE_LIST = SERVER_BUILDER
                .comment("Only allows mobs to spawn if the pots are above a certain block."
                        + " Useful if you want the mobs to only spawn in pots of"
                        + " certain structures. [id:block] (use"
                        + " 'minecraft:oxidized_copper' for trial chambers)")
                .defineListAllowEmpty(
                        "blocks_white_list",
                        Arrays.asList(),
                        () -> "id:block",
                        obj -> obj instanceof String string && ConfigListHelper.isValidBlock(string));
        SILVERFISH_GROUP_SPAWN_PROBABILITY = SERVER_BUILDER
                .comment("This is the probability that when a silverfish spawns from a"
                        + " broken pot, that other silverfish from nearby pots are"
                        + " spawning.")
                .defineInRange("silverfish_group_spawn_probability", 0.1, 0.0, 1.0);
        SERVER_BUILDER.pop();

        SERVER_CONFIG = SERVER_BUILDER.build();
    }

    public static boolean isDebugModeEnabled() {
        return DEBUG_MODE.get() || xxrexraptorxx.magmacore.config.Config.getDebugMode();
    }

    public static double getSilverfishGroupSpawnProbability() {
        return SILVERFISH_GROUP_SPAWN_PROBABILITY.get();
    }

    public static List<String> getSpawningList() {
        return (List<String>) SPAWNING_LIST.get();
    }

    public static List<String> getSpawnBlocks() {
        return (List<String>) SPAWN_BLOCKS.get();
    }

    public static List<String> getBlockWhiteList() {
        return (List<String>) BLOCKS_WHITE_LIST.get();
    }

    private static List<String> addColoredStuff(String base) {
        List<String> result = new ArrayList<>();
        for (DyeColor dye : DyeColor.values()) {
            result.add(base + "_" + dye.getName());
        }
        return result;
    }

    private static List<String> makeSpawnBlockList() {
        List<String> list = new ArrayList<>();

        list.add("minecraft:decorated_pot");
        list.add("pottery:base_pot_blank");
        list.add("pottery:wide_pot_blank");
        list.add("pottery:tall_pot_blank");
        list.add("simply_pots:surface_loot_pot");
        list.add("simply_pots:desert_loot_pot");
        list.add("simply_pots:jungle_loot_pot");
        list.add("simply_pots:cave_loot_pot");
        list.add("simply_pots:lush_loot_pot");
        list.add("simply_pots:sculk_loot_pot");
        list.add("archeological:dark_urn");
        list.add("archeological:content_dark_urn");
        list.add("archeological:fear_dark_urn");
        list.add("archeological:fearless_dark_urn");
        list.add("archeological:grumpy_urn");
        list.add("archeological:surprise_urn");
        list.add("archeological:dark_urn");
        list.add("supplementaries:urn");

        list.addAll(addColoredStuff("pottery:default_pot"));
        list.addAll(addColoredStuff("pottery:base_pot"));
        list.addAll(addColoredStuff("pottery:wide_pot"));
        list.addAll(addColoredStuff("pottery:tall_pot"));

        return list;
    }
}
