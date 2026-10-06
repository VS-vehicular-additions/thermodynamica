package com.Tribulla.thermodynamica.network;

import com.Tribulla.thermodynamica.client.HeatEnergyDebugOverlay;
import net.minecraft.network.FriendlyByteBuf;

public class HeatDebugOverlayPacket {

    private final boolean enabled;

    public HeatDebugOverlayPacket(boolean enabled) {
        this.enabled = enabled;
    }

    public static void encode(HeatDebugOverlayPacket packet, FriendlyByteBuf buf) {
        buf.writeBoolean(packet.enabled);
    }

    public static HeatDebugOverlayPacket decode(FriendlyByteBuf buf) {
        return new HeatDebugOverlayPacket(buf.readBoolean());
    }

    public static void handleClient(HeatDebugOverlayPacket packet) {
        HeatEnergyDebugOverlay.setEnabled(packet.enabled);
    }

    public boolean isEnabled() {
        return enabled;
    }
}
