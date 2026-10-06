package com.Tribulla.thermodynamica.debug;

import com.Tribulla.thermodynamica.platform.Services;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Supplier;

public class DebugRegistry {

    public static final Supplier<Block> VARIABLE_HEAT_BLOCK = Services.PLATFORM.registerBlock("variable_heat_block",
            () -> new VariableHeatBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED)
                    .strength(1.5f)
                    .noOcclusion()));

    public static final Supplier<Item> VARIABLE_HEAT_ITEM = Services.PLATFORM.registerItem("variable_heat_block",
            () -> new BlockItem(VARIABLE_HEAT_BLOCK.get(), new Item.Properties()));

    public static final Supplier<BlockEntityType<VariableHeatBlockEntity>> VARIABLE_HEAT_BLOCK_ENTITY = Services.PLATFORM.registerBlockEntityType(
            "variable_heat_block",
            VariableHeatBlockEntity::new,
            VARIABLE_HEAT_BLOCK);

    public static final Supplier<Item> HEAT_INSPECTOR = Services.PLATFORM.registerItem("heat_inspector",
            () -> new HeatInspectorItem(new Item.Properties().stacksTo(1)));

    public static final Supplier<Item> ENERGY_INJECTOR = Services.PLATFORM.registerItem("energy_injector",
            () -> new EnergyInjectorItem(new Item.Properties().stacksTo(1)));

    public static void init() {
        // Triggers static class loading and registry initialization
    }
}
