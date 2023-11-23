package xxrexraptorxx.suspicious_pots.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import xxrexraptorxx.suspicious_pots.main.SuspiciousPots;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class SpawnHelper {

    /**
     * Spawns a creature based on the configuration settings at the specified position.
     *
     * @param level The world level.
     * @param pos   The position where the creature should be spawned.
     */
    public static void SpawnCreature(Level level, BlockPos pos) {
        List<String> spawnList = Config.SPAWNING_LIST.get();

        if (!level.isClientSide) {
            try {
                // Sort the spawnList based on spawnProbability
                spawnList.sort(new Comparator<String>() {
                    @Override
                    public int compare(String entry1, String entry2) {
                        try {
                            double spawnProbability1 = Double.parseDouble(entry1.split("-")[1]);
                            double spawnProbability2 = Double.parseDouble(entry2.split("-")[1]);

                            return Double.compare(spawnProbability1, spawnProbability2);

                        } catch (NumberFormatException e) {
                            SuspiciousPots.LOGGER.error("Error parsing spawn probability: " + e.getMessage(), e);
                            return 0; // Default value in case of error
                        }
                    }
                });

                // Iterate through the sorted spawnList
                for (String entry : spawnList) {
                    String[] parts = entry.split("-");

                    if (parts.length == 2) {
                        EntityType<?> entityType = EntityType.byString(parts[0]).orElse(null);
                        double spawnProbability = Double.parseDouble(parts[1]);
                        double random = Math.random();

                        if (Config.DEBUG_MODE.get()) {
                            SuspiciousPots.LOGGER.info("Random [" + (float) random + "] need to be less then spawn probability [" + spawnProbability + " (" + ConvertDecimalToPercentage(spawnProbability) + "%) for " + EntityTypeNameFormatter(entityType) + "]");
                        }

                        if (entityType != null && random < spawnProbability) {
                            spawnEntityAtLocation(entityType, level, pos);

                            if (Config.DEBUG_MODE.get())
                                SuspiciousPots.LOGGER.info(EntityTypeNameFormatter(entityType) + " spawned successfully!");
                            return; // Spawned entity, exit the loop
                        }
                    }
                }

            } catch (Exception e) {
                SuspiciousPots.LOGGER.error(e.getMessage());
            }
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
            if (entity instanceof AgeableMob) {
                AgeableMob ageableEntity = (AgeableMob) entity;
                ageableEntity.setBaby(true);
            }
            if (entity instanceof Zombie) {
                Zombie zombie = (Zombie) entity;
                zombie.setBaby(true);
            }
            if (entity instanceof Slime) {
                Slime slime = (Slime) entity;
                slime.setSize(1, false);
            }

            level.playSound((Player) null, pos, SoundEvents.PLAYER_BIG_FALL, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.15F + 1.0F);
            entity.setPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
            level.addFreshEntity(entity);
        }
    }


    public static double ConvertDecimalToPercentage(double decimal) {
        return decimal > 1 ? 100 : decimal * 100;
    }


    public static String EntityTypeNameFormatter(EntityType entityType) {
        return entityType.toString().replace("entity.", "").replace("." ,":");
    }
}
