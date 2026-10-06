package com.Tribulla.thermodynamica.fabric.client;

import com.Tribulla.thermodynamica.client.HeatEnergyDebugOverlay;
import com.Tribulla.thermodynamica.fabric.network.FabricNetworkHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

public class FabricThermodynamicaClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FabricNetworkHandler.registerClientReceivers();

        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            HeatEnergyDebugOverlay.render(context.matrixStack(), context.camera().getPosition());
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            HeatEnergyDebugOverlay.onDisconnect();
        });
    }
}
