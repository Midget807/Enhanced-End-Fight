package net.midget807.enhancedendfight.network.s2c.packet;

import io.netty.buffer.ByteBuf;
import net.midget807.enhancedendfight.EnhancedEndFightMain;
import net.midget807.enhancedendfight.entity.client.ClientTenacityDataHolder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record TenacityBossBarProgressPacket(String bossUuid, float tenacityProgress) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<TenacityBossBarProgressPacket> TYPE = new Type<>(EnhancedEndFightMain.id("tenacity"));

    public static final StreamCodec<ByteBuf, TenacityBossBarProgressPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, TenacityBossBarProgressPacket::bossUuid,
            ByteBufCodecs.FLOAT, TenacityBossBarProgressPacket::tenacityProgress,
            TenacityBossBarProgressPacket::new
    );

    public static void handle(TenacityBossBarProgressPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientTenacityDataHolder.TENACITY.put(UUID.fromString(packet.bossUuid()), packet.tenacityProgress());
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
