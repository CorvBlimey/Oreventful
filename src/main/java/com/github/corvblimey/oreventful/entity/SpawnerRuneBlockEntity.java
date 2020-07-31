package com.github.corvblimey.oreventful.entity;

import com.github.corvblimey.oreventful.Oreventful;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.WeightedPicker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.MobSpawnerEntry;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class SpawnerRuneBlockEntity extends BlockEntity implements Tickable {

    private static final List<MobRuneEntry> mobProbabilities = new ArrayList<MobRuneEntry>();
    private static final int spawnDelay = 120; // How many ticks between spawn attempts
    private static final int requiredPlayerRange = 8; // Max distance of player from block

    private int ticksSinceLastSpawn = 0;
    private int mobsLeftToSpawn = 10;  // Maybe a bit high, but eh.
    private boolean isCountingDown = false;
    private int countDown = 100;  // 5 seconds seems fair, prolly configurable in the future

    static class MobRuneEntry extends WeightedPicker.Entry{
        private final EntityType toSpawn;
        public MobRuneEntry(final int weight, final EntityType entity) {
            super(weight);
            this.toSpawn = entity;
        }
    }

    public SpawnerRuneBlockEntity() {
        super(Oreventful.SPAWNER_RUNE_BLOCK_ENTITY);
        // Every SpawnerRune has the same spawn list...for now.
        mobProbabilities.add(new MobRuneEntry(3, EntityType.SKELETON));
        mobProbabilities.add(new MobRuneEntry(12, EntityType.ZOMBIE));
        mobProbabilities.add(new MobRuneEntry(1, EntityType.WITCH));
        mobProbabilities.add(new MobRuneEntry(5, EntityType.SPIDER));
    }

    // Use at some point to change block visual state
    public int getMobsLeftToSpawn() {
        return mobsLeftToSpawn;
    }

    public boolean getIsCountingDown() {
        return isCountingDown;
    }

    public void startCountDown() {
        isCountingDown = true;
    }

    @Override
    public void tick() {
        if(world.getDifficulty() == Difficulty.PEACEFUL)
            return;

        // No mobs are spawned during countdown
        if (this.isCountingDown) {
            this.countDown --;
            if(this.countDown % 5 == 0)
                world.addParticle(ParticleTypes.CRIT, pos.getX() + Math.random(), pos.getY() + Math.random(), pos.getZ() + Math.random(), 0, 0, 0);
            if(this.countDown < 1) {
                world.breakBlock(pos, false);
                doRelease();
            }
            return;
        }

        if (ticksSinceLastSpawn > spawnDelay){
            if (this.getWorld() instanceof ServerWorld){
                boolean spawnSucceeded = attemptToSpawnMob();
                if (spawnSucceeded) {
                    ticksSinceLastSpawn = 0;
                    if (mobsLeftToSpawn < 1)
                        world.removeBlock(pos, false);
                } else {
                    ticksSinceLastSpawn -= 30; // 1.5-second penalty before trying again.
                }
            }
        }
        ticksSinceLastSpawn ++;
    }

    // Release all remaining mobs at once
    public void doRelease() {
        final int maxAttempts = this.mobsLeftToSpawn*10;
        int attemptsMade = 0;
        while(this.mobsLeftToSpawn > 0 && attemptsMade < maxAttempts){
            attemptToSpawnMob();
            attemptsMade ++;
        }
    }

    // MobSpawnerLogic had this as private, so I stole it
    public boolean isPlayerInRange() {
        final BlockPos pos = this.getPos();
        return this.getWorld().isPlayerInRange(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, requiredPlayerRange);
    }

    // TODO: Less stupid way of picking where mobs spawn
    private boolean attemptToSpawnMob() {
        if (!this.isPlayerInRange())
            return false;
        final World world = this.getWorld();
        if (!(world instanceof ServerWorld))
            return false;

        BlockPos pos = getPos();

        EntityType typeToCreate = WeightedPicker.<MobRuneEntry>getRandom(this.getWorld().random, mobProbabilities).toSpawn;
        MobEntity spawnedEntity = (MobEntity) typeToCreate.create(world);
        spawnedEntity.refreshPositionAndAngles(pos.getX() + 0.5, pos.getY()+1,pos.getZ() + 0.5, 0.0f, 0.0f);
        spawnedEntity.initialize(world, world.getLocalDifficulty(this.pos), SpawnReason.SPAWNER, null, null);

        world.spawnEntity(spawnedEntity);
        this.mobsLeftToSpawn --;
        return true;
    }
}
