package xxrexraptorxx.suspicious_pots.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import xxrexraptorxx.suspicious_pots.main.SuspiciousPots;

import java.util.List;

public class SpawnHelper {

    /**
     * Spawns a creature based on the configuration settings at the specified position.
     *
     * @param level The world level.
     * @param pos   The position where the creature should be spawned.
     */
    public static void SpawnCreature(Level level, BlockPos pos) {
        List<String> spawnList = Config.SPAWNING_LIST.get();

        try {
            // Sort the spawnList based on spawnProbability
            spawnList.sort((entry1, entry2) -> {
                double spawnProbability1 = Double.parseDouble(entry1.split("-")[1]);
                double spawnProbability2 = Double.parseDouble(entry2.split("-")[1]);
                return Double.compare(spawnProbability1, spawnProbability2);
            });

            // Iterate through the sorted spawnList
            for (String entry : spawnList) {
                String[] parts = entry.split("-");
                if (parts.length == 2) {
                    EntityType<?> entityType = EntityType.byString(parts[0]).orElse(null);
                    double spawnProbability = Double.parseDouble(parts[1]);

                    if (entityType != null && Math.random() < spawnProbability) {
                        spawnEntityAtLocation(entityType, level, pos);
                        return; // Spawned entity, exit the loop
                    }
                }
            }

        } catch (Exception e) {
            SuspiciousPots.LOGGER.error(e.getMessage());
        }
    }

    /**
     * Spawns the specified entity type at the given position in the world.
     *
     * @param entityType The type of entity to spawn.
     * @param level      The world level.
     * @param pos        The position where the entity should be spawned.
     */
    private static void spawnEntityAtLocation(EntityType<?> entityType, Level level, BlockPos pos) {
        Entity entity = entityType.create(level);
        if (entity != null) {
            entity.moveTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, level.random.nextFloat() * 360.0F, 0.0F);
            level.addFreshEntity(entity);
        }
    }
}