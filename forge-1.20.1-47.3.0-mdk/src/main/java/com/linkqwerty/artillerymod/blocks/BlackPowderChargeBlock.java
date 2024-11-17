package com.linkqwerty.artillerymod.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.RegistryObject;

// Black Powder Charge (dry) Block  class
public class BlackPowderChargeBlock extends Block implements ICharges {
	private final BlockState wet_charge;

	// Default constructor
	public BlackPowderChargeBlock(RegistryObject<Block> registryBlock, Properties properties) {
		super(properties);
		this.wet_charge = registryBlock.get().defaultBlockState();
	}
	
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Level world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState state = world.getBlockState(pos);
		return (inWater(world, pos, state)) ? this.wet_charge : super.getStateForPlacement(context);
	}
		
	public void wasExploded(Level world, BlockPos pos, Explosion explosion) {
		ExplodeCharge(world, pos, null);
	}
	
	public boolean dropFromExplosion(Explosion explosion) {
		return false;
	}
	
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		if(inHumidityZone(world, pos, state)) {
	    	world.setBlock(pos, this.wet_charge, UPDATE_ALL);
		} else if(inFire(world, pos, state)) {
			ExplodeCharge(world, pos, null);
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
	    ItemStack held = player.getItemInHand(hand);
	    	if(held.is(Items.FLINT_AND_STEEL) || held.is(Items.FIRE_CHARGE) || held.is(Items.BLAZE_ROD)) {
	    	ExplodeCharge(world, pos, player);
	    	return InteractionResult.SUCCESS;
	    }
	    return super.use(state, world, pos, player, hand, hit);
	}

	private static void ExplodeCharge(Level world, BlockPos pos, Player player) {
		if(!world.isClientSide) {
			world.addParticle(ParticleTypes.LARGE_SMOKE, pos.getX(), pos.getY() + 0.5D, pos.getZ(), 10.0D, 10.0D, 10.0D);
			world.explode(player, pos.getX(), pos.getY(), pos.getZ(), 8.0F, false, ExplosionInteraction.TNT);
			world.removeBlock(pos, false);
		}
	}
}
