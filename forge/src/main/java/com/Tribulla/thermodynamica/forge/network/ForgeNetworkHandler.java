package com.Tribulla.thermodynamica.forge.network;

import com.Tribulla.thermodynamica.Thermodynamica;
import com.Tribulla.thermodynamica.network.ChunkHeatSyncPacket;
import com.Tribulla.thermodynamica.network.DebugInfoPacket;
import com.Tribulla.thermodynamica.network.HeatDebugOverlayPacket;
import com.Tribulla.thermodynamica.network.HeatSyncPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ForgeNetworkHandler {

    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Thermodynamica.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals);

    private static int id = 0;

    public static void register() {
        CHANNEL.registerMessage(id++, HeatSyncPacket.class,
                HeatSyncPacket::encode,
                HeatSyncPacket::decode,
                (msg, ctx) -> {
                    ctx.get().enqueueWork(() -> HeatSyncPacket.handleClient(msg));
                    ctx.get().setPacketHandled(true);
                });

        CHANNEL.registerMessage(id++, ChunkHeatSyncPacket.class,
                ChunkHeatSyncPacket::encode,
                ChunkHeatSyncPacket::decode,
                (msg, ctx) -> {
                    ctx.get().enqueueWork(() -> ChunkHeatSyncPacket.handleClient(msg));
                    ctx.get().setPacketHandled(true);
                });

        CHANNEL.registerMessage(id++, DebugInfoPacket.class,
                DebugInfoPacket::encode,
                DebugInfoPacket::decode,
                (msg, ctx) -> {
                    ctx.get().enqueueWork(() -> DebugInfoPacket.handleClient(msg));
                    ctx.get().setPacketHandled(true);
                });

        CHANNEL.registerMessage(id++, HeatDebugOverlayPacket.class,
                HeatDebugOverlayPacket::encode,
                HeatDebugOverlayPacket::decode,
                (msg, ctx) -> {
                    ctx.get().enqueueWork(() -> HeatDebugOverlayPacket.handleClient(msg));
                    ctx.get().setPacketHandled(true);
                });

        Thermodynamica.LOGGER.debug("Forge network channel registered");
    }
}
