package com.Tribulla.thermodynamica.fabric;

import com.Tribulla.thermodynamica.Thermodynamica;
import com.Tribulla.thermodynamica.ThermodynamicaCommand;
import com.Tribulla.thermodynamica.fabric.resource.FabricThermalPropertyResourceLoader;
import com.Tribulla.thermodynamica.network.HeatSyncManager;
import com.Tribulla.thermodynamica.simulation.SimulationEventHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;

public class FabricThermodynamica implements ModInitializer {

    @Override
    public void onInitialize() {
        Thermodynamica.init();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            ThermodynamicaCommand.register(dispatcher);
        });

        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(
                new FabricThermalPropertyResourceLoader(
                        Thermodynamica.getInstance().getConfigManager().getThermalPropertiesRegistry()));

        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            Thermodynamica.getInstance().onServerAboutToStart(server);
        });

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            Thermodynamica.getInstance().onServerStarting(server);
        });

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            Thermodynamica.getInstance().onServerStopping(server);
        });

        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            Thermodynamica.getInstance().onServerStopped(server);
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            SimulationEventHandler.onServerTick(server);
            HeatSyncManager.onServerTick(server);
        });

        ServerChunkEvents.CHUNK_LOAD.register((world, chunk) -> {
            SimulationEventHandler.onChunkLoad(world, chunk);
        });

        ServerChunkEvents.CHUNK_UNLOAD.register((world, chunk) -> {
            SimulationEventHandler.onChunkUnload(world, chunk);
        });

        ServerWorldEvents.LOAD.register((server, world) -> {
            SimulationEventHandler.onLevelLoad(world);
        });

        ServerWorldEvents.UNLOAD.register((server, world) -> {
            SimulationEventHandler.onLevelSave(world);
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            HeatSyncManager.onPlayerLogout(handler.player);
        });
    }
}
