package com.Tribulla.thermodynamica.fabric.network;

import com.Tribulla.thermodynamica.Thermodynamica;
import com.Tribulla.thermodynamica.network.ChunkHeatSyncPacket;
import com.Tribulla.thermodynamica.network.DebugInfoPacket;
import com.Tribulla.thermodynamica.network.HeatDebugOverlayPacket;
import com.Tribulla.thermodynamica.network.HeatSyncPacket;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class FabricNetworkHandler {

    public static final ResourceLocation CHUNK_HEAT_SYNC = new ResourceLocation(Thermodynamica.MODID, "chunk_heat_sync");
    public static final ResourceLocation DEBUG_INFO = new ResourceLocation(Thermodynamica.MODID, "debug_info");
    public static final ResourceLocation HEAT_DEBUG_OVERLAY = new ResourceLocation(Thermodynamica.MODID, "heat_debug_overlay");
    public static final ResourceLocation HEAT_SYNC = new ResourceLocation(Thermodynamica.MODID, "heat_sync");

    public static void sendToPlayer(ServerPlayer player, Object packet) {
        if (packet instanceof ChunkHeatSyncPacket p) {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            ChunkHeatSyncPacket.encode(p, buf);
            ServerPlayNetworking.send(player, CHUNK_HEAT_SYNC, buf);
        } else if (packet instanceof DebugInfoPacket p) {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            DebugInfoPacket.encode(p, buf);
            ServerPlayNetworking.send(player, DEBUG_INFO, buf);
        } else if (packet instanceof HeatDebugOverlayPacket p) {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            HeatDebugOverlayPacket.encode(p, buf);
            ServerPlayNetworking.send(player, HEAT_DEBUG_OVERLAY, buf);
        } else if (packet instanceof HeatSyncPacket p) {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            HeatSyncPacket.encode(p, buf);
            ServerPlayNetworking.send(player, HEAT_SYNC, buf);
        }
    }

    public static void registerClientReceivers() {
        ClientPlayNetworking.registerGlobalReceiver(CHUNK_HEAT_SYNC, (client, handler, buf, responseSender) -> {
            ChunkHeatSyncPacket packet = ChunkHeatSyncPacket.decode(buf);
            client.execute(() -> ChunkHeatSyncPacket.handleClient(packet));
        });

        ClientPlayNetworking.registerGlobalReceiver(DEBUG_INFO, (client, handler, buf, responseSender) -> {
            DebugInfoPacket packet = DebugInfoPacket.decode(buf);
            client.execute(() -> DebugInfoPacket.handleClient(packet));
        });

        ClientPlayNetworking.registerGlobalReceiver(HEAT_DEBUG_OVERLAY, (client, handler, buf, responseSender) -> {
            HeatDebugOverlayPacket packet = HeatDebugOverlayPacket.decode(buf);
            client.execute(() -> HeatDebugOverlayPacket.handleClient(packet));
        });

        ClientPlayNetworking.registerGlobalReceiver(HEAT_SYNC, (client, handler, buf, responseSender) -> {
            HeatSyncPacket packet = HeatSyncPacket.decode(buf);
            client.execute(() -> HeatSyncPacket.handleClient(packet));
        });
    }
}
