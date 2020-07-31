package com.github.corvblimey.oreventful.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.WorldEventS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Random;

public class CrumblingStoneBlock extends Block {
    public static final BooleanProperty CRUMBLING = BooleanProperty.of("crumbling");
    private static final int minTicksTilBreak = 2;  // Min ticks til destruction when adjacent crumble's destroyed
    private static final int maxTicksTilBreak = 6;  // Min ticks til destruction when adjacent crumble's destroyed

    public CrumblingStoneBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(CRUMBLING, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(CRUMBLING);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
        if(!world.isClient()) {
            triggerNearbyCrumbling((ServerWorld) world, pos);
        }
    }

    /* Starting to notice that more experienced folks doing similar use queues...very ominous
    * TODO: spawn a Big Honking Cube to test FPS/see how bad my newbie code is.
    * I doubt (?) veins will spawn such that more than, idk, a few dozen blocks are awaiting ticks. Hopefully going
    * for a "tunnel" shape
    */
    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.get(CRUMBLING)){
            triggerNearbyCrumbling(world, pos);
            // This little bit of stupid lets us "fake" a block break (because it feels satisfying)
            world.syncWorldEvent(null, 2001, pos, getRawIdFromState(state));
            world.setBlockState(pos, world.getFluidState(pos).getBlockState(), 3);
        }
    }

    public static void triggerNearbyCrumbling(ServerWorld world, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockPos offset = new BlockPos(pos.offset(direction, 1));
            BlockState newState = world.getBlockState(offset);
            Random random = new Random();
            if (newState.getBlock() instanceof CrumblingStoneBlock && !newState.get(CRUMBLING)) {
                CrumblingStoneBlock newBlock = (CrumblingStoneBlock) newState.getBlock();
                world.setBlockState(offset, newState.with(CRUMBLING, true));
                world.getBlockTickScheduler().schedule(offset, newBlock, newBlock.calcTicksTilBreak(random));
            }
        }
    }

    public int calcTicksTilBreak(Random random) {
        return random.nextInt(maxTicksTilBreak-minTicksTilBreak+1)+minTicksTilBreak;
    }
}
