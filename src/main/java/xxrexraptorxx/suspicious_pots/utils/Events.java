package xxrexraptorxx.suspicious_pots.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import xxrexraptorxx.suspicious_pots.main.References;
import xxrexraptorxx.suspicious_pots.main.SuspiciousPots;

@EventBusSubscriber(modid = References.MODID, bus = EventBusSubscriber.Bus.GAME)
public class Events {


    @SubscribeEvent
    public static void PotBreakEvent(BlockEvent.BreakEvent event) {
        Level level = event.getPlayer().level();
        BlockPos pos = event.getPos();
        Block block = level.getBlockState(pos).getBlock();

        if (block == Blocks.DECORATED_POT) {
            ItemStack stack = event.getPlayer().getMainHandItem();
            Block blockBelow = level.getBlockState(pos.below()).getBlock();

            if (stack.is(ItemTags.BREAKS_DECORATED_POTS) && (Config.BLOCKS_WHITE_LIST.get().isEmpty() || isBlockInWhiteList(blockBelow))) {
                SpawnHelper.SpawnCreature(level, pos);
            }
        }
    }


    private static boolean isBlockInWhiteList(Block block) {
        if (Config.DEBUG_MODE.get()) {
            SuspiciousPots.LOGGER.info("Pot placed above: " + BuiltInRegistries.BLOCK.getKey(block).toString());
        }

        for (String validBlock : Config.BLOCKS_WHITE_LIST.get()) {
            if (validBlock.equals(BuiltInRegistries.BLOCK.getKey(block).toString())) {
                return true;
            }
        }
        return false;
    }

}
