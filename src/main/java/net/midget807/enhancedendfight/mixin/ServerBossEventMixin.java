package net.midget807.enhancedendfight.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBossEventPacket;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.BossEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.UUID;
import java.util.function.Function;

@Mixin(ServerBossEvent.class)
public abstract class ServerBossEventMixin extends BossEvent {
    @Shadow
    protected abstract void broadcast(Function<BossEvent, ClientboundBossEventPacket> packetGetter);

    public ServerBossEventMixin(UUID id, Component name, BossBarColor color, BossBarOverlay overlay) {
        super(id, name, color, overlay);
    }
}
