package xxrexraptorxx.suspicious_pots.utils;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber
public class Config {

    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_POTS = "pots";

    public static ForgeConfigSpec CLIENT_CONFIG;
    public static ForgeConfigSpec SERVER_CONFIG;

    public static ForgeConfigSpec.BooleanValue UPDATE_CHECKER;
    public static ForgeConfigSpec.BooleanValue PATREON_REWARDS;

    public static ForgeConfigSpec.BooleanValue ONLY_IN_TRIAL_CHAMBERS;
    public static ForgeConfigSpec.BooleanValue DEBUG_MODE;

    public static ForgeConfigSpec.ConfigValue<List<String>> SPAWNING_LIST;


    public static void init() {
        initServer();
        initClient();

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_CONFIG);
    }


    public static void initClient() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("General").push(CATEGORY_GENERAL);
        UPDATE_CHECKER = builder.comment("Activate the Update-Checker").define("update-checker", true);
        builder.pop();

        CLIENT_CONFIG = builder.build();
    }

    public static void initServer() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("General").push(CATEGORY_GENERAL);
        PATREON_REWARDS = builder.comment("Enables ingame rewards on first spawn for Patreons").define("patreon_rewards", true);
        builder.pop();

        builder.comment("Pots").push(CATEGORY_POTS);
        ONLY_IN_TRIAL_CHAMBERS = builder.comment("Mobs can only spawn in pots from Trial Chambers").define("only_in_trial_chambers", false);
        SPAWNING_LIST = builder.comment("A list with all the mobs that can spawn from a broken pot [id:entity-probability] (probability is written in decimal. 1.0 = 100%, 0.5 = 50%, 0.03 = 3%)").define("spawning_list", new ArrayList<>(Arrays.asList(
                ForgeRegistries.ENTITY_TYPES.getKey(EntityType.BAT).toString() + "-0.03",
                ForgeRegistries.ENTITY_TYPES.getKey(EntityType.ENDERMITE).toString() + "-0.06",
                ForgeRegistries.ENTITY_TYPES.getKey(EntityType.SLIME).toString() + "-0.05",
                ForgeRegistries.ENTITY_TYPES.getKey(EntityType.CAVE_SPIDER).toString() + "-0.03",
                ForgeRegistries.ENTITY_TYPES.getKey(EntityType.HUSK).toString() + "-0.05",
                ForgeRegistries.ENTITY_TYPES.getKey(EntityType.VEX).toString() + "-0.008",
                ForgeRegistries.ENTITY_TYPES.getKey(EntityType.CAT).toString() + "-0.005",
                ForgeRegistries.ENTITY_TYPES.getKey(EntityType.SILVERFISH).toString() + "-0.08"
        )));
        DEBUG_MODE = builder.comment("Enables the Debug Mode. (shows you the spawn values & probabilities in the server console)").define("debug_mode", false);
        builder.pop();

        SERVER_CONFIG = builder.build();
    }

}