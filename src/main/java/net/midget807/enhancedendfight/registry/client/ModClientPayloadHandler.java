package net.midget807.enhancedendfight.registry.client;

import net.midget807.enhancedendfight.entity.client.ClientTenacityDataHolder;
import net.midget807.enhancedendfight.network.s2c.packet.TenacityBossBarProgressPacket;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public class ModClientPayloadHandler {
    public static void handleOnMain(TenacityBossBarProgressPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientTenacityDataHolder.TENACITY.put(UUID.fromString(packet.bossUuid()), packet.tenacityProgress());
        });
    }
}
