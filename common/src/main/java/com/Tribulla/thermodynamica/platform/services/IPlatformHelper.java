package com.Tribulla.thermodynamica.platform.services;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public interface IPlatformHelper {

    /**
     * Gets the name of the current platform ("Forge" or "Fabric").
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Registers a block for the platform.
     */
    <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> blockSupplier);

    /**
     * Registers an item for the platform.
     */
    <T extends Item> Supplier<T> registerItem(String name, Supplier<T> itemSupplier);

    @FunctionalInterface
    interface BlockEntityFactory<T extends BlockEntity> {
        T create(net.minecraft.core.BlockPos pos, net.minecraft.world.level.block.state.BlockState state);
    }

    /**
     * Registers a block entity type for the platform.
     */
    <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntityType(
            String name,
            BlockEntityFactory<T> factory,
            Supplier<? extends Block> validBlock);

    /**
     * Sends a packet to a specific player.
     */
    void sendToPlayer(ServerPlayer player, Object packet);
}
