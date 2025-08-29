package xxrexraptorxx.suspicious_pots.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import xxrexraptorxx.suspicious_pots.main.SuspiciousPots;

import java.util.Comparator;
import java.util.List;

public class SpawnHelper {

    /**
     * Spawns a creature based on the configuration settings at the specified position.
     *
     * @param level The world level.
     * @param pos   The position where the creature should be spawned.
     */
    public static void SpawnCreature(Level level, BlockPos pos) {
        if (Config.isDebugModeEnabled()) SuspiciousPots.LOGGER.info("Try to spawn creature!");

        if (!level.isClientSide) {
            List<String> spawnList = Config.getSpawningList();

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

                        if (Config.isDebugModeEnabled()) {
                            SuspiciousPots.LOGGER.info(
                                    "Random [" + (float) random + "] must to be less then spawn probability ["
                                            + spawnProbability + " (" + ConvertDecimalToPercentage(spawnProbability)
                                            + "%) for " + EntityTypeNameFormatter(entityType) + "]");
                        }

                        if (entityType != null && random < spawnProbability) {
                            if (Config.isDebugModeEnabled())
                                SuspiciousPots.LOGGER.info(
                                        EntityTypeNameFormatter(entityType) + " spawned successfully!");

                            spawnEntityAtLocation(entityType, level, pos);
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
        Entity entity = entityType.create(level, EntitySpawnReason.TRIGGERED);

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
            if (entity instanceof Silverfish && Config.getSilverfishGroupSpawnProbability() != 0) {
                SpawnSilverfishGroup(level, pos);
            }

            level.playSound(
                    (Player) null,
                    pos,
                    SoundEvents.DECORATED_POT_HIT,
                    SoundSource.BLOCKS,
                    1.0F,
                    level.random.nextFloat() * 0.15F + 1.0F);
            entity.setPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
            level.addFreshEntity(entity);
        }
    }

    private static void SpawnSilverfishGroup(Level level, BlockPos pos) {
        int searchRadius = 5;

        // sets the start position
        int posX = pos.getX();
        int posY = pos.getY();
        int posZ = pos.getZ();

        // changes the tested position
        for (int x = -searchRadius; x <= searchRadius; x++) {

            for (int y = -searchRadius; y <= searchRadius; y++) {

                for (int z = -searchRadius; z <= searchRadius; z++) {
                    BlockPos block = new BlockPos(posX + x, posY + y, posZ + z);

                    // tests if current block is a pot and it's not the start block
                    if (level.getBlockState(block).getBlock() == Blocks.DECORATED_POT && pos != block) {
                        double random = Math.random();

                        if (Config.isDebugModeEnabled()) {
                            SuspiciousPots.LOGGER.info("Additional silverfish spawning for Pot at the position: ["
                                    + block.getX() + ", " + block.getY() + ", " + block.getZ() + "]");
                            SuspiciousPots.LOGGER.info("Random [" + (float) random
                                    + "] must to be less then spawn probability ["
                                    + Config.getSilverfishGroupSpawnProbability() + " ("
                                    + ConvertDecimalToPercentage(Config.getSilverfishGroupSpawnProbability()) + "%)]");
                        }

                        // entity spawning
                        if (Config.getSilverfishGroupSpawnProbability() > random) {
                            if (Config.isDebugModeEnabled())
                                SuspiciousPots.LOGGER.info("Additional silverfish spawned successfully!");

                            level.playSound(
                                    (Player) null,
                                    block,
                                    SoundEvents.DECORATED_POT_HIT,
                                    SoundSource.BLOCKS,
                                    1.0F,
                                    level.random.nextFloat() * 0.15F + 1.0F);
                            Silverfish entity = new Silverfish(EntityType.SILVERFISH, level);
                            entity.setPos(block.getX() + 0.5F, block.getY() + 1.3F, block.getZ() + 0.5F);
                            level.addFreshEntity(entity);
                        }
                    }
                }
            }
        }
    }

    public static double ConvertDecimalToPercentage(double decimal) {
        return decimal > 1 ? 100 : decimal * 100;
    }

    public static String EntityTypeNameFormatter(EntityType entityType) {
        return entityType.toString().replace("entity.", "").replace(".", ":");
    }
}
