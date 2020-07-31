package com.github.corvblimey.oreventful.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class LavaloggedStoneBlock extends Block {
    public static final BooleanProperty DISSOLVING = BooleanProperty.of("dissolving");
    private static final int durability = 20;  // there's a 1-in-<durability> chance of dissolving when stepped on (per tick?)
    private static final int ticksTilBreak = 20;  // how many ticks til it dissolves

    public LavaloggedStoneBlock(final Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(DISSOLVING, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(DISSOLVING);
    }
    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
        if (world instanceof ServerWorld)
            dissolve((ServerWorld)world, pos);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.get(DISSOLVING)){
            world.breakBlock(pos, false);
            dissolve(world, pos);
        }
    }

    @Override
    public void onSteppedOn(final World world, final BlockPos pos, final Entity entity) {
        super.onSteppedOn(world, pos, entity);
        possiblyDissolve(world, pos);
    }

    public void dissolve(ServerWorld world, BlockPos pos){
        if(!world.isClient()) {
            world.setBlockState(pos, Blocks.LAVA.getDefaultState(), 3);
        } else {
            world.playSound(null, pos, SoundEvents.BLOCK_LAVA_POP, SoundCategory.BLOCKS, 1.0f, 1.5f);
        }
    }

    public void possiblyDissolve(final World world, final BlockPos pos) {
        if(!world.isClient()) {
            BlockState blockState = world.getBlockState(pos);
            if(!blockState.get(DISSOLVING)){
                if(world.random.nextInt(durability)==0) {
                    world.setBlockState(pos, blockState.with(DISSOLVING, true));
                    world.getBlockTickScheduler().schedule(pos, blockState.getBlock(), ticksTilBreak);
                    // So the server tells the client to play the sound or what?
                    world.playSound(null, pos, SoundEvents.BLOCK_GRAVEL_BREAK, SoundCategory.BLOCKS, 1.0f, 1.0f);
                }
            }
        }
    }
}
