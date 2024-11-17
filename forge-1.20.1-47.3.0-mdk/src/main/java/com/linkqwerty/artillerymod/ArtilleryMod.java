package com.linkqwerty.artillerymod;

//Imports
import com.linkqwerty.artillerymod.blocks.BlackPowderChargeBlock;
import com.linkqwerty.artillerymod.blocks.BlackPowderChargeWetBlock;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

// Mod main class
@Mod(ArtilleryMod.MODID)
public class ArtilleryMod {
	// Mod identifier
    public static final String MODID = "artillerymod";
    // DeferredRegister for blocks
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    // DeferredRegister for items (i.e. item_blocks)
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    // Define a Deferred Register to hold CreativeModeTabs collection
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    
    // Blocks
    public static final RegistryObject<Block> BLACKPOWDERCHARGEBROKEN_BLOCK = BLOCKS.register("blackpowderchargesbroken_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.WOOL).instrument(NoteBlockInstrument.BASEDRUM).strength(0.4F).sound(SoundType.WOOL).ignitedByLava()));
    public static final RegistryObject<Block> BLACKPOWDERCHARGEWET_BLOCK = BLOCKS.register("blackpowderchargewet_block", () -> new BlackPowderChargeWetBlock(BLACKPOWDERCHARGEBROKEN_BLOCK, BlockBehaviour.Properties.of().mapColor(MapColor.WOOL).instrument(NoteBlockInstrument.BASS).strength(0.4F).randomTicks().sound(SoundType.WOOL).ignitedByLava()));
    public static final RegistryObject<Block> BLACKPOWDERCHARGE_BLOCK = BLOCKS.register("blackpowdercharge_block", () -> new BlackPowderChargeBlock(BLACKPOWDERCHARGEWET_BLOCK, BlockBehaviour.Properties.of().mapColor(MapColor.WOOL).instrument(NoteBlockInstrument.GUITAR).strength(0.4F).randomTicks().sound(SoundType.WOOL).ignitedByLava()));
    
    // Items
    public static final RegistryObject<Item> BLACKPOWDERCHARGEBROKEN_BLOCK_ITEM = ITEMS.register("blackpowderchargebroken_item", () -> new BlockItem(BLACKPOWDERCHARGEBROKEN_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> BLACKPOWDERCHARGEWET_BLOCK_ITEM = ITEMS.register("blackpowderchargewet_item", () -> new BlockItem(BLACKPOWDERCHARGEWET_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> BLACKPOWDERCHARGE_BLOCK_ITEM = ITEMS.register("blackpowdercharge_item", () -> new BlockItem(BLACKPOWDERCHARGE_BLOCK.get(), new Item.Properties()));
    
    
    // Create a mod creative tab
    public static final RegistryObject<CreativeModeTab> ARTILLERY_TAB = CREATIVE_MODE_TABS.register("artillerymod_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> BLACKPOWDERCHARGE_BLOCK_ITEM.get().getDefaultInstance())
            .title(Component.translatable("artillerymod.artillerymod_tab"))
            .displayItems((parameters, output) -> {
                output.accept(BLACKPOWDERCHARGE_BLOCK_ITEM.get());
                output.accept(BLACKPOWDERCHARGEWET_BLOCK_ITEM.get());
                output.accept(BLACKPOWDERCHARGEBROKEN_BLOCK_ITEM.get());
            }).build());

    // Constructor
    public ArtilleryMod()
    {
        // Define EventBus
    	IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    	
        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }
}