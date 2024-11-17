package com.linkqwerty.artillerymod.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags.Biomes;

public interface ICharges {
	
	default boolean inHumidityZone(Level world, BlockPos pos, BlockState state) {
		boolean flag = false;
		
		if(world.isRainingAt(pos.above())) {
			flag = true;
		} else if(world.getBiome(pos).containsTag(Biomes.IS_WET)) {
			flag = true;
		} else { 
			for(BlockPos blockpos : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
				if (state.canBeHydrated(world, pos, world.getFluidState(blockpos), blockpos)) {
					flag = true;
					break;
				}
			}
	    }		
		return flag;
	}
	
	default boolean inHotZone(Level world, BlockPos pos, BlockState state) {
		boolean flag = false;
		
		if(world.getBiome(pos).containsTag(Biomes.IS_HOT)) {
			flag = true;
		} else { 
			for(BlockPos blockpos : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
				if (world.getBlockState(blockpos).is(Blocks.FIRE) || world.getBlockState(blockpos).is(Blocks.LAVA)) {
					flag = true;
					break;
				}
			}
	    }		
		return flag;
	}
	
	default boolean inWater(Level world, BlockPos pos, BlockState state) {
		boolean flag = false;
		BlockPos.MutableBlockPos blockpos_mutable = pos.mutable();
		BlockState state_neighbor = world.getBlockState(blockpos_mutable);
		
		for(Direction direction : Direction.values()) {
			blockpos_mutable.setWithOffset(pos, direction);
			state_neighbor = world.getBlockState(blockpos_mutable);
			if(state.canBeHydrated(world, pos, state_neighbor.getFluidState(), blockpos_mutable)) {
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	default boolean inFire(Level world, BlockPos pos, BlockState state) {
		boolean flag = false;
		BlockPos.MutableBlockPos blockpos_mutable = pos.mutable();
		BlockState state_neighbor = world.getBlockState(blockpos_mutable);
		
		for(Direction direction : Direction.values()) {
			blockpos_mutable.setWithOffset(pos, direction);
			state_neighbor = world.getBlockState(blockpos_mutable);
			if(state_neighbor.is(Blocks.FIRE) || state_neighbor.is(Blocks.LAVA)) {
				flag = true;
				break;
			}
		}
		return flag;
	}
}
