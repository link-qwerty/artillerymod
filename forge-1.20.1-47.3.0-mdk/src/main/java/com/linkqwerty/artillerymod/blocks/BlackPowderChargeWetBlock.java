package com.linkqwerty.artillerymod.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;

// Black Powder Charge (wet) Block  class
public class BlackPowderChargeWetBlock extends Block implements ICharges {
	private final BlockState broken_charge;

	// Default constructor
	public BlackPowderChargeWetBlock(RegistryObject<Block> registryBlock, Properties properties) {
		super(properties);
		this.broken_charge = registryBlock.get().defaultBlockState();
	}
	
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Level world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState state = world.getBlockState(pos);
		return (inFire(world, pos, state)) ? this.broken_charge : super.getStateForPlacement(context);
	}
	
	public boolean dropFromExplosion(Explosion explosion) {
		return false;
	}
	
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		if(inHotZone(world, pos, state)) {
	    	world.setBlock(pos, this.broken_charge, UPDATE_ALL);
		}
	}
}
