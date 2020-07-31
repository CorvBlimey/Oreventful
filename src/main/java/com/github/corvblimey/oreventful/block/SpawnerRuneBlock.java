package com.github.corvblimey.oreventful.block;

import com.github.corvblimey.oreventful.entity.SpawnerRuneBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class SpawnerRuneBlock extends Block implements BlockEntityProvider {

    public SpawnerRuneBlock(final Settings settings) { super(settings); }

    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new SpawnerRuneBlockEntity();
    }

    @Override
    public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockHitResult blockHitResult) {
        if (world.isClient) return ActionResult.SUCCESS;
        SpawnerRuneBlockEntity spawner = (SpawnerRuneBlockEntity) world.getBlockEntity(blockPos);
        if(spawner != null && !spawner.getIsCountingDown()) {
            spawner.startCountDown();
            world.playSound(null, blockPos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0f, 0.5f);
        }
        return ActionResult.SUCCESS;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void randomDisplayTick(final BlockState state, final World world, final BlockPos pos, final Random random) {
        SpawnerRuneBlockEntity spawner = (SpawnerRuneBlockEntity) world.getBlockEntity(pos);
        if(spawner.isPlayerInRange()) {
            // Assign random velocities
            double xVel = (random.nextDouble() - 0.5) / 10;
            double yVel = (random.nextDouble() - 0.5) / 10;
            double zVel = (random.nextDouble() - 0.5) / 10;
            world.addParticle(ParticleTypes.SMOKE, pos.getX(), pos.getY(), pos.getZ(), xVel, yVel, zVel);
        }
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
        SpawnerRuneBlockEntity spawner = (SpawnerRuneBlockEntity) world.getBlockEntity(pos);
        if(spawner != null)  // Should I assert instead?
            spawner.doRelease();
    }
}