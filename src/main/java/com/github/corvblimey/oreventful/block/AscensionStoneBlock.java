package com.github.corvblimey.oreventful.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;

public class AscensionStoneBlock extends Block {

    public AscensionStoneBlock(final Settings settings) { super(settings); }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
        if(!world.isClient()) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 100, 0));
            BlockPos targetPos = world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, pos);
            player.teleport(targetPos.getX(), targetPos.getY(), targetPos.getZ());
            world.playSound(null, targetPos, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f);
        }
    }
}
