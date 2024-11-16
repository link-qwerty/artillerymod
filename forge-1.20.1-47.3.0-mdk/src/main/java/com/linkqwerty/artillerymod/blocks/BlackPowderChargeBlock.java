package com.linkqwerty.artillerymod.blocks;

//Imports
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.RegistryObject;

// Black Powder Charge (dry) Block  class
public class BlackPowderChargeBlock extends Block {
	private final BlockState wet_charge;

	// Default constructor
	public BlackPowderChargeBlock(RegistryObject<Block> registryBlock, Properties properties) {
		super(properties);
		this.wet_charge = registryBlock.get().defaultBlockState();
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Level getter = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState state = getter.getBlockState(pos);
		return (inWater(getter, pos, state)) ? this.wet_charge : super.getStateForPlacement(context);
	}
	
	@Override
	public void onCaughtFire(BlockState state, Level world, BlockPos pos, @Nullable net.minecraft.core.Direction face, @Nullable LivingEntity igniter) {
	    ExplodeCharge(world, pos, null);
	}
	
	@Override
	public void wasExploded(Level world, BlockPos pos, Explosion explosion) {
		ExplodeCharge(world, pos, null);
	    super.wasExploded(world, pos, explosion);
	}
	
	@Override
	public boolean dropFromExplosion(Explosion explosion) {
		return false;
	}
	
	@Override
	public boolean isRandomlyTicking(BlockState state) {
	    return true;
	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		if(inHumidityZone(state, world, pos)) {
	    		world.setBlock(pos, this.wet_charge, UPDATE_ALL);
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
	    ItemStack held = player.getItemInHand(hand);
	    	if(!world.isClientSide() && (held.is(Items.FLINT_AND_STEEL) || held.is(Items.FIRE_CHARGE))) {
	    	ExplodeCharge(world, pos, player);
	    	return InteractionResult.SUCCESS;
	    }
	    return super.use(state, world, pos, player, hand, hit);
	}

	private static boolean inHumidityZone(BlockState state, Level world, BlockPos pos) {
		if(world.isRainingAt(pos.above())) {
			return true;
		}
		for(BlockPos blockpos : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
	         if (state.canBeHydrated(world, pos, world.getFluidState(blockpos), blockpos)) {
	            return true;
	         }
	    }
		return false;
	}
	
	private static boolean inWater(BlockGetter getter, BlockPos pos, BlockState state) {
		boolean flag = false;
		BlockPos.MutableBlockPos blockpos_mutable = pos.mutable();
		
		for(Direction direction : Direction.values()) {
			BlockState state_next = getter.getBlockState(blockpos_mutable);
			if(direction != Direction.DOWN || state.canBeHydrated(getter, pos, state_next.getFluidState(), blockpos_mutable)) {
				blockpos_mutable.setWithOffset(pos, direction);
				state_next = getter.getBlockState(blockpos_mutable);
					
				if(state.canBeHydrated(getter, pos, state_next.getFluidState(), blockpos_mutable) && !state_next.isFaceSturdy(getter, pos, direction.getOpposite())) {
					flag = true;
					break;
				}
				
			}
		}
		return flag;
	}
	
	private static void ExplodeCharge(Level world, BlockPos pos, Player player) {
		world.addParticle(ParticleTypes.LARGE_SMOKE, pos.getX(), pos.getY() + 0.5D, pos.getZ(), 10.0D, 10.0D, 10.0D);
		world.explode(player, pos.getX(), pos.getY(), pos.getZ(), 8.0F, false, ExplosionInteraction.MOB);
		world.removeBlock(pos, false);
	}
}
