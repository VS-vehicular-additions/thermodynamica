package com.Tribulla.thermodynamica.platform;

import com.Tribulla.thermodynamica.Thermodynamica;
import com.Tribulla.thermodynamica.fabric.network.FabricNetworkHandler;
import com.Tribulla.thermodynamica.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> blockSupplier) {
        T registered = Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(Thermodynamica.MODID, name), blockSupplier.get());
        return () -> registered;
    }

    @Override
    public <T extends Item> Supplier<T> registerItem(String name, Supplier<T> itemSupplier) {
        T registered = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Thermodynamica.MODID, name), itemSupplier.get());
        return () -> registered;
    }

    @Override
    public <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntityType(
            String name,
            BlockEntityFactory<T> factory,
            Supplier<? extends Block> validBlock) {
        BlockEntityType<T> registered = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                new ResourceLocation(Thermodynamica.MODID, name),
                BlockEntityType.Builder.of(factory::create, validBlock.get()).build(null));
        return () -> registered;
    }

    @Override
    public void sendToPlayer(ServerPlayer player, Object packet) {
        FabricNetworkHandler.sendToPlayer(player, packet);
    }
}
