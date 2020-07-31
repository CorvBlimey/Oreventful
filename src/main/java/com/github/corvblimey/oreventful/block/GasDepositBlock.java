package com.github.corvblimey.oreventful.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.PotionUtil;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class GasDepositBlock extends Block {
    // Values based on lingering potion for now
    private static final float INITIAL_RADIUS = -0.5f;
    private static final float FINAL_RADIUS = 2.0f;
    private static final int CLOUD_DURATION = 4;  // At least I think this is duration?
    private static final StatusEffectInstance[] gasEffects = new StatusEffectInstance[]{
            new StatusEffectInstance(StatusEffects.REGENERATION, 200, 1),
            new StatusEffectInstance(StatusEffects.NAUSEA, 300, 0),
            new StatusEffectInstance(StatusEffects.NIGHT_VISION, 600, 0),
            new StatusEffectInstance(StatusEffects.STRENGTH, 600, 1),
            new StatusEffectInstance(StatusEffects.HASTE, 600, 1),
            new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 1800, 0),
            new StatusEffectInstance(StatusEffects.BLINDNESS, 200, 0)};
    private Random effectPicker = new Random();

    public GasDepositBlock(final Settings settings) { super(settings); }

    @Environment(EnvType.CLIENT)
    @Override
    public void randomDisplayTick(final BlockState state, final World world, final BlockPos pos, final Random random) {
        // Assign random velocities
        double xVel = (random.nextDouble()-0.5)/3;
        double yVel = (random.nextDouble()-0.5)/3;
        double zVel = (random.nextDouble()-0.5)/3;
        world.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, pos.getX(), pos.getY(), pos.getZ(), xVel, yVel, zVel);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
        final AreaEffectCloudEntity gasPlume = new AreaEffectCloudEntity(world, pos.getX(), pos.getY()+0.5, pos.getZ());
        gasPlume.setRadius(FINAL_RADIUS);
        gasPlume.setRadiusOnUse(INITIAL_RADIUS);
        gasPlume.setWaitTime(CLOUD_DURATION);
        gasPlume.setRadiusGrowth(-gasPlume.getRadius() / gasPlume.getDuration());
        gasPlume.addEffect(gasEffects[effectPicker.nextInt(gasEffects.length)]);
        world.spawnEntity(gasPlume);
        world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0f, 1.5f);
    }
}
