package net.midget807.enhancedendfight.event;

import net.midget807.enhancedendfight.EnhancedEndFightMain;
import net.midget807.enhancedendfight.network.s2c.packet.TenacityBossBarProgressPacket;
import net.midget807.enhancedendfight.registry.ModServerPayloadHandler;
import net.midget807.enhancedendfight.registry.client.ModClientPayloadHandler;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = EnhancedEndFightMain.MODID)
public class NetworkRegistryEvent {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playBidirectional(
                TenacityBossBarProgressPacket.TYPE,
                TenacityBossBarProgressPacket.CODEC,
                new DirectionalPayloadHandler<>(
                        ModClientPayloadHandler::handleOnMain,
                        ModServerPayloadHandler::handleOnMain
                )
        );
    }

}
