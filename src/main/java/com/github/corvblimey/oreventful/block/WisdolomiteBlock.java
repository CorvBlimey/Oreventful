package com.github.corvblimey.oreventful.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.OreBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

public class WisdolomiteBlock extends OreBlock {
    // You get a max of 7 for mining emerald
    private static final int minExperience = 7;
    private static final int maxExperience = 22;

    public WisdolomiteBlock(final Settings settings) { super(settings); }

    @Override
    protected int getExperienceWhenMined(final Random random) { return MathHelper.nextInt(random, minExperience, maxExperience); }
}
