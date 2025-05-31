package xxrexraptorxx.suspicious_pots.utils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Config {

    public static final String CATEGORY_POTS = "pots";

    public static ModConfigSpec SERVER_CONFIG;

    public static ModConfigSpec.BooleanValue DEBUG_MODE;
    public static ModConfigSpec.DoubleValue SILVERFISH_GROUP_SPAWN_PROBABILITY;
    public static ModConfigSpec.ConfigValue<List<String>> SPAWNING_LIST;
    public static ModConfigSpec.ConfigValue<List<String>> BLOCKS_WHITE_LIST;


    public static void init(ModContainer container) {
        initServer();

        container.registerConfig(ModConfig.Type.SERVER, SERVER_CONFIG);
    }


    public static void initServer() {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.comment("Pots").push(CATEGORY_POTS);
        SPAWNING_LIST = builder.comment("A list with all the mobs that can spawn from a broken pot [id:entity-probability] (probability is written in decimal. 1.0 = 100%, 0.5 = 50%, 0.03 = 3%)").define("spawning_list", new ArrayList<>(Arrays.asList(
                BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.BAT).toString() + "-0.03",
                BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.ENDERMITE).toString() + "-0.06",
                BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.SLIME).toString() + "-0.05",
                BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.CAVE_SPIDER).toString() + "-0.03",
                BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.HUSK).toString() + "-0.05",
                BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.VEX).toString() + "-0.008",
                BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.CAT).toString() + "-0.005",
                BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.SILVERFISH).toString() + "-0.08"
        )));
        BLOCKS_WHITE_LIST = builder.comment("Only lets mob spawn if the pots are on a certain block. Useful if you want the mobs to only spawn in pots of certain structures. [id:block] (use 'minecraft:oxidized_copper' for trial chambers)").define("blocks_white_list", new ArrayList<>(Arrays.asList()));
        SILVERFISH_GROUP_SPAWN_PROBABILITY = builder.comment("This is the probability that when a silverfish spawns from a broken pot, that other silverfish from nearby pots are spawning.").defineInRange("silverfish_group_spawn_probability", 0.1, 0.0, 1.0);
        DEBUG_MODE = builder.comment("Enables the Debug Mode. (shows you the spawn values & probabilities in the server console)").define("debug_mode", false);
        builder.pop();

        SERVER_CONFIG = builder.build();
    }

}