package com.Tribulla.thermodynamica.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

public class ChunkHeatSyncPacket {

    public record HeatData(double celsius, BlockPos renderStatePos, Vec3 worldCenter) {
    }

    private final ChunkPos chunkPos;
    private final Map<BlockPos, HeatData> temperatures;

    public ChunkHeatSyncPacket(ChunkPos chunkPos, Map<BlockPos, HeatData> temperatures) {
        this.chunkPos = chunkPos;
        this.temperatures = temperatures;
    }

    public static void encode(ChunkHeatSyncPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.chunkPos.x);
        buf.writeInt(packet.chunkPos.z);
        buf.writeVarInt(packet.temperatures.size());
        for (Map.Entry<BlockPos, HeatData> entry : packet.temperatures.entrySet()) {
            buf.writeBlockPos(entry.getKey());
            buf.writeDouble(entry.getValue().celsius());
            buf.writeBlockPos(entry.getValue().renderStatePos());
            buf.writeDouble(entry.getValue().worldCenter().x);
            buf.writeDouble(entry.getValue().worldCenter().y);
            buf.writeDouble(entry.getValue().worldCenter().z);
        }
    }

    public static ChunkHeatSyncPacket decode(FriendlyByteBuf buf) {
        int cx = buf.readInt();
        int cz = buf.readInt();
        int count = buf.readVarInt();
        Map<BlockPos, HeatData> temps = new HashMap<>(count);
        for (int i = 0; i < count; i++) {
            BlockPos pos = buf.readBlockPos();
            double celsius = buf.readDouble();
            BlockPos renderStatePos = buf.readBlockPos();
            Vec3 worldCenter = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
            temps.put(pos, new HeatData(celsius, renderStatePos, worldCenter));
        }
        return new ChunkHeatSyncPacket(new ChunkPos(cx, cz), temps);
    }

    public static void handleClient(ChunkHeatSyncPacket packet) {
        for (Map.Entry<BlockPos, HeatData> entry : packet.temperatures.entrySet()) {
            HeatData data = entry.getValue();
            ClientHeatCache.update(entry.getKey(), data.celsius(), data.renderStatePos(), data.worldCenter());
        }
    }

    public ChunkPos getChunkPos() {
        return chunkPos;
    }

    public Map<BlockPos, HeatData> getTemperatures() {
        return temperatures;
    }
}
