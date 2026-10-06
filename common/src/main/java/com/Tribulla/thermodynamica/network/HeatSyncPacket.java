package com.Tribulla.thermodynamica.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public class HeatSyncPacket {

    private final BlockPos pos;
    private final double celsius;

    public HeatSyncPacket(BlockPos pos, double celsius) {
        this.pos = pos;
        this.celsius = celsius;
    }

    public static void encode(HeatSyncPacket packet, FriendlyByteBuf buf) {
        buf.writeBlockPos(packet.pos);
        buf.writeDouble(packet.celsius);
    }

    public static HeatSyncPacket decode(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        double celsius = buf.readDouble();
        return new HeatSyncPacket(pos, celsius);
    }

    public static void handleClient(HeatSyncPacket packet) {
        ClientHeatCache.update(packet.pos, packet.celsius);
    }

    public BlockPos getPos() {
        return pos;
    }

    public double getCelsius() {
        return celsius;
    }
}
