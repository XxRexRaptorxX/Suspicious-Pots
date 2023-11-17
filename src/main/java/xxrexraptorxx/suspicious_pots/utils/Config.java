package xxrexraptorxx.suspicious_pots.utils;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.util.List;

@Mod.EventBusSubscriber
public class Config {

    public static final String CATEGORY_GENERAL = "general";

    public static ForgeConfigSpec CLIENT_CONFIG;
    public static ForgeConfigSpec SERVER_CONFIG;

    public static ForgeConfigSpec.BooleanValue UPDATE_CHECKER;
    public static ForgeConfigSpec.BooleanValue PATREON_REWARDS;

    public static ForgeConfigSpec.DoubleValue UPCYCLE_CHANCE;
    public static ForgeConfigSpec.ConfigValue<List<String>> COMPOSTABLE_ITEMS;


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
        UPCYCLE_CHANCE = builder.comment("The chance for an item to add a level of compost").defineInRange("upcycle_chance", 0.3F, 0.1F, 1.0F);
        PATREON_REWARDS = builder.comment("Enables ingame rewards on first spawn for Patreons").define("patreon_rewards", true);

        /** TODO
        COMPOSTABLE_ITEMS = builder.comment("A list with all compostable items [modid:item]").define("compostable_items", new ArrayList<>(Arrays.asList(
                Items.ROTTEN_FLESH.getRegistryName().toString(),
                Items.SPIDER_EYE.getRegistryName().toString(),
                Items.FERMENTED_SPIDER_EYE.getRegistryName().toString(),
                Items.POISONOUS_POTATO.getRegistryName().toString(),
                Items.RABBIT_FOOT.getRegistryName().toString()
        )));
        **/
        builder.pop();

        SERVER_CONFIG = builder.build();
    }

}